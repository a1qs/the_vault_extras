package com.a1qs.the_vault_extras.integration;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.data.recipes.loot.LootTableRecipe;
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

    public LootTableRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 162, 139);
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
    public void setRecipe(@NotNull IRecipeLayout recipeLayout, @NotNull LootTableRecipe recipe, @NotNull IIngredients ingredients) {
        recipe.setRecipeLayout(recipeLayout);

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

        // Draw text and other UI components relative to the current recipe
        fontRenderer.drawStringWithShadow(matrixStack, recipe.getLootTableName(), 0, 110, 0xFFffffff);

        int totalPages = recipe.getTotalPages(54);

        minecraft.fontRenderer.drawStringWithShadow(matrixStack, (recipe.getCurrentPage()+1) + " / " + totalPages , 70, 126, 0xFFffffff);

        // Only draw navigation buttons if more than one page exists
        if (recipe.getCurrentPage() > 0) {
            drawImageButton(matrixStack, 0, 120, 20, 20, mouseX, mouseY, "<");
        }

        if (recipe.getCurrentPage() < totalPages - 1) {
            drawImageButton(matrixStack, 143, 120, 20, 20, mouseX, mouseY, ">");
        }
    }

    private void drawImageButton(MatrixStack matrixStack, int x, int y, int width, int height, double mouseX, double mouseY, String direction) {
        Minecraft minecraft = Minecraft.getInstance();

        // Bind the texture
        minecraft.getTextureManager().bindTexture(TEXTURE);

        // Determine if the button is hovered
        boolean isHovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        // Calculate the texture coordinates for normal and hovered states
        int textureX = 162;
        int textureY = isHovered ? 20 : 0;  // Use different Y offset for hovered state

        AbstractGui.blit(matrixStack, x, y, width, height, textureX, textureY, 20, 20, 256, 256);
        minecraft.fontRenderer.drawStringWithShadow(matrixStack, direction , x+8, y+6, 0xFFffffff);
    }

    @Override
    public boolean handleClick(@NotNull LootTableRecipe recipe, double mouseX, double mouseY, int mouseButton) {
        int buttonX1 = 143;
        int buttonY = 120;
        int buttonWidth = 20;
        int buttonHeight = 20;
        int buttonX2 = 0;


        if (mouseButton == 0) { // Left click
            // Check if the click is within the button bounds
            if (mouseX >= buttonX1 && mouseX <= buttonX1 + buttonWidth && mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {
                Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

                // Check and set the next page if possible
                if (recipe.getCurrentPage() < recipe.getTotalPages(54) - 1) {
                    recipe.setCurrentPage(recipe.getCurrentPage() + 1);
                    updateLayout(recipe.getRecipeLayout(), recipe);
                }

                return true; // Indicate that the click was handled
            }

            if (mouseX >= buttonX2 && mouseX <= buttonX2 + buttonWidth && mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {
                Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

                // Check and set the previous page if possible
                if (recipe.getCurrentPage() > 0) {
                    recipe.setCurrentPage(recipe.getCurrentPage() - 1);
                    updateLayout(recipe.getRecipeLayout(), recipe);
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
