package com.a1qs.the_vault_extras.data.recipes.loot;

import mezz.jei.api.gui.IRecipeLayout;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LootTableRecipe {
    private final String lootTableName;
    private final List<ItemStack> possibleOutputs;
    private int currentPage;
    private IRecipeLayout recipeLayout;


    public LootTableRecipe(String lootTableName, List<ItemStack> possibleOutputs) {
        this.lootTableName = lootTableName;
        this.possibleOutputs = possibleOutputs;
        this.currentPage = 0;
    }

    public String getLootTableName() {
        return lootTableName;
    }

    public List<ItemStack> getPossibleOutputs() {
        return possibleOutputs;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages(int itemsPerPage) {
        return (int) Math.ceil((double) getPossibleOutputs().size() / itemsPerPage);
    }

    public List<List<ItemStack>> getPaginatedOutputs(int page, int itemsPerPage) {
        List<ItemStack> allOutputs = getPossibleOutputs();
        int startIndex = page * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, allOutputs.size());

        // Create the outer list to hold the inner lists (each containing one ItemStack)
        List<List<ItemStack>> paginatedOutputs = new ArrayList<>();

        // Loop through the selected outputs and wrap each in a List
        for (int i = startIndex; i < endIndex; i++) {
            List<ItemStack> wrappedOutput = Collections.singletonList(allOutputs.get(i));
            paginatedOutputs.add(wrappedOutput);
        }

        return paginatedOutputs;
    }

    public void setRecipeLayout(IRecipeLayout layout) {
        this.recipeLayout = layout;
    }

    public IRecipeLayout getRecipeLayout() {
        return this.recipeLayout;
    }
}
