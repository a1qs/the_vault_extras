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


        List<LootTableRecipe> recipes = new ArrayList<>();

        // Load and register your recipes
        for (Map.Entry<ResourceLocation, JsonObject> entry : LootTableUtil.LOOT_TABLES.entrySet()) {
            ResourceLocation lootTableLocation = entry.getKey();
            JsonObject lootTableJson = entry.getValue();
            System.out.println(entry.getValue());
            List<ItemStack> possibleOutputs = new ArrayList<>();
            JsonArray pools = lootTableJson.getAsJsonArray("pools");
            System.out.println("POols: " + pools);
            for (JsonElement poolElement : pools) {
                JsonObject pool = poolElement.getAsJsonObject();
                System.out.println("pool: " + pool);
                JsonArray entries = pool.getAsJsonArray("entries");
                System.out.println("entries: " + entries);

                for (JsonElement entryElement : entries) {
                    JsonObject entry2 = entryElement.getAsJsonObject();
                    System.out.println("entry2: " + entry2);
                    if (entry2.get("type").getAsString().equals("minecraft:item")) {
                        System.out.println("TYPE EXQUALS MC ITEM");
                        String itemId = entry2.get("name").getAsString();
                        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
                        System.out.println(entry2.get("name").getAsString());

                        if (item != null && item != Items.AIR) {
                            possibleOutputs.add(new ItemStack(item));
                        } else {
                            VaultExtras.LOGGER.warn("Skipping invalid or air item: {}", itemId);
                        }

                    } else {
                        System.out.println("type is not a minecraft:item");
                    }
                }
            }

            if (!possibleOutputs.isEmpty()) {
                LootTableRecipe recipe = new LootTableRecipe(lootTableLocation, possibleOutputs);
                recipes.add(recipe);
            }
        }

        // Debug log to check if recipes are being added
        System.out.println("Number of LootTableRecipes: " + recipes.size());

        // Register the recipes with JEI
        registration.addRecipes(recipes, LootTableRecipeCategory.UID);
    }
}
