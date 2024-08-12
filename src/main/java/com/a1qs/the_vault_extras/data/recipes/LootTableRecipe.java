package com.a1qs.the_vault_extras.data.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class LootTableRecipe {
    private final ResourceLocation lootTable;
    private List<ItemStack> possibleOutputs;


    public LootTableRecipe(ResourceLocation lootTable, List<ItemStack> possibleOutputs) {
        this.lootTable = lootTable;
        this.possibleOutputs = possibleOutputs;
    }

    public ResourceLocation getLootTable() {
        return lootTable;
    }

    public List<ItemStack> getPossibleOutputs() {
        return possibleOutputs;
    }
}
