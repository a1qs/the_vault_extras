package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.container.inventory.ShardPouchContainer;
import iskallia.vault.init.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(value = {PlayerInventory.class})
public class MixinPlayerInventory {

    @Final
    @Shadow
    public PlayerEntity player;

    public MixinPlayerInventory() {
    }

    @Inject(method = {"addItemStackToInventory"}, at = {@At("HEAD")}, cancellable = true)

    public void interceptItemAddition(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() == ModItems.SOUL_SHARD) {
            if (!(this.player.openContainer instanceof ShardPouchContainer)) {
                ItemStack pouchStack = ItemStack.EMPTY;
                if(CuriosApi.getCuriosHelper().findFirstCurio(player, ModItems.SHARD_POUCH).isPresent()) {
                    pouchStack = CuriosApi.getCuriosHelper().findFirstCurio(player, ModItems.SHARD_POUCH).get().getStack();
                }

                if (!pouchStack.isEmpty()) {
                    pouchStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((handler) -> {
                        ItemStack remainder = handler.insertItem(0, stack, false);
                        stack.setCount(remainder.getCount());
                        if (stack.isEmpty()) {
                            cir.setReturnValue(true);
                        }
                    });
                }
            }
        }
    }
}