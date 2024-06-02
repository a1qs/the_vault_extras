package com.a1qs.the_vault_extras.data.recipes;

import com.a1qs.the_vault_extras.VaultExtras;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public interface IVendorRecipes extends IRecipe<IInventory> {
    ResourceLocation TYPE_ID = new ResourceLocation(VaultExtras.MOD_ID, "vendor");

    @Override
    default IRecipeType<?> getType() {
        return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
    }

    @Override
    default boolean canFit(int width, int height) {
        return true;
    }

    @Override
    default boolean isDynamic() {
        return true;
    }
}
