package com.a1qs.the_vault_extras.mixins;

import com.a1qs.the_vault_extras.util.ModSoundEvents;
import iskallia.vault.Vault;

import iskallia.vault.skill.ability.config.HunterConfig;
import iskallia.vault.skill.ability.effect.AbilityEffect;
import iskallia.vault.skill.ability.effect.HunterAbility;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = HunterAbility.class, remap = false)
public abstract class HunterAbilityMixin<C extends HunterConfig> extends AbilityEffect<C> {

    @Inject(method = "onAction(Liskallia/vault/skill/ability/config/HunterConfig;Lnet/minecraft/entity/player/ServerPlayerEntity;Z)Z", at = @At("HEAD"))
    private void hunterSoundEffect(C config, ServerPlayerEntity player, boolean active, CallbackInfoReturnable<Boolean> cir) {
        World world = player.getEntityWorld();
        if (player instanceof ServerPlayerEntity && world instanceof ServerWorld && world.getDimensionKey() == Vault.VAULT_KEY) {
            player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), ModSoundEvents.HUNTER_SFX.get(), SoundCategory.PLAYERS, 0.2F, 1.0F);
            player.playSound(ModSoundEvents.HUNTER_SFX.get(), SoundCategory.PLAYERS, 0.2F, 1.0F);
        }
    }
}
