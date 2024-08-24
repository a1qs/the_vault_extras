package com.a1qs.the_vault_extras.util;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.data.recipes.loot.LootTableRecipe;
import com.google.gson.*;
import iskallia.vault.Vault;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.*;
import java.util.*;

public class LootTableUtil {
    public static final Map<String, JsonObject> LOOT_TABLES = new HashMap<>();

    public static JsonObject loadLootTable(ResourceLocation location) {
        try {
            IResourceManager resourceManager;
            if (ServerLifecycleHooks.getCurrentServer() != null) {
                // Server-side resource manager
                resourceManager = ServerLifecycleHooks.getCurrentServer().getDataPackRegistries().getResourceManager();
            } else {
                // Client-side resource manager
                resourceManager = Minecraft.getInstance().getResourceManager();
            }

            // Adjusted ResourceLocation path to match loot table format
            ResourceLocation lootTableLocation = new ResourceLocation(location.getNamespace(), "loot_tables/" + location.getPath() + ".json");

            // Attempt to load the resource
            IResource resource = resourceManager.getResource(lootTableLocation);
            InputStream inputStream = resource.getInputStream();

            // Parse the JSON
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(new InputStreamReader(inputStream));
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            return jsonObject;
        } catch (IOException e) {
            // Print error if the file cannot be found or read
            System.err.println("Failed to load loot table: " + location);
            e.printStackTrace();
            return null;
        }
    }

