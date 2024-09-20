package com.a1qs.the_vault_extras.mixins;

import com.a1qs.the_vault_extras.mixins.invokers.InvokerPartyCommand;
import iskallia.vault.Vault;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModSounds;
import iskallia.vault.world.data.VaultPartyData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.raid.RaidProperties;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.event.VaultEvent;
import iskallia.vault.world.vault.gen.VaultGenerator;
import iskallia.vault.world.vault.logic.VaultChestPity;
import iskallia.vault.world.vault.logic.VaultSpawner;
import iskallia.vault.world.vault.logic.behaviour.VaultBehaviour;
import iskallia.vault.world.vault.logic.condition.VaultCondition;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.logic.task.VaultTask;
import iskallia.vault.world.vault.player.VaultMember;
import iskallia.vault.world.vault.player.VaultPlayerType;
import iskallia.vault.world.vault.player.VaultRunner;
import iskallia.vault.world.vault.player.VaultSpectator;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static iskallia.vault.world.vault.VaultRaid.*;

@Mixin(value = VaultRaid.class, remap = false)
public class MixinVaultRaid {
    @Final
    @Shadow
    public static VaultTask EXIT_DEATH = VaultTask.register(Vault.id("exit_death_test"), (vault, player, world) -> {
        player.runIfPresent(world.getServer(), (playerEntity) -> {
            if (player instanceof VaultSpectator) {
                playerEntity.interactionManager.setGameType(((VaultSpectator)player).oldGameType);
            }

            world.playSound((PlayerEntity)null, playerEntity.getPosX(), playerEntity.getPosY(), playerEntity.getPosZ(), ModSounds.TIMER_KILL_SFX, SoundCategory.PLAYERS, 1.0F, 1.0F);
            world.playMovingSound((PlayerEntity)null, playerEntity, ModSounds.TIMER_KILL_SFX, SoundCategory.PLAYERS, 1.0F, 1.0F);
            playerEntity.attackEntityFrom((new DamageSource("vaultFailed")).setDamageBypassesArmor().setDamageAllowedInCreativeMode(), 1.0E8F);
            player.exit();

            // Leave party if one exists
            VaultPartyData data = VaultPartyData.get((ServerWorld) playerEntity.world);
            Optional<VaultPartyData.Party> party = data.getParty(playerEntity.getUniqueID());
            if (party.isPresent()) {
                Commands command = new Commands(Commands.EnvironmentType.ALL);
                command.handleCommand(playerEntity.getCommandSource(), "party leave");

                player.getObjectives().clear();
                player.getAllObjectives().clear();
                vault.getAllObjectives().forEach((objective) -> {
                    objective.notifyBail(vault, player, world);
                });
            }

            // Delete inventory
            playerEntity.inventory.func_234564_a_((stack) -> {
                return true;
            }, -1, playerEntity.container.func_234641_j_());
            playerEntity.openContainer.detectAndSendChanges();
            playerEntity.container.onCraftMatrixChanged(playerEntity.inventory);
            playerEntity.updateHeldItem();
            HIDE_OVERLAY.execute(vault, player, world);
            UUID parent = (UUID)vault.getProperties().getBase(PARENT).orElse((UUID) null);
            VaultRaid parentVault = parent == null ? null : VaultRaidData.get(world).get(parent);
            if (parentVault != null) {
                parentVault.getProperties().getBase(LOBBY).ifPresent((lobby) -> {
                    VaultMember member = new VaultMember(player.getPlayerId());
                    member.getProperties().create(CAN_HEAL, true);
                    member.getBehaviours().add(new VaultBehaviour(IS_OUTSIDE, TP_TO_START));
                    member.getBehaviours().add(new VaultBehaviour(IS_DEAD.negate(), TICK_LOBBY));
                    parentVault.getPlayers().add(member);
                    player.runIfPresent(world.getServer(), (sPlayer) -> {
                        lobby.snapshots.restoreSnapshot(sPlayer);
                    });
                    parentVault.getProperties().create(LOBBY, lobby);
                    lobby.exitVault(vault, world, parentVault, member, playerEntity, true);
                    vault.getPlayers().remove(player);
                });
                vault.getProperties().create(FORCE_ACTIVE, false);
            }

        });
    });;


    @Shadow @Final public static VaultCondition NO_OBJECTIVES_LEFT;

    @Shadow @Final public static VaultCondition IS_RUNNER;

    @Shadow @Final public static VaultCondition IS_OUTSIDE;

    /**
     * @author Josh
     * @reason Remove co-op deaths wiping party
     */
    @Overwrite
    public static VaultRaid coop(VaultGenerator generator, VaultTask initializer, RaidProperties properties, VaultObjective objective, List<VaultEvent<?>> events, Map<VaultPlayerType, Set<ServerPlayerEntity>> playersMap) {
        MinecraftServer srv = (MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        VaultRaid vault = new VaultRaid(generator, initializer, properties, events, (Iterable)playersMap.entrySet().stream().flatMap((entry) -> {
            VaultPlayerType type = (VaultPlayerType)entry.getKey();
            Set<ServerPlayerEntity> players = (Set)entry.getValue();
            if (type == VaultPlayerType.RUNNER) {
                return players.stream().map((player) -> {
                    VaultRunner runner = new VaultRunner(player.getUniqueID());
                    runner.getBehaviours().add(new VaultBehaviour(NO_OBJECTIVES_LEFT_GLOBALLY.and(NO_TIME_LEFT), REMOVE_SCAVENGER_ITEMS.then(REMOVE_INVENTORY_RESTORE_SNAPSHOTS).then(GRANT_EXP_COMPLETE.then(EXIT_SAFELY))));
                    runner.getBehaviours().add(new VaultBehaviour(IS_RUNNER.and(NO_TIME_LEFT), EXIT_DEATH_ALL));
                    runner.getBehaviours().add(new VaultBehaviour(IS_RUNNER.and(IS_DEAD), EXIT_DEATH));
                    runner.getBehaviours().add(new VaultBehaviour(IS_FINISHED.negate(), TICK_SPAWNER.then(TICK_CHEST_PITY)));
                    runner.getBehaviours().add(new VaultBehaviour(AFTER_GRACE_PERIOD.and(IS_FINISHED.negate()), TICK_INFLUENCES));
                    runner.getBehaviours().add(new VaultBehaviour(IS_RUNNER, CHECK_BAIL_COOP));
                    runner.getBehaviours().add(new VaultBehaviour(NO_ACTIVE_RUNNERS_LEFT, REMOVE_SCAVENGER_ITEMS.then(REMOVE_INVENTORY_RESTORE_SNAPSHOTS).then(GRANT_EXP_BAIL.then(EXIT_SAFELY))));
//                    runner.getBehaviours().add(new VaultBehaviour(IS_RUNNER).and(IS_OUTSIDE), )
                    runner.getProperties().create(SPAWNER, new VaultSpawner());
                    runner.getProperties().create(CHEST_PITY, new VaultChestPity());
                    runner.getTimer().start(objective.getVaultTimerStart(ModConfigs.VAULT_GENERAL.getTickCounter()));
                    return runner;
                });
            } else {
                if (type == VaultPlayerType.SPECTATOR) {
                }

                return Stream.empty();
            }
        }).collect(Collectors.toList()));
        vault.getAllObjectives().add(objective.thenComplete(LEVEL_UP_GEAR).thenComplete(VICTORY_SCENE));
        vault.getAllObjectives().forEach((obj) -> {
            obj.initialize(srv, vault);
        });
        return vault;
    }
}
