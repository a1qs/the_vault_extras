package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.block.entity.VaultChestTileEntity;
import iskallia.vault.config.VaultChestConfig;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.util.VaultRarity;
import iskallia.vault.util.data.RandomListAccess;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.chest.VaultChestEffect;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.modifier.ChestTrapModifier;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Iterator;
import java.util.Random;

@Mixin(value = VaultChestTileEntity.class, remap = false)
public class MixinVaultChestTileEntity {
    /**
     * @author a1qs
     * @reason why are you returning a massive block, why. why would you do this
     */
    @Overwrite
    private boolean shouldDoChestTrapEffect(VaultRaid vault, ServerWorld sWorld, ServerPlayerEntity player, BlockState thisState) {
        //be strong and ignore the IJ warnings
        VaultChestTileEntity thisInstance = (VaultChestTileEntity)((Object) this);
        return vault.getAllObjectives().stream().anyMatch(VaultObjective::preventsTrappedChests) ? false : vault.getPlayer(player.getUniqueID()).map((vPlayer) -> {
            int level = vPlayer.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
            boolean raffle = vault.getProperties().getBase(VaultRaid.IS_RAFFLE).orElse(false);
            VaultChestConfig config = null;
            if (thisState.getBlock() == ModBlocks.VAULT_CHEST) {
                config = ModConfigs.VAULT_CHEST;
            } else if (thisState.getBlock() == ModBlocks.VAULT_TREASURE_CHEST) {
                config = ModConfigs.VAULT_TREASURE_CHEST;
            } else if (thisState.getBlock() == ModBlocks.VAULT_ALTAR_CHEST) {
                config = ModConfigs.VAULT_ALTAR_CHEST;
            } else if (thisState.getBlock() == ModBlocks.VAULT_COOP_CHEST) {
                config = ModConfigs.VAULT_COOP_CHEST;
            } else if (thisState.getBlock() == ModBlocks.VAULT_BONUS_CHEST) {
                config = ModConfigs.VAULT_BONUS_CHEST;
            }

            if (config != null) {
                RandomListAccess<String> effectPool = config.getEffectPool(level, raffle);
                if (effectPool != null) {
                    ChestTrapModifier modifier;
                    for(Iterator var9 = vault.getActiveModifiersFor(PlayerFilter.of(new VaultPlayer[]{vPlayer}), ChestTrapModifier.class).iterator(); var9.hasNext(); effectPool = modifier.modifyWeightedList(config, (RandomListAccess)effectPool)) {
                        modifier = (ChestTrapModifier)var9.next();
                    }

                    VaultChestEffect effect = config.getEffectByName((String)((RandomListAccess)effectPool).getRandom(thisInstance.getWorld().getRandom()));
                    if (effect != null) {
                        effect.apply(vault, vPlayer, sWorld);
                        thisInstance.getWorld().setBlockState(thisInstance.getPos(), ModBlocks.VAULT_BEDROCK.getDefaultState());

                        if(effect.getName().equals("Mob Trap")) {
                            spawnParticles(thisInstance.getPos());
                        }
                        return true;
                    }
                }
            }

            return false;
        }).orElse(false);
    }

    @OnlyIn(Dist.CLIENT)
    private static void spawnParticles(BlockPos pos) {
        World world = Minecraft.getInstance().world;
        if (world != null) {
            ParticleManager mgr = Minecraft.getInstance().particles;
            Random random = world.getRandom();

            int i;
            Vector3d offset;
            int col;
            float f;
            float f1;
            float f2;
            for(i = 0; i < 40; ++i) {
                offset = new Vector3d(random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1), 0.0, random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1));
                Particle p = mgr.addParticle(ParticleTypes.POOF, (double)pos.getX() + 0.5 + offset.x, (double)pos.getY() + random.nextDouble() * 0.15000000596046448 + 0.25, (double)pos.getZ() + 0.5 + offset.z, offset.x / 2.0, random.nextDouble() * 0.1 + 0.1, offset.z / 2.0);
                if (random.nextBoolean()) {
                    col = 6842472;
                    f = (float)((col & 16711680) >> 16) / 255.0F;
                    f1 = (float)((col & '\uff00') >> 8) / 255.0F;
                    f2 = (float)(col & 255) / 255.0F;
                    if (p != null) {
                        p.setColor(f, f1, f2);
                    }
                } else {
                    col = 10724259;
                    f = (float)((col & 16711680) >> 16) / 255.0F;
                    f1 = (float)((col & '\uff00') >> 8) / 255.0F;
                    f2 = (float)(col & 255) / 255.0F;
                    if (p != null) {
                        p.setColor(f, f1, f2);
                    }
                }
            }

            for(i = 0; i < 30; ++i) {
                offset = new Vector3d(random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1), 0.0, random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1));
                SmokeParticle p = (SmokeParticle)mgr.addParticle(ParticleTypes.SMOKE, (double)pos.getX() + 0.5 + offset.x, (double)pos.up().getY() + random.nextDouble() * 0.15000000596046448, (double)pos.getZ() + 0.5 + offset.z, offset.x / 20.0, random.nextDouble() * 0.1 + 0.1, offset.z / 20.0);
                if (random.nextBoolean()) {
                    col = 4802889;
                    f = (float)((col & 16711680) >> 16) / 255.0F;
                    f1 = (float)((col & '\uff00') >> 8) / 255.0F;
                    f2 = (float)(col & 255) / 255.0F;
                    if (p != null) {
                        p.setColor(f, f1, f2);
                    }
                } else {
                    col = 8553090;
                    f = (float)((col & 16711680) >> 16) / 255.0F;
                    f1 = (float)((col & '\uff00') >> 8) / 255.0F;
                    f2 = (float)(col & 255) / 255.0F;
                    if (p != null) {
                        p.setColor(f, f1, f2);
                    }
                }
            }
        }
    }
}
