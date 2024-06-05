package com.a1qs.the_vault_extras.item.entity;


import iskallia.vault.init.ModEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class AdvancedVaultPearlEntity extends EnderPearlEntity {
    Entity entity = this.getShooter();
    boolean explosive;
    boolean speed;


    public AdvancedVaultPearlEntity(World worldIn, LivingEntity throwerIn, boolean isExplosive, boolean appliesSpeed) {
        super(worldIn, throwerIn);
        explosive = isExplosive;
        speed = appliesSpeed;
    }

    protected void onImpact(RayTraceResult result) {
        RayTraceResult.Type raytraceresult$type = result.getType();
        if (raytraceresult$type == RayTraceResult.Type.ENTITY) {
            this.onEntityHit((EntityRayTraceResult)result);

        } else if (raytraceresult$type == RayTraceResult.Type.BLOCK) {
            this.func_230299_a_((BlockRayTraceResult) result);
            /*if(explosive) {
                this.world.createExplosion(null, this.getPosX(), this.getPosYHeight(0.0625D), this.getPosZ(), 4.0F, Explosion.Mode.NONE);
            }*/
        }
        if(explosive) {
            this.world.createExplosion(null, this.getPosX(), this.getPosYHeight(0.0625D), this.getPosZ(), 4.0F, Explosion.Mode.NONE);
        }

        for(int i = 0; i < 32; ++i) {
            this.world.addParticle(ParticleTypes.PORTAL, this.getPosX(), this.getPosY() + this.rand.nextDouble() * 2.0, this.getPosZ(), this.rand.nextGaussian(), 0.0, this.rand.nextGaussian());
        }

        if (!this.world.isRemote && !this.removed) {
            if (entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)entity;
                if(speed) {
                    EffectInstance activeSpeed = serverplayerentity.getActivePotionEffect(Effects.SPEED);
                    if(activeSpeed != null && serverplayerentity.getActivePotionEffect(ModEffects.VAULT_POWERUP) == null) {
                        serverplayerentity.addPotionEffect(new EffectInstance(Effects.SPEED, 300, activeSpeed.getAmplifier() + 2));
                        serverplayerentity.addPotionEffect(new EffectInstance(ModEffects.VAULT_POWERUP, 300, 0));
                    } else if (serverplayerentity.getActivePotionEffect(ModEffects.VAULT_POWERUP) == null){
                        serverplayerentity.addPotionEffect(new EffectInstance(Effects.SPEED, 300, 1));
                        serverplayerentity.addPotionEffect(new EffectInstance(ModEffects.VAULT_POWERUP, 300, 0));
                    }
                }
                if (serverplayerentity.connection.getNetworkManager().isChannelOpen() && serverplayerentity.world == this.world && !serverplayerentity.isSleeping()) {
                    if (entity.isPassenger()) {
                        entity.stopRiding();
                    }

                    entity.setPositionAndUpdate(this.getPosX(), this.getPosY(), this.getPosZ());
                    entity.fallDistance = 0.0F;
                }
            } else if (entity != null) {
                entity.setPositionAndUpdate(this.getPosX(), this.getPosY(), this.getPosZ());
                entity.fallDistance = 0.0F;
            }
            this.remove();
        }
    }
}
