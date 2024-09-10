package com.a1qs.the_vault_extras.integration.category;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.data.recipes.loot.WeightedItemRecipe;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.data.WeightedList;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MysteryEggItemRecipeCategory extends AbstractWeightedItemRecipeCategory<WeightedItemRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(VaultExtras.MOD_ID, "mystery_egg");

    public MysteryEggItemRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, new ResourceLocation(VaultExtras.MOD_ID, "textures/gui/weighted_item_jei.png"), new ItemStack(ModItems.MYSTERY_EGG));
    }
    @NotNull
    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @NotNull
    @Override
    public Class<? extends WeightedItemRecipe> getRecipeClass() {
        return WeightedItemRecipe.class;
    }

    @NotNull
    @Override
    public String getTitle() {
        return new TranslationTextComponent("jei." + VaultExtras.MOD_ID + ".mystery_egg").getString();
    }

    public static List<WeightedItemRecipe> getRecipes() {
        List<WeightedItemRecipe> recipes = new ArrayList<>();

        recipes.add(processEntries(ModConfigs.MYSTERY_EGG.POOL, new ItemStack(ModItems.MYSTERY_EGG)));
        recipes.add(processEntries(ModConfigs.MYSTERY_HOSTILE_EGG.POOL, new ItemStack(ModItems.MYSTERY_HOSTILE_EGG)));



        return recipes;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayout recipeLayout, @NotNull WeightedItemRecipe recipe, @NotNull IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        List<List<ItemStack>> outputLists = ingredients.getOutputs(VanillaTypes.ITEM);

        int index = 0;
        itemStacks.init(index, true, 72, 0);
        itemStacks.set(index, Collections.singletonList(recipe.getDisplayStack()));
        index++;
        for (List<ItemStack> outputList : outputLists) {
            int indexPosX = 18 * ((index-1) % 9);
            int indexPosY = 30 + 18 * ((index-1)/9);
            itemStacks.init(index, false, indexPosX, indexPosY);
            itemStacks.set(index, outputList);
            index++;
        }

        itemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (!input) {
                tooltip.add(new TranslationTextComponent("jeitooltip." + VaultExtras.MOD_ID + ".chance", recipe.getPercentageChance(ingredient)));
            }
        });

    }

    private static WeightedItemRecipe processEntries(List<WeightedList.Entry<iskallia.vault.config.entry.vending.ProductEntry>> pool, ItemStack stack) {
        List<ItemStack> items = new ArrayList<>();
        Map<ItemStack, Integer> weightMap = new HashMap<>();

        for (WeightedList.Entry<iskallia.vault.config.entry.vending.ProductEntry> entry : pool) {
            ItemStack outputItem = entry.value.generateItemStack();
            int weight = entry.weight;
            weightMap.put(outputItem, weight);
            items.add(outputItem);
        }
        return new WeightedItemRecipe(items, weightMap, stack);
    }
}
