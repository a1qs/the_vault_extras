package com.a1qs.the_vault_extras.integration;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.data.recipes.LootTableRecipe;
import com.a1qs.the_vault_extras.data.recipes.RecyclerRecipe;
import com.a1qs.the_vault_extras.data.recipes.VendorRecipe;
import com.a1qs.the_vault_extras.init.ModRecipeTypes;
import com.a1qs.the_vault_extras.util.LootTableUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@JeiPlugin
public class VaultExtrasJEI implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(VaultExtras.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new VendorRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new LootTableRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new RecyclerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().world).getRecipeManager();

        registration.addRecipes(rm.getRecipesForType(ModRecipeTypes.VENDOR_RECIPE).stream()
                .filter(r -> r instanceof VendorRecipe).collect(Collectors.toList()),
            VendorRecipeCategory.UID);

        registration.addRecipes(rm.getRecipesForType(ModRecipeTypes.RECYCLER_RECIPE).stream()
                        .filter(r -> r instanceof RecyclerRecipe).collect(Collectors.toList()),
                RecyclerRecipeCategory.UID);

        registration.addRecipes(AnvilRecipeProvider.getAnvilRecipes(registration.getVanillaRecipeFactory()), VanillaRecipeCategoryUid.ANVIL);
        registration.addRecipes(LootTableUtil.getLootTableRecipes(), LootTableRecipeCategory.UID);
    }
}
