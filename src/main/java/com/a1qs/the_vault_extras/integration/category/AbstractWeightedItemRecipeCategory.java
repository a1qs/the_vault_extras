package com.a1qs.the_vault_extras.integration.category;

import com.a1qs.the_vault_extras.data.recipes.loot.WeightedItemRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractWeightedItemRecipeCategory<T extends WeightedItemRecipe> implements IRecipeCategory<T> {
    private final IDrawable background;
    private final IDrawable icon;
    protected final IGuiHelper guiHelper;

    public AbstractWeightedItemRecipeCategory(IGuiHelper guiHelper, ResourceLocation texture, ItemStack iconItem) {
        this.guiHelper = guiHelper;
        this.background = guiHelper.createDrawable(texture, 0, 0, 162, 120);
        this.icon = guiHelper.createDrawableIngredient(iconItem);
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Nonnull
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(T recipe, @Nonnull IIngredients ingredients) {
        ingredients.setOutputs(VanillaTypes.ITEM, recipe.getOutputs());
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull T recipe, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        List<List<ItemStack>> outputLists = ingredients.getOutputs(VanillaTypes.ITEM);

        int index = 0;

        for (List<ItemStack> outputList : outputLists) {
            int indexPosX = 18 * (index % 9);
            int indexPosY = 18 * (index/9);
            itemStacks.init(index, false, indexPosX, indexPosY);
            itemStacks.set(index, outputList);
            index++;
        }

        itemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (!input) {
                tooltip.add(new StringTextComponent("Weight: " + recipe.getPercentageChance(ingredient)));
            }
        });
    }

    // Abstract methods to be implemented by subclasses
    @Nonnull
    @Override
    public abstract ResourceLocation getUid();

    @Nonnull
    @Override
    public abstract Class<? extends T> getRecipeClass();

    @Nonnull
    @Override
    public abstract String getTitle();
}
