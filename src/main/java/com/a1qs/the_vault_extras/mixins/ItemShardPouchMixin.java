package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.init.ModItems;
import iskallia.vault.item.ItemShardPouch;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import top.theillusivec4.curios.api.CuriosApi;

import static iskallia.vault.item.ItemShardPouch.getContainedStack;
import static iskallia.vault.item.ItemShardPouch.setContainedStack;

@Mixin(value = ItemShardPouch.class, remap = false)
public class ItemShardPouchMixin {

    /**
     * @author iwolfking // a1qs
     * @reason add curios support for returning total shards in playerInventory
     */

    @Overwrite
    public static int getShardCount(PlayerInventory playerInventory) {
        int shards = 0;

        if(CuriosApi.getCuriosHelper().findFirstCurio(playerInventory.player, ModItems.SHARD_POUCH).isPresent()) {
            ItemStack pouchStack = CuriosApi.getCuriosHelper().findFirstCurio(playerInventory.player,ModItems.SHARD_POUCH).get().getStack();
            shards += getContainedStack(pouchStack).getCount();
        }

        for(int slot = 0; slot < playerInventory.getSizeInventory(); ++slot) {
            ItemStack stack = playerInventory.getStackInSlot(slot);
            if (stack.getItem() instanceof ItemShardPouch) {
                shards += getContainedStack(stack).getCount();
            } else if (stack.getItem() == ModItems.SOUL_SHARD) {
                shards += stack.getCount();
            }
        }
        return shards;
    }
    /**
     * @author iwolfking // a1qs
     * @reason add curios support for reducing total shards in playerInventory
     */
    @Overwrite
    public static boolean reduceShardAmount(PlayerInventory playerInventory, int count, boolean simulate) {
        if(CuriosApi.getCuriosHelper().findFirstCurio(playerInventory.player, ModItems.SHARD_POUCH).isPresent()) {
            ItemStack pouchStack = CuriosApi.getCuriosHelper().findFirstCurio(playerInventory.player, ModItems.SHARD_POUCH).get().getStack();
            ItemStack shardStack = getContainedStack(pouchStack);
            int toReduce = Math.min(count, shardStack.getCount());
            if(!simulate) {
                shardStack.setCount(shardStack.getCount() - toReduce);
                setContainedStack(pouchStack, shardStack);
            }

            count -= toReduce;
        }
        for(int slot = 0; slot < playerInventory.getSizeInventory(); ++slot) {
            ItemStack stack = playerInventory.getStackInSlot(slot);
            if (stack.getItem() instanceof ItemShardPouch) {
                ItemStack shardStack = getContainedStack(stack);
                int toReduce = Math.min(count, shardStack.getCount());
                if (!simulate) {
                    shardStack.setCount(shardStack.getCount() - toReduce);
                    setContainedStack(stack, shardStack);
                }

                count -= toReduce;
            } else if (stack.getItem() == ModItems.SOUL_SHARD) {
                int toReduce = Math.min(count, stack.getCount());
                if (!simulate) {
                    stack.shrink(toReduce);
                    playerInventory.setInventorySlotContents(slot, stack);
                }
                count -= toReduce;
            }

            if (count <= 0) {
                return true;
            }
        }
        return false;
    }
}
