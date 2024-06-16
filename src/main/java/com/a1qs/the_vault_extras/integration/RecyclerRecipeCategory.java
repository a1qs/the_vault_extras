package com.a1qs.the_vault_extras.integration;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.data.recipes.RecyclerRecipe;
import com.a1qs.the_vault_extras.init.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;


public class RecyclerRecipeCategory implements IRecipeCategory<RecyclerRecipe> {


    public static final ResourceLocation UID = new ResourceLocation(VaultExtras.MOD_ID, "recycle");
    public static final ResourceLocation TEXTURE = new ResourceLocation(VaultExtras.MOD_ID, "textures/gui/vault_recycler_gui.png");
    private final IDrawable background;
    private final IDrawable icon;

    public RecyclerRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 41);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.VAULT_RECYCLER.get()));
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



    /*
    @Override
    public void draw(RecyclerRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, TextFormatting.DARK_GRAY + "" + recipe.getRange(), 69, 26, 0xFFFFFF);
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, TextFormatting.WHITE + "" + recipe.getRange(), 68, 25, 0xFFFFFF);
    }
    */

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, RecyclerRecipe recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 50, 11);
        recipeLayout.getItemStacks().init(1, false, 105, 11);

        recipeLayout.getItemStacks().set(ingredients);
    }

    /*
    @Override
    public List<ITextComponent> getTooltipStrings(RecyclerRecipe recipe, double mouseX, double mouseY) {
        if (mouseX >= 70 && mouseX <= 105 && mouseY >= 25 && mouseY <= 32)
            return Collections.singletonList(new StringTextComponent("Cost Range"));
        return new ArrayList<>();
    }
    */
}
