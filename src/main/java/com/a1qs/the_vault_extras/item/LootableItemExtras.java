package com.a1qs.the_vault_extras.item;

import iskallia.vault.item.ItemRelicBoosterPack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class LootableItemExtras extends Item {
    private final Supplier<ItemStack> supplier;

    public LootableItemExtras(Item.Properties properties, Supplier<ItemStack> supplier) {
        super(properties);
        this.supplier = supplier;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!world.isRemote) {
            ItemStack heldStack = player.getHeldItem(hand);
            ItemRelicBoosterPack.successEffects(world, player.getPositionVec());
            ItemStack randomLoot = this.supplier.get();

            while (randomLoot.getCount() > 0) {
                int amount = Math.min(randomLoot.getCount(), randomLoot.getMaxStackSize());
                ItemStack copy = randomLoot.copy();
                copy.setCount(amount);
                randomLoot.shrink(amount);
                player.dropItem(copy, false, false);
            }

            heldStack.shrink(1);
        }

        return super.onItemRightClick(world, player, hand);
    }

}
