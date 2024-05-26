package com.a1qs.the_vault_extras.mixins;

import com.a1qs.the_vault_extras.events.ModSoundEvents;
import iskallia.vault.init.ModParticles;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.chest.MobTrapEffect;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = MobTrapEffect.class, remap = false)

public class MobTrapEffectMixin {

    @Inject(method = "apply", at = @At(value="HEAD"), cancellable = true)
    public void apply(VaultRaid vault, VaultPlayer player, ServerWorld world, CallbackInfo ci) {
        ServerPlayerEntity playerEntity = (ServerPlayerEntity) world.getEntityByUuid(player.getPlayerId());
        assert playerEntity != null;

        playerEntity.world.playSound(playerEntity, playerEntity.getPosY(), playerEntity.getPosY(), playerEntity.getPosZ(), ModSoundEvents.MOB_TRAP_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
        playerEntity.playSound(ModSoundEvents.MOB_TRAP_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);

    }

}
