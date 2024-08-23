package com.a1qs.the_vault_extras.integration;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.data.recipes.LootTableRecipe;
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
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class LootTableRecipeCategory implements IRecipeCategory<LootTableRecipe>  {
    public static final ResourceLocation UID = new ResourceLocation(VaultExtras.MOD_ID, "loottable");
    public static final ResourceLocation TEXTURE = new ResourceLocation(VaultExtras.MOD_ID, "textures/gui/loot_info_jei.png");
    private final IDrawable background;
    private final IDrawable icon;
    private IRecipeLayout currentRecipeLayout;

    public LootTableRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 162, 136);
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
    public void setRecipe(IRecipeLayout recipeLayout, @NotNull LootTableRecipe recipe, @NotNull IIngredients ingredients) {
        this.currentRecipeLayout = recipeLayout;
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

        int itemsPerPage = 54;

        List<List<ItemStack>> outputLists = recipe.getPaginatedOutputs(recipe.getCurrentPage(), itemsPerPage);


        int index = 0;
        for (List<ItemStack> outputList : outputLists) {
            for (ItemStack output : outputList) {
                int indexPosX = 18 * (index % 9);
                int indexPosY = 18 * (index/9);

                itemStacks.init(index, false, indexPosX, indexPosY);
                itemStacks.set(index, output);
                index++;
            }
        }
    }

    @Override
    public void draw(LootTableRecipe recipe, @NotNull MatrixStack matrixStack, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontRenderer = minecraft.fontRenderer;

        fontRenderer.drawStringWithShadow(matrixStack, "Loot table:", 0, 110, 0xFFffffff);
        fontRenderer.drawStringWithShadow(matrixStack, recipe.getLootTable().toString(), 0, 120, 0xFFffffff);



        int totalPages = recipe.getTotalPages(54);

        if (recipe.getCurrentPage() > 0) {
            drawButton(matrixStack, "<", 0, 130, 10, 10, mouseX, mouseY);
        }

        if (recipe.getCurrentPage() < totalPages - 1) {
            drawButton(matrixStack, ">", 150, 130, 10, 10, mouseX, mouseY);
        }
    }

    private void drawButton(MatrixStack matrixStack, String text, int x, int y, int width, int height, double mouseX, double mouseY) {
        boolean isHovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        int backgroundColor = isHovered ? 0xFFAAAAAA : 0xFF888888;
        AbstractGui.fill(matrixStack, x, y, x + width, y + height, backgroundColor);

        Minecraft.getInstance().fontRenderer.drawString(matrixStack, text, x + (width / 2) - (Minecraft.getInstance().fontRenderer.getStringWidth(text) / 2), y + (height / 2) - 4, 0xFF404040);
    }

    @Override
    public boolean handleClick(@NotNull LootTableRecipe recipe, double mouseX, double mouseY, int mouseButton) {
        int buttonX1 = 150;
        int buttonY = 130;
        int buttonWidth = 10;
        int buttonHeight = 10;
        int buttonX2 = 0;


        if (mouseButton == 0) { // Left click
            // Check if the click is within the button bounds
            if (mouseX >= buttonX1 && mouseX <= buttonX1 + buttonWidth && mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {
                // Play the click sound
                Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

                if (recipe.getCurrentPage() < recipe.getTotalPages(54) - 1) {
                    recipe.setCurrentPage(recipe.getCurrentPage() + 1);
                    updateLayout(currentRecipeLayout, recipe);
                }

                return true; // Indicate that the click was handled
            }

            if (mouseX >= buttonX2 && mouseX <= buttonX2 + buttonWidth && mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {
                // Play the click sound
                Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                if (recipe.getCurrentPage() > 0) {
                    recipe.setCurrentPage(recipe.getCurrentPage() - 1);
                    updateLayout(currentRecipeLayout, recipe);
                }

                return true; // Indicate that the click was handled
            }
        }

        return false;
    }

    public void updateLayout(IRecipeLayout recipeLayout, LootTableRecipe recipe) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

        int itemsPerPage = 54;
        List<List<ItemStack>> outputLists = recipe.getPaginatedOutputs(recipe.getCurrentPage(), itemsPerPage);

        int index = 0;
        for (int i = 0; i < itemsPerPage; i++) {
            int indexPosX = 18 * (i % 9);
            int indexPosY = 18 * (i / 9);

            itemStacks.init(i, false, indexPosX, indexPosY);

            if (index < outputLists.size() && !outputLists.get(index).isEmpty()) {
                itemStacks.set(i, outputLists.get(index));
                index++;
            } else {
                // Leave the slot empty by not setting anything
                itemStacks.set(i, Collections.emptyList());
            }
        }
    }

}
