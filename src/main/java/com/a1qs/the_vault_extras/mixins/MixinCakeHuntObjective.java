package com.a1qs.the_vault_extras.mixins;


import com.a1qs.the_vault_extras.events.ModSoundEvents;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.CakeHuntObjective;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = CakeHuntObjective.class, remap = false)
public class MixinCakeHuntObjective {

    @Inject(method = "expandVault", at = @At(value="HEAD"), cancellable = true)
    private void playSoundOnCakeClick(ServerWorld world, ServerPlayerEntity player, BlockPos cakePos, VaultRaid vault, CallbackInfo ci) {
        /*
        ITextComponent c0 = player.getDisplayName().deepCopy().mergeStyle(TextFormatting.LIGHT_PURPLE);
        ITextComponent c1 = (new StringTextComponent(" found a ")).mergeStyle(TextFormatting.GRAY);
        ITextComponent c2 = (new StringTextComponent("cake")).mergeStyle(TextFormatting.GREEN);
        ITextComponent ct = (new StringTextComponent("")).appendSibling(c0).appendSibling(c1).appendSibling(c2);
        vault.getPlayers().forEach((vPlayer) -> vPlayer.runIfPresent(world.getServer(), (sPlayer) -> sPlayer.sendMessage(ct, Util.DUMMY_UUID)));
         */

        vault.getPlayers().forEach((vPlayer) -> vPlayer.runIfPresent(world.getServer(), (sPlayer) -> sPlayer.getEntityWorld().playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 0.6F, 1.0F)));
    }
}
