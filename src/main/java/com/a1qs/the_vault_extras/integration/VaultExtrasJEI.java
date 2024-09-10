package com.a1qs.the_vault_extras.integration;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.data.recipes.RecyclerRecipe;
import com.a1qs.the_vault_extras.data.recipes.VendorRecipe;
import com.a1qs.the_vault_extras.init.ModBlocks;
import com.a1qs.the_vault_extras.init.ModRecipeTypes;
import com.a1qs.the_vault_extras.integration.category.*;
import com.a1qs.the_vault_extras.util.LootTableUtil;
import iskallia.vault.init.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@JeiPlugin
public class VaultExtrasJEI implements IModPlugin {

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(VaultExtras.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new VendorRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new LootTableRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new MysteryEggItemRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new BoxRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
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


        registration.addRecipes(MysteryEggItemRecipeCategory.getRecipes(), MysteryEggItemRecipeCategory.UID);
        registration.addRecipes(BoxRecipeCategory.getRecipes(), BoxRecipeCategory.UID);

        registration.addRecipes(AnvilRecipeProvider.getAnvilRecipes(registration.getVanillaRecipeFactory()), VanillaRecipeCategoryUid.ANVIL);
        registration.addRecipes(LootTableUtil.getLootTableRecipes(), LootTableRecipeCategory.UID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.VAULT_RECYCLER.get()), RecyclerRecipeCategory.UID);

        registration.addRecipeCatalyst(new ItemStack(iskallia.vault.init.ModBlocks.VAULT_CHEST.asItem()), LootTableRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(iskallia.vault.init.ModBlocks.VAULT_BONUS_CHEST.asItem()), LootTableRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(iskallia.vault.init.ModBlocks.VAULT_COOP_CHEST.asItem()), LootTableRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(iskallia.vault.init.ModBlocks.VAULT_ALTAR_CHEST.asItem()), LootTableRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(iskallia.vault.init.ModBlocks.VAULT_TREASURE_CHEST.asItem()), LootTableRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(iskallia.vault.init.ModBlocks.VAULT_CRATE.asItem()), LootTableRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(iskallia.vault.init.ModBlocks.VAULT_CRATE_ARENA.asItem()), LootTableRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(iskallia.vault.init.ModBlocks.VAULT_CRATE_CAKE.asItem()), LootTableRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(iskallia.vault.init.ModBlocks.VAULT_CRATE_SCAVENGER.asItem()), LootTableRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(Blocks.BLACK_SHULKER_BOX.asItem()), LootTableRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(iskallia.vault.init.ModBlocks.VAULT_ALTAR.asItem()), LootTableRecipeCategory.UID);

        registration.addRecipeCatalyst(new ItemStack(ModItems.MYSTERY_EGG.asItem()), MysteryEggItemRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ModItems.MYSTERY_HOSTILE_EGG.asItem()), MysteryEggItemRecipeCategory.UID);

        registration.addRecipeCatalyst(new ItemStack(ModItems.MYSTERY_BOX.asItem()), BoxRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ModItems.PANDORAS_BOX.asItem()), BoxRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(com.a1qs.the_vault_extras.init.ModItems.MYSTERY_BOOK.get()), BoxRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(com.a1qs.the_vault_extras.init.ModItems.MYSTERY_RUNE.get()), BoxRecipeCategory.UID);
    }
}
