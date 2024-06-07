package com.a1qs.the_vault_extras.mixins;


import iskallia.vault.Vault;
import iskallia.vault.config.ScavengerHuntConfig;
import iskallia.vault.entity.*;
import iskallia.vault.event.EntityEvents;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.BasicScavengerItem;
import iskallia.vault.item.ItemVaultRaffleSeal;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.type.SoulShardTalent;
import iskallia.vault.world.data.PlayerTalentsData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.influence.VaultAttributeInfluence;
import iskallia.vault.world.vault.logic.objective.ScavengerHuntObjective;
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

import java.util.*;

@Mixin(value = EntityEvents.class, remap = false)
public class MixinEntityEvents {

    private static final Random rand = new Random();

    /**
     * @author a1qs
     * @reason remove soul shard talent requirement
     */

    @Overwrite
    private static boolean addShardDrops(World world, Entity killed, ServerPlayerEntity killer, VaultRaid vault, Collection<ItemEntity> drops) {
        List<TalentNode<SoulShardTalent>> shardNodes = PlayerTalentsData.get(killer.getServerWorld()).getTalents(killer).getLearnedNodes(SoulShardTalent.class);

            int shardCount = ModConfigs.SOUL_SHARD.getRandomShards(killed.getType());
            Iterator var13 = vault.getInfluences().getInfluences(VaultAttributeInfluence.class).iterator();

            while(var13.hasNext()) {
                VaultAttributeInfluence influence = (VaultAttributeInfluence)var13.next();
                if (influence.getType() == VaultAttributeInfluence.Type.SOUL_SHARD_DROPS && !influence.isMultiplicative()) {
                    shardCount = (int)((float)shardCount + influence.getValue());
                }
            }

            if (shardCount <= 0) {
                return false;
            } else {
                float additionalSoulShardChance = 0.0F;

                TalentNode node;
                for(Iterator var15 = shardNodes.iterator(); var15.hasNext(); additionalSoulShardChance += ((SoulShardTalent)node.getTalent()).getAdditionalSoulShardChance()) {
                    node = (TalentNode)var15.next();
                }

                float shShardCount = (float)shardCount * (1.0F + additionalSoulShardChance);
                shardCount = MathHelper.floor(shShardCount);
                float decimal = shShardCount - (float)shardCount;
                if (rand.nextFloat() < decimal) {
                    ++shardCount;
                }

                Iterator var10 = vault.getInfluences().getInfluences(VaultAttributeInfluence.class).iterator();

                while(var10.hasNext()) {
                    VaultAttributeInfluence influence = (VaultAttributeInfluence)var10.next();
                    if (influence.getType() == VaultAttributeInfluence.Type.SOUL_SHARD_DROPS && influence.isMultiplicative()) {
                        shardCount = (int)((float)shardCount * influence.getValue());
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
     * @reason remove shard pouch requirement... sorry for this, I have no better idea...
     */

    @Overwrite
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onEntityDrops(LivingDropsEvent event) {
        World world = event.getEntity().world;
        if (!world.isRemote() && world instanceof ServerWorld) {
            if (world.getDimensionKey() == Vault.VAULT_KEY) {
                Entity entity = event.getEntity();
                if (!shouldDropDefaultInVault(entity)) {
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
                            addedDrops |= addScavengerDrops(world, entity, vault, event.getDrops());
                            addedDrops |= addSubFighterDrops(world, entity, vault, event.getDrops());
                            Entity killerEntity = killingSrc.getTrueSource();
                            if (killerEntity instanceof EternalEntity) {
                                killerEntity = (Entity)((EternalEntity)killerEntity).getOwner().right().orElse(null);
                            }

                            if (killerEntity instanceof ServerPlayerEntity) {
                                ServerPlayerEntity killer = (ServerPlayerEntity)killerEntity;
                                if (vault.getActiveObjectives().stream().noneMatch((objective) -> {
                                    return objective.shouldPauseTimer(sWorld.getServer(), vault);
                                })) {
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

    private static boolean shouldDropDefaultInVault(Entity entity) {
        return entity instanceof VaultGuardianEntity || entity instanceof TreasureGoblinEntity;
    }

    private static boolean addScavengerDrops(World world, Entity killed, VaultRaid vault, Collection<ItemEntity> drops) {
        Optional<ScavengerHuntObjective> objectiveOpt = vault.getActiveObjective(ScavengerHuntObjective.class);
        if (!objectiveOpt.isPresent()) {
            return false;
        } else {
            ScavengerHuntObjective objective = (ScavengerHuntObjective)objectiveOpt.get();
            List<ScavengerHuntConfig.ItemEntry> specialDrops = ModConfigs.SCAVENGER_HUNT.generateMobDropLoot(objective.getGenerationDropFilter(), killed.getType());
            return specialDrops.isEmpty() ? false : (Boolean)vault.getProperties().getBase(VaultRaid.IDENTIFIER).map((identifier) -> {
                specialDrops.forEach((entry) -> {
                    ItemStack stack = entry.createItemStack();
                    if (!stack.isEmpty()) {
                        BasicScavengerItem.setVaultIdentifier(stack, identifier);
                        ItemEntity itemEntity = new ItemEntity(world, killed.getPosX(), killed.getPosY(), killed.getPosZ(), stack);
                        itemEntity.setDefaultPickupDelay();
                        drops.add(itemEntity);
                    }
                });
                return true;
            }).orElse(false);
        }
    }

    private static boolean addSubFighterDrops(World world, Entity killed, VaultRaid vault, Collection<ItemEntity> drops) {
        if (!(killed instanceof VaultFighterEntity)) {
            return false;
        } else {
            int level = (Integer)vault.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
            float shardChance = ModConfigs.LOOT_TABLES.getForLevel(level).getSubFighterRaffleChance();
            if (rand.nextFloat() >= shardChance) {
                return false;
            } else {
                String name = killed.getPersistentData().getString("VaultPlayerName");
                if (name.isEmpty()) {
                    return false;
                } else {
                    ItemStack raffleSeal = new ItemStack(ModItems.CRYSTAL_SEAL_RAFFLE);
                    ItemVaultRaffleSeal.setPlayerName(raffleSeal, name);
                    ItemEntity itemEntity = new ItemEntity(world, killed.getPosX(), killed.getPosY(), killed.getPosZ(), raffleSeal);
                    itemEntity.setDefaultPickupDelay();
                    drops.add(itemEntity);
                    return true;
                }
            }
        }
    }
}

