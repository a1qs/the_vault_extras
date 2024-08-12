package com.a1qs.the_vault_extras.integration;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.data.recipes.LootTableRecipe;
import com.a1qs.the_vault_extras.util.LootTableUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.init.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LootTableRecipeCategory implements IRecipeCategory<LootTableRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(VaultExtras.MOD_ID, "loottable");
    public static final ResourceLocation TEXTURE = new ResourceLocation(VaultExtras.MOD_ID, "textures/gui/loot_info_jei.png");
    private final IGuiHelper guiHelper;
    private IDrawable background;
    private final IDrawable icon;

    public LootTableRecipeCategory(IGuiHelper helper) {
        this.guiHelper = helper;
        this.background = helper.createDrawable(TEXTURE, 0, 0, 162, 162);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.VAULT_CHEST));
    }

    @Override
    public @NotNull ResourceLocation getUid() {
        return UID;
    }

    @Override
    public @NotNull Class<? extends LootTableRecipe> getRecipeClass() {
        return LootTableRecipe.class;
    }

    @Override
    public @NotNull String getTitle() {
        return new TranslationTextComponent("jei." + VaultExtras.MOD_ID + ".loottable").getString();
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(LootTableRecipe recipe, @NotNull IIngredients ingredients) {
        ingredients.setOutputs(VanillaTypes.ITEM, recipe.getPossibleOutputs());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, @NotNull LootTableRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

        // Get the outputs from ingredients and flatten the list
        List<List<ItemStack>> outputLists = ingredients.getOutputs(VanillaTypes.ITEM);

        int index = 0;
        for (List<ItemStack> outputList : outputLists) {
            for (ItemStack output : outputList) {
                int indexPosX = 18 * (index % 9);
                int indexPosY = 18 * (index/9);  //(index/9) * 18;

                itemStacks.init(index, false, indexPosX, indexPosY); // Positioning items with some spacing
                itemStacks.set(index, output);
                index++;
            }
        }
    }

    @Override
    public void draw(LootTableRecipe recipe, @NotNull MatrixStack matrixStack, double mouseX, double mouseY) {
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, "Loot table:", 0, 146, 0xFF404040);
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, recipe.getLootTable().toString(), 0, 156, 0xFF404040);
    }
}
