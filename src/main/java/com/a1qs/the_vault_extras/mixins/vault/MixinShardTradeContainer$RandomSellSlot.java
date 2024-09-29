package com.a1qs.the_vault_extras.mixins.vault;

import iskallia.vault.init.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.LogicalSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "iskallia.vault.container.inventory.ShardTradeContainer$RandomSellSlot", remap = false)
public class MixinShardTradeContainer$RandomSellSlot {
    @Inject(method = "modifyTakenStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/container/Container;detectAndSendChanges()V", shift = At.Shift.AFTER))
    private void t(PlayerEntity player, ItemStack taken, LogicalSide side, boolean simulate, CallbackInfoReturnable<ItemStack> cir) {
        player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.VAULT_CHEST_RARE_OPEN, SoundCategory.BLOCKS, 1.0F, 0.5F);
    }
}
