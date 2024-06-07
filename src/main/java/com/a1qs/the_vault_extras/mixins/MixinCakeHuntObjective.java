package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.CakeHuntObjective;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CakeHuntObjective.class, remap = false)
public class MixinCakeHuntObjective {

    @Inject(method = "expandVault", at = @At(value="HEAD"), cancellable = true)
    private void playSoundOnCakeClick(ServerWorld world, ServerPlayerEntity player, BlockPos cakePos, VaultRaid vault, CallbackInfo ci) {
        vault.getPlayers().forEach((vPlayer) -> vPlayer.runIfPresent(world.getServer(), (sPlayer) -> sPlayer.getEntityWorld().playSound(null, sPlayer.getPosX(), sPlayer.getPosY(), sPlayer.getPosZ(), SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 0.6F, 1.0F)));
    }
}
