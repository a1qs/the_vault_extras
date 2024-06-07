package com.a1qs.the_vault_extras.integration;

import com.google.common.collect.Lists;
import iskallia.vault.Vault;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.crystal.VaultCrystalItem;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnvilRecipeProvider {
    public static List<Object> getAnvilRecipes(IVanillaRecipeFactory factory) {
        ArrayList recipeList = Lists.newArrayList();

        //Cake Objective Crystal
        ItemStack cakeCrystal = new ItemStack(ModItems.VAULT_CRYSTAL);
        CrystalData cakeData = VaultCrystalItem.getData(cakeCrystal);
        cakeData.setSelectedObjective(Vault.id("cake_hunt"));
        VaultCrystalItem.setRandomSeed(cakeCrystal);
        recipeList.add(factory.createAnvilRecipe(new ItemStack(ModItems.VAULT_CRYSTAL), Collections.singletonList(new ItemStack(com.a1qs.the_vault_extras.init.ModItems.CAKE_SEAL.get())), Collections.singletonList(cakeCrystal)));

        //Kill the Boss Objective Crystal
        ItemStack bossCrystal = new ItemStack(ModItems.VAULT_CRYSTAL);
        CrystalData bossData = VaultCrystalItem.getData(bossCrystal);
        bossData.setSelectedObjective(Vault.id("summon_and_kill_boss"));
        VaultCrystalItem.setRandomSeed(bossCrystal);
        recipeList.add(factory.createAnvilRecipe(new ItemStack(ModItems.VAULT_CRYSTAL), Collections.singletonList(new ItemStack(ModItems.CRYSTAL_SEAL_EXECUTIONER)), Collections.singletonList(bossCrystal)));

        //Scavenger Objective Crystal
        ItemStack scavengerCrystal = new ItemStack(ModItems.VAULT_CRYSTAL);
        CrystalData scavengerData = VaultCrystalItem.getData(scavengerCrystal);
        scavengerData.setSelectedObjective(Vault.id("scavenger_hunt"));
        VaultCrystalItem.setRandomSeed(scavengerCrystal);
        recipeList.add(factory.createAnvilRecipe(new ItemStack(ModItems.VAULT_CRYSTAL), Collections.singletonList(new ItemStack(ModItems.CRYSTAL_SEAL_HUNTER)), Collections.singletonList(scavengerCrystal)));

        //Architect Objective Crystal
        ItemStack architectCrystal = new ItemStack(ModItems.VAULT_CRYSTAL);
        CrystalData architectData = VaultCrystalItem.getData(architectCrystal);
        architectData.setSelectedObjective(Vault.id("architect"));
        VaultCrystalItem.setRandomSeed(architectCrystal);
        recipeList.add(factory.createAnvilRecipe(new ItemStack(ModItems.VAULT_CRYSTAL), Collections.singletonList(new ItemStack(ModItems.CRYSTAL_SEAL_ARCHITECT)), Collections.singletonList(architectCrystal)));

        //Ancients Objective Crystal
        ItemStack ancientCrystal = new ItemStack(ModItems.VAULT_CRYSTAL);
        CrystalData ancientData = VaultCrystalItem.getData(ancientCrystal);
        ancientData.setSelectedObjective(Vault.id("ancients"));
        VaultCrystalItem.setRandomSeed(ancientCrystal);
        recipeList.add(factory.createAnvilRecipe(new ItemStack(ModItems.VAULT_CRYSTAL), Collections.singletonList(new ItemStack(ModItems.CRYSTAL_SEAL_ANCIENTS)), Collections.singletonList(ancientCrystal)));

        //Raid Objective Crystal
        ItemStack raidCrystal = new ItemStack(ModItems.VAULT_CRYSTAL);
        CrystalData raidData = VaultCrystalItem.getData(raidCrystal);
        raidData.setSelectedObjective(Vault.id("raid_challenge"));
        VaultCrystalItem.setRandomSeed(raidCrystal);
        recipeList.add(factory.createAnvilRecipe(new ItemStack(ModItems.VAULT_CRYSTAL), Collections.singletonList(new ItemStack(ModItems.CRYSTAL_SEAL_RAID)), Collections.singletonList(raidCrystal)));

        //Raffle Objective Crystal
        ItemStack raffleCrystal = new ItemStack(ModItems.VAULT_CRYSTAL);
        CrystalData raffleData = VaultCrystalItem.getData(raffleCrystal);
        raffleData.setSelectedObjective(Vault.id("summon_and_kill_boss"));
        raffleData.setType(CrystalData.Type.RAFFLE);
        raffleData.setPlayerBossName("???");
        VaultCrystalItem.setRandomSeed(raffleCrystal);
        recipeList.add(factory.createAnvilRecipe(new ItemStack(ModItems.VAULT_CRYSTAL), Collections.singletonList(new ItemStack(ModItems.CRYSTAL_SEAL_RAFFLE)), Collections.singletonList(raffleCrystal)));

        return recipeList;
    }
}
