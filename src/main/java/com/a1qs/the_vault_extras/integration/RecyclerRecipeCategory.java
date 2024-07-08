package com.a1qs.the_vault_extras.integration;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.data.recipes.RecyclerRecipe;
import com.a1qs.the_vault_extras.init.ModBlocks;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class RecyclerRecipeCategory implements IRecipeCategory<RecyclerRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(VaultExtras.MOD_ID, "recycle");
    public static final ResourceLocation TEXTURE = new ResourceLocation(VaultExtras.MOD_ID, "textures/gui/vault_recycler_jei.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public RecyclerRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 175, 41);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.VAULT_RECYCLER.get()));
        this.cachedArrows = CacheBuilder.newBuilder()
            .maximumSize(25)
            .build(new CacheLoader<Integer, IDrawableAnimated>() {
                @Override
                public IDrawableAnimated load(Integer smeltTime) {
                    return helper.drawableBuilder(TEXTURE, 176, 0, 24, 17)
                            .buildAnimated(smeltTime, IDrawableAnimated.StartDirection.LEFT, false);
                }
            });
    }


    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends RecyclerRecipe> getRecipeClass() {
        return RecyclerRecipe.class;
    }

    @Override
    public String getTitle() {
        return new TranslationTextComponent("jei." + VaultExtras.MOD_ID + ".recycle").getString();
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(RecyclerRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void draw(RecyclerRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, TextFormatting.DARK_GRAY + "" + (int)(recipe.getExtraChance()*100) + "%",98, 29, 0xFFFFFF);
        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(matrixStack, 52, 12);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, RecyclerRecipe recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 31, 11);
        recipeLayout.getItemStacks().init(1, false, 79, 11);
        recipeLayout.getItemStacks().init(2, false, 97, 11);
        recipeLayout.getItemStacks().set(2, recipe.getExtraOutput());

        recipeLayout.getItemStacks().set(ingredients);
    }

    private IDrawableAnimated getArrow(RecyclerRecipe recipe) {
        int smeltTime = recipe.getSmeltTime();
        return this.cachedArrows.getUnchecked(smeltTime);
    }
}