    public static void loadLootTables() {

        //LVL0
        LOOT_TABLES.put("LVL 0 Vault Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/vaultchestcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/vaultchestrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/vaultchestepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/vaultchestomega")));


        LOOT_TABLES.put("LVL 0 Gilded Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/gildedomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/gildedcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/gildedrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/gildedepic")));

        LOOT_TABLES.put("LVL 0 God Altars", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/godaltarcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/godaltarrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/godaltarepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/godaltaromega")));

        LOOT_TABLES.put("LVL 0 Treasure Chest", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl0/treasurecommon")));
        LOOT_TABLES.put("LVL 0 Boss Crate", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl0/bosscrate")));
        LOOT_TABLES.put("LVL 0 Scavenger Crate", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl0/bosscratenew")));
        LOOT_TABLES.put("LVL 0 Vault Fighter", loadLootTable(new ResourceLocation(Vault.MOD_ID, "entities/lvl0/vault_fighter")));
        LOOT_TABLES.put("LVL 0 Vault Altar", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/altar0")));

        //LVL25
        LOOT_TABLES.put("LVL 25 Vault Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/vaultchestcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/vaultchestrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/vaultchestepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/vaultchestomega")));

        LOOT_TABLES.put("LVL 25 Treasure Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/treasurecommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/treasurerare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/treasureomega")));

        LOOT_TABLES.put("LVL 25 God Altars", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/godaltarcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/godaltarrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/godaltarepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/godaltaromega")));

        LOOT_TABLES.put("LVL 25 Gilded Chest", mergeLootTables(new ResourceLocation(Vault.MOD_ID, "chest/lvl25/gildedcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/gildedrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/gildedepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/gildedomega")));

        LOOT_TABLES.put("LVL 25 Coop Chest", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl25/coopchest")));
        LOOT_TABLES.put("LVL 25 Boss Crate", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl25/bosscrate")));
        LOOT_TABLES.put("LVL 25 Scavenger Crate", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl25/scavengecrate")));
        LOOT_TABLES.put("LVL 25 Vault Fighter", loadLootTable(new ResourceLocation(Vault.MOD_ID, "entities/lvl25/vault_fighter")));
        LOOT_TABLES.put("LVL 25 Champion Crate", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl25/championbox")));
        LOOT_TABLES.put("LVL 25 Cow", loadLootTable(new ResourceLocation(Vault.MOD_ID, "entities/lvl25/cow")));

        //LVL50
        LOOT_TABLES.put("LVL 50 Vault Chest", mergeLootTables(new ResourceLocation(Vault.MOD_ID, "chest/lvl50/vaultchestcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/vaultchestrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/vaultchestepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/vaultchestomega")));

        LOOT_TABLES.put("LVL 50 Treasure Chest", mergeLootTables(new ResourceLocation(Vault.MOD_ID, "chest/lvl50/treasurecommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/treasurerare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/treasureomega")));

        LOOT_TABLES.put("LVL 50 God Altars", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/godaltarcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/godaltarrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/godaltarepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/godaltaromega")));

        LOOT_TABLES.put("LVL 50 Gilded Chests", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/gildedcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/gildedrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/gildedepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/gildedomega")));

        LOOT_TABLES.put("LVL 50 Boss Crate", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl50/bosscratenew")));
        LOOT_TABLES.put("LVL 50 Scavenger Crate", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl50/scavengecrate")));
        LOOT_TABLES.put("LVL 50 Champion Box", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl50/championbox")));
        LOOT_TABLES.put("LVL 50 Coop Chest", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl50/coopchest")));
        LOOT_TABLES.put("LVL 50 Vault Fighter", loadLootTable(new ResourceLocation(Vault.MOD_ID, "entities/lvl50/vault_fighter")));
        LOOT_TABLES.put("LVL 50 Cow", loadLootTable(new ResourceLocation(Vault.MOD_ID, "entities/lvl50/cow")));

        //LVL75
        LOOT_TABLES.put("LVL 75 Vault Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/vaultchestcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/vaultchestrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/vaultchestepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/vaultchestomega")));

        LOOT_TABLES.put("LVL 75 Treasure Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/treasurecommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/treasurerare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/treasureomega")));

        LOOT_TABLES.put("LVL 75 God Altars", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/godaltarcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/godaltarrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/godaltarepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/godaltaromega")));

        LOOT_TABLES.put("LVL 75 Gilded Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/gildedcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/gildedrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/gildedepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/gildedomega")));

        LOOT_TABLES.put("LVL 75 Coop Chest", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl75/coopchest")));
        LOOT_TABLES.put("LVL 75 Boss Crate", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl75/bosscratenew")));
        LOOT_TABLES.put("LVL 75 Scavenger Crate", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl75/scavengecrate")));

        //LVl100
        LOOT_TABLES.put("LVL 100 Vault Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/vaultchestcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/vaultchestrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/vaultchestepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/vaultchestomega")));

        LOOT_TABLES.put("LVL 100 Treasure Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/treasurecommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/treasurerare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/treasureomega")));

        LOOT_TABLES.put("LVL 100 God Altars", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/godaltarcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/godaltarrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/godaltarepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/godaltaromega")));

        LOOT_TABLES.put("LVL 100 Gilded Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/gildedcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/gildedrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/gildedepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/gildedomega")));

        LOOT_TABLES.put("LVL 100 Coop Chest", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl100/coopchest")));
        LOOT_TABLES.put("LVL 100 Vault Altar", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/altar100")));
        LOOT_TABLES.put("LVL 100 Boss Crate", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl100/bosscratenew")));
        LOOT_TABLES.put("LVL 100 Scavenger Crate", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl100/scavengecrate")));
        LOOT_TABLES.put("LVL 100 Champion Box", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl100/championbox")));
        LOOT_TABLES.put("LVL 100 Vault Fighter", loadLootTable(new ResourceLocation(Vault.MOD_ID, "entities/lvl100/vault_fighter")));
        LOOT_TABLES.put("LVL 100 Cow", loadLootTable(new ResourceLocation(Vault.MOD_ID, "entities/lvl100/cow")));
        LOOT_TABLES.put("LVL 100 Treasure Goblin", loadLootTable(new ResourceLocation(Vault.MOD_ID, "entities/lvl100/treasure_goblin")));

        //LVL150
        LOOT_TABLES.put("LVL 150 Vault Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/vaultchestcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/vaultchestrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/vaultchestepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/vaultchestomega")));

        LOOT_TABLES.put("LVL 150 Treasure Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/treasurecommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/treasurerare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/treasureomega")));

        LOOT_TABLES.put("LVL 150 God Altar", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/godaltarcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/godaltarrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/godaltarepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/godaltaromega")));

        LOOT_TABLES.put("LVL 150 Gilded Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/gildedcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/gildedrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/gildedepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/gildedomega")));

        LOOT_TABLES.put("LVL 150 Coop Chest", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl150/coopchest")));
        LOOT_TABLES.put("LVL 150 Boss Crate", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl150/bosscratenew")));
        LOOT_TABLES.put("LVL 150 Scavenger Crate", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl150/scavengecrate")));
        LOOT_TABLES.put("LVL 150 Champion Box", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl150/championbox")));
        LOOT_TABLES.put("LVL 150 Cow", loadLootTable(new ResourceLocation(Vault.MOD_ID, "entities/lvl150/cow")));
        LOOT_TABLES.put("LVL 150 Treasure Goblin", loadLootTable(new ResourceLocation(Vault.MOD_ID, "entities/lvl150/treasure_goblin")));

        // LVL200
        LOOT_TABLES.put("LVL 200 Vault Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/vaultchestcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/vaultchestrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/vaultchestepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/vaultchestomega")));

        LOOT_TABLES.put("LVL 200 Treasure Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/treasurecommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/treasurerare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/treasureomega")));

        LOOT_TABLES.put("LVL 200 God Altars", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/godaltarcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/godaltarrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/godaltarepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/godaltaromega")));

        // LVL 200 Gilded Chest
        LOOT_TABLES.put("LVL 200 Gilded Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/gildedcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/gildedrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/gildedepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/gildedomega")));

        LOOT_TABLES.put("LVL 200 Vault Altar", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/altar200")));
        LOOT_TABLES.put("LVL 200 Boss Crate", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl200/bosscratenew")));
        LOOT_TABLES.put("LVL 200 Scavenger Crate", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl200/scavengecrate")));
        LOOT_TABLES.put("LVL 200 Champion Box", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl200/championbox")));
        LOOT_TABLES.put("LVL 200 Vault Fighter", loadLootTable(new ResourceLocation(Vault.MOD_ID, "entities/lvl200/vault_fighter")));
        LOOT_TABLES.put("LVL 200 Cow", loadLootTable(new ResourceLocation(Vault.MOD_ID, "entities/lvl200/cow")));
        LOOT_TABLES.put("LVL 200 Treasure Goblin", loadLootTable(new ResourceLocation(Vault.MOD_ID, "entities/lvl200/treasure_goblin")));

        //LVL250
        LOOT_TABLES.put("LVL 250 Vault Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/vaultchestcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/vaultchestrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/vaultchestepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/vaultchestomega")));
        
        LOOT_TABLES.put("LVL 250 Treasure Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/treasurecommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/treasurerare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/treasureomega")));
        
        LOOT_TABLES.put("LVL 250 God Altars", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/godaltarcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/godaltarrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/godaltarepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/godaltaromega")));
        
        LOOT_TABLES.put("LVL 250 Gilded Chest", mergeLootTables(
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/gildedcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/gildedrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/gildedepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/gildedomega")));
        
        
        LOOT_TABLES.put("LVL 250 Boss Crate", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl250/bosscratenew")));
        LOOT_TABLES.put("LVL 250 Scavenger Crate", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl250/scavengecrate")));
        LOOT_TABLES.put("LVL 250 Champion Box", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/lvl250/championbox")));
        LOOT_TABLES.put("LVL 250 Vault Fighter", loadLootTable(new ResourceLocation(Vault.MOD_ID, "entities/lvl250/vault_fighter")));
        LOOT_TABLES.put("LVL 250 Cow", loadLootTable(new ResourceLocation(Vault.MOD_ID, "entities/lvl250/cow")));
        LOOT_TABLES.put("LVL 250 Treasure Goblin", loadLootTable(new ResourceLocation(Vault.MOD_ID, "entities/lvl250/treasure_goblin")));
        
        LOOT_TABLES.put("LVL 300 Vault Altar", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/altar300")));
        LOOT_TABLES.put("Ancient Box", loadLootTable(new ResourceLocation(Vault.MOD_ID, "chest/ancientbox")));

    }

    public static List<LootTableRecipe> getLootTableRecipes() {
        List<LootTableRecipe> recipes = new ArrayList<>();

        for (Map.Entry<String, JsonObject> entry : LootTableUtil.LOOT_TABLES.entrySet()) {
            String lootTableName = entry.getKey();
            JsonObject lootTableJson = entry.getValue();
            List<ItemStack> possibleOutputs = new ArrayList<>();
            Set<String> itemIds = new HashSet<>(); // Set to track item IDs

            // Check if the loot table has a "pools" field and ensure it's a JsonArray
            if (lootTableJson.has("pools") && lootTableJson.get("pools").isJsonArray()) {
                JsonArray pools = lootTableJson.getAsJsonArray("pools");

                for (JsonElement poolElement : pools) {
                    JsonObject pool = poolElement.getAsJsonObject();
                    JsonArray entries = pool.getAsJsonArray("entries");

                    for (JsonElement entryElement : entries) {
                        JsonObject entry2 = entryElement.getAsJsonObject();
                        if (entry2.get("type").getAsString().equals("minecraft:item")) {
                            String itemId = entry2.get("name").getAsString();
                            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));

                            if (item != null && item != Items.AIR && !itemIds.contains(itemId)) {
                                itemIds.add(itemId); // Add the item ID to the Set
                                possibleOutputs.add(new ItemStack(item));
                            } else {
                                VaultExtras.LOGGER.warn("Skipping invalid, duplicate or air item: {}", itemId);
                            }
                        }
                    }
                }

                if (!possibleOutputs.isEmpty()) {
                    LootTableRecipe recipe = new LootTableRecipe(lootTableName, possibleOutputs);
                    recipes.add(recipe);
                }
            } else {
                VaultExtras.LOGGER.warn("Loot table '{}' does not contain valid pools", lootTableName);
            }
        }
        return recipes;
    }


    //todo: fucks up with certain pools
    public static JsonObject mergeLootTables(ResourceLocation... rl) {
        JsonObject mergedLootTable = new JsonObject();
        JsonArray mergedPools = new JsonArray();  // To hold merged pools
        JsonArray mergedEntries = new JsonArray();  // To hold merged entries for non-pool loot tables

        for (ResourceLocation location : rl) {
            JsonObject lootTable = loadLootTable(location);
            if (lootTable != null) {
                // Handle merging pools
                JsonArray pools = lootTable.getAsJsonArray("pools");
                if (pools != null) {
                    for (JsonElement poolElement : pools) {
                        if (poolElement != null && poolElement.isJsonObject()) {
                            mergedPools.add(poolElement.getAsJsonObject());
                        } else {
                            VaultExtras.LOGGER.error("Invalid pool element in loot table: {}", location);
                        }
                    }
                }

                // Handle merging entries (for non-pool loot tables)
                JsonArray entries = lootTable.getAsJsonArray("entries");
                if (entries != null) {
                    for (JsonElement entryElement : entries) {
                        if (entryElement != null && entryElement.isJsonObject()) {
                            mergedEntries.add(entryElement.getAsJsonObject());
                        } else {
                            VaultExtras.LOGGER.error("Invalid entry element in loot table: {}", location);
                        }
                    }
                }

                // Log if neither pools nor entries are found
                if (pools == null && entries == null) {
                    VaultExtras.LOGGER.error("Neither pools nor entries found in loot table: {}", location);
                }

            } else {
                VaultExtras.LOGGER.error("Loot table could not be loaded: {}", location);
            }
        }

        // Add the merged pools to the final loot table JSON if pools exist
        if (mergedPools.size() > 0) {
            mergedLootTable.add("pools", mergedPools);
        }

        // Add the merged entries to the final loot table JSON if entries exist and no pools are present
        if (mergedEntries.size() > 0 && mergedPools.size() == 0) {
            mergedLootTable.add("entries", mergedEntries);
        }

        // Return the merged loot table
        return mergedLootTable;
    }
}
