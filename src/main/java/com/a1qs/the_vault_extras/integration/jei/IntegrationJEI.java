package com.a1qs.the_vault_extras.integration.jei;

import com.a1qs.the_vault_extras.VaultExtras;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class IntegrationJEI implements IModPlugin {
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory recipeFactory = registration.getVanillaRecipeFactory();

        registration.addRecipes(DummyRecipeProvider.getAnvilRecipes(recipeFactory), VanillaRecipeCategoryUid.ANVIL);
    }

    public ResourceLocation getPluginUid() {
        return VaultExtras.id("jei_integration");
    }
}
