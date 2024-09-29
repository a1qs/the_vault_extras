package com.a1qs.the_vault_extras.mixins.vault;

import iskallia.vault.Vault;
import iskallia.vault.init.ModSounds;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.behaviour.VaultBehaviour;
import iskallia.vault.world.vault.logic.task.VaultTask;
import iskallia.vault.world.vault.player.VaultMember;
import iskallia.vault.world.vault.player.VaultSpectator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

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
//            playerEntity.inventory.func_234564_a_((stack) -> {
//                return true;
//            }, -1, playerEntity.container.func_234641_j_());
//            playerEntity.openContainer.detectAndSendChanges();
//            playerEntity.container.onCraftMatrixChanged(playerEntity.inventory);
//            playerEntity.updateHeldItem();
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


}
