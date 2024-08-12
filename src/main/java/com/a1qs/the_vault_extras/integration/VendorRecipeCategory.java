package com.a1qs.the_vault_extras.integration;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.data.recipes.VendorRecipe;
import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.init.ModItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class VendorRecipeCategory implements IRecipeCategory<VendorRecipe> {


    public static final ResourceLocation UID = new ResourceLocation(VaultExtras.MOD_ID, "vendor");
    public static final ResourceLocation TEXTURE = new ResourceLocation(VaultExtras.MOD_ID, "textures/gui/vendor_trade_jei.png");
    private final IDrawable background;
    private final IDrawable icon;

    public VendorRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 41);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModItems.ETCHING));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends VendorRecipe> getRecipeClass() {
        return VendorRecipe.class;
    }

    @Override
    public String getTitle() {
        return new TranslationTextComponent("jei." + VaultExtras.MOD_ID + ".vendor").getString();
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
    public void setIngredients(VendorRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());

    }

    @Override
    public void draw(VendorRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, TextFormatting.DARK_GRAY + "" + recipe.getRange(), 69, 26, 0xFFFFFF);
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, TextFormatting.WHITE + "" + recipe.getRange(), 68, 25, 0xFFFFFF);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, VendorRecipe recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 50, 11);
        recipeLayout.getItemStacks().init(1, false, 105, 11);

        recipeLayout.getItemStacks().set(ingredients);
    }

    @Override
    public List<ITextComponent> getTooltipStrings(VendorRecipe recipe, double mouseX, double mouseY) {
        if (mouseX >= 70 && mouseX <= 105 && mouseY >= 25 && mouseY <= 32)
            return Collections.singletonList(new StringTextComponent("Cost Range"));
        return new ArrayList<>();
    }
}
