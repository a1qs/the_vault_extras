package com.a1qs.the_vault_extras.mixins.vault;


import com.a1qs.the_vault_extras.mixins.invokers.InvokerEntityEvents;
import iskallia.vault.Vault;
import iskallia.vault.entity.AggressiveCowEntity;
import iskallia.vault.entity.EternalEntity;
import iskallia.vault.entity.VaultFighterEntity;
import iskallia.vault.event.EntityEvents;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.type.SoulShardTalent;
import iskallia.vault.world.data.PlayerTalentsData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.influence.VaultAttributeInfluence;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Mixin(value = EntityEvents.class, remap = false)
public class MixinEntityEvents {

    @Shadow
    private static final Random rand = new Random();

    /**
     * @author a1qs
     * @reason remove soul shard talent requirement
     */

    @Overwrite
    private static boolean addShardDrops(World world, Entity killed, ServerPlayerEntity killer, VaultRaid vault, Collection<ItemEntity> drops) {
        List<TalentNode<SoulShardTalent>> shardNodes = PlayerTalentsData.get(killer.getServerWorld()).getTalents(killer).getLearnedNodes(SoulShardTalent.class);

        int shardCount = ModConfigs.SOUL_SHARD.getRandomShards(killed.getType());

        for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
            if (influence.getType() == VaultAttributeInfluence.Type.SOUL_SHARD_DROPS && !influence.isMultiplicative()) {
                shardCount = (int) ((float) shardCount + influence.getValue());
            }
        }

            if (shardCount <= 0) {
                return false;
            } else {
                float additionalSoulShardChance = 0.0F;

                TalentNode node;
                for(Iterator<TalentNode<SoulShardTalent>> var15 = shardNodes.iterator(); var15.hasNext(); additionalSoulShardChance += ((SoulShardTalent)node.getTalent()).getAdditionalSoulShardChance()) {
                    node = var15.next();
                }

                float shShardCount = (float)shardCount * (1.0F + additionalSoulShardChance);
                shardCount = MathHelper.floor(shShardCount);
                float decimal = shShardCount - (float)shardCount;
                if (rand.nextFloat() < decimal) {
                    ++shardCount;
                }

                for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                    if (influence.getType() == VaultAttributeInfluence.Type.SOUL_SHARD_DROPS && influence.isMultiplicative()) {
                        shardCount = (int) ((float) shardCount * influence.getValue());
                    }
                }

                ItemStack shards = new ItemStack(ModItems.SOUL_SHARD, shardCount);
                ItemEntity itemEntity = new ItemEntity(world, killed.getPosX(), killed.getPosY(), killed.getPosZ(), shards);
                itemEntity.setDefaultPickupDelay();
                drops.add(itemEntity);
                return true;
            }
    }

    /**
     * @author a1qs
     * @reason remove shard pouch requirement
     */

    @Overwrite
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onEntityDrops(LivingDropsEvent event) {
        World world = event.getEntity().world;
        if (!world.isRemote() && world instanceof ServerWorld) {
            if (world.getDimensionKey() == Vault.VAULT_KEY) {
                Entity entity = event.getEntity();
                if (!InvokerEntityEvents.shouldDropDefaultInVault(entity)) {
                    BlockPos pos = entity.getPosition();
                    ServerWorld sWorld = (ServerWorld)world;
                    VaultRaid vault = VaultRaidData.get(sWorld).getAt(sWorld, pos);
                    if (vault == null) {
                        event.setCanceled(true);
                    } else {
                        DamageSource killingSrc = event.getSource();
                        if (!(entity instanceof VaultFighterEntity) && !(entity instanceof AggressiveCowEntity)) {
                            event.getDrops().clear();
                        }

                        if (vault.getActiveObjectives().stream().anyMatch(VaultObjective::preventsNormalMonsterDrops)) {
                            event.setCanceled(true);
                        } else {
                            boolean addedDrops = entity instanceof AggressiveCowEntity;
                            addedDrops |= InvokerEntityEvents.addScavengerDrops(world, entity, vault, event.getDrops());
                            addedDrops |= InvokerEntityEvents.addSubFighterDrops(world, entity, vault, event.getDrops());
                            Entity killerEntity = killingSrc.getTrueSource();
                            if (killerEntity instanceof EternalEntity) {
                                killerEntity = ((EternalEntity)killerEntity).getOwner().right().orElse(null);
                            }

                            if (killerEntity instanceof ServerPlayerEntity) {
                                ServerPlayerEntity killer = (ServerPlayerEntity)killerEntity;
                                if (vault.getActiveObjectives().stream().noneMatch((objective) ->
                                        objective.shouldPauseTimer(sWorld.getServer(), vault))) {
                                    addedDrops |= addShardDrops(world, entity, killer, vault, event.getDrops());
                                }
                            }
                            if (!addedDrops) {
                                event.setCanceled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}

