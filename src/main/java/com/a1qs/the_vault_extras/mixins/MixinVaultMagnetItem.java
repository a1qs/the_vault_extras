package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.init.ModItems;
import iskallia.vault.item.VaultMagnetItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(value = VaultMagnetItem.class, remap = false)
public abstract class MixinVaultMagnetItem {
    @Inject(method = "onItemPickup", at = @At(value="HEAD"), cancellable = true)

    private static void magnetApplyDurabilityDamageInCurio(PlayerEvent.ItemPickupEvent event, CallbackInfo ci) {
        PlayerEntity player = event.getPlayer();
        PlayerInventory inventory = player.inventory;

        // check if there's a magnet in a curio slot
        if(CuriosApi.getCuriosHelper().findFirstCurio(inventory.player, ModItems.VAULT_MAGNET_OMEGA).isPresent()) {
            ItemStack stack = CuriosApi.getCuriosHelper().findFirstCurio(inventory.player, ModItems.VAULT_MAGNET_OMEGA).get().getStack();

            //damage the item if it's enabled upon pickup
            if(stack.getOrCreateTag().getBoolean("Enabled")) {
                stack.damageItem(1, player, (onBroken) -> {});
            }
        } else if(CuriosApi.getCuriosHelper().findFirstCurio(inventory.player, ModItems.VAULT_MAGNET_STRONG).isPresent()) {
            ItemStack stack = CuriosApi.getCuriosHelper().findFirstCurio(inventory.player, ModItems.VAULT_MAGNET_STRONG).get().getStack();
            if(stack.getOrCreateTag().getBoolean("Enabled")) {
                stack.damageItem(1, player, (onBroken) -> {});
            }
        } else if(CuriosApi.getCuriosHelper().findFirstCurio(inventory.player, ModItems.VAULT_MAGNET_WEAK).isPresent()) {
            ItemStack stack = CuriosApi.getCuriosHelper().findFirstCurio(inventory.player, ModItems.VAULT_MAGNET_WEAK).get().getStack();
            if(stack.getOrCreateTag().getBoolean("Enabled")) {
                stack.damageItem(1, player, (onBroken) -> {});
            }
        }
    }
}
