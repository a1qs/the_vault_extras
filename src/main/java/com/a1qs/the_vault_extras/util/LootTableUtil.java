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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class LootTableUtil {
    public static final Map<ResourceLocation, JsonObject> LOOT_TABLES = new HashMap<>();

    public static JsonObject loadLootTable(ResourceLocation location) {
        try {
            // Log the loot table we're attempting to load
            System.out.println("Trying to load loot table: " + location);

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

            // Log the loaded JSON
            System.out.println("Loaded loot table: " + jsonObject);
            return jsonObject;
        } catch (IOException e) {
            // Print error if the file cannot be found or read
            System.err.println("Failed to load loot table: " + location);
            e.printStackTrace();
            return null;
        }
    }

    public static void loadLootTables() {

        //im so sorry but im NOT going to try and find all automatically

        ResourceLocation[] lootTableLocations = {
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/vaultchestcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/vaultchestrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/vaultchestepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/vaultchestomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/treasurecommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/godaltarcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/godaltarrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/godaltarepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/godaltaromega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/vaultchestepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/gildedepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/gildedomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/gildedcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/gildedrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/gildedepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/gildedomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/bosscrate"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/bosscratenew"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/arena"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl0/vault_fighter"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl0/cow"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl0/treasure_goblin"),

                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/vaultchestcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/vaultchestrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/vaultchestepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/vaultchestomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/treasurecommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/treasurerare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/treasureomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/godaltarcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/godaltarrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/godaltarepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/godaltaromega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/coopchest"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/coopchest"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/coopchest"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/coopchest"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/gildedcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/gildedrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/gildedepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/gildedomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/bosscrate"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/scavengecrate"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/arena"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl25/vault_fighter"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl25/cow"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl25/treasure_goblin"),

                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/vaultchestcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/vaultchestrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/vaultchestepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/vaultchestomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/treasurecommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/treasurerare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/treasureomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/godaltarcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/godaltarrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/godaltarepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/godaltaromega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/coopchest"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/gildedcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/gildedrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/gildedepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/gildedomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/bosscratenew"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/scavengecrate"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/championbox"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl50/arena"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl50/vault_fighter"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl50/cow"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl50/treasure_goblin"),

                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/vaultchestcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/vaultchestrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/vaultchestepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/vaultchestomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/treasurecommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/treasurerare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/treasureomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/godaltarcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/godaltarrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/godaltarepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/godaltaromega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/coopchest"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/gildedcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/gildedrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/gildedepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/gildedomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/bosscratenew"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/scavengecrate"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/championbox"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl75/arena"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl50/vault_fighter"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl50/cow"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl75/treasure_goblin"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/vaultchestcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/vaultchestrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/vaultchestepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/vaultchestomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/treasurecommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/treasurerare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/treasureomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/godaltarcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/godaltarrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/godaltarepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/godaltaromega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/coopchest"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/gildedcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/gildedrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/gildedepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/gildedomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/altar100"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/bosscratenew"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/scavengecrate"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/championbox"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl100/arena"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl100/vault_fighter"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl100/cow"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl100/treasure_goblin"),
                new ResourceLocation(Vault.MOD_ID, "chest/ancientbox"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/vaultchestcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/vaultchestrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/vaultchestepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/vaultchestomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/treasurecommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/treasurerare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/treasureomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/godaltarcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/godaltarrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/godaltarepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/godaltaromega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/coopchest"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/gildedcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/gildedrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/gildedepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/gildedomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/bosscratenew"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/scavengecrate"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/championbox"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl150/arena"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl150/vault_fighter"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl150/cow"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl150/treasure_goblin"),
                new ResourceLocation(Vault.MOD_ID, "chest/ancientbox"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/vaultchestcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/vaultchestrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/vaultchestepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/vaultchestomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/treasurecommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/treasurerare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/treasurerare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/treasureomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/godaltarcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/godaltarrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/godaltarepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/godaltaromega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/coopchest"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/coopchest"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/coopchest"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/coopchest"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/gildedcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/gildedrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/gildedepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/gildedomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/altar200"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/bosscratenew"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/scavengecrate"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/championbox"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl200/arena"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl200/vault_fighter"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl200/cow"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl200/treasure_goblin"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/vaultchestcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/vaultchestrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/vaultchestepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/vaultchestomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/treasurecommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/treasurerare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/treasureomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/godaltarcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/godaltarrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/godaltarepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/godaltaromega"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/coopchest"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/gildedcommon"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/gildedrare"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/gildedepic"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/gildedomega"),
                new ResourceLocation(Vault.MOD_ID, "chest/altar300"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/bosscratenew"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/scavengecrate"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/championbox"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl250/arena"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl250/vault_fighter"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl250/cow"),
                new ResourceLocation(Vault.MOD_ID, "entities/lvl250/treasure_goblin"),

                new ResourceLocation(Vault.MOD_ID, "chest/altar"),
                new ResourceLocation(Vault.MOD_ID, "chest/ancientbox"),
                new ResourceLocation(Vault.MOD_ID, "chest/altar0"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl25/championbox"),
        };

        for(ResourceLocation rl : lootTableLocations) {
            JsonObject lootTableJson = loadLootTable(rl);
            if(lootTableJson != null) {
                LOOT_TABLES.put(rl, lootTableJson);
            }
        }
    }

    public static List<LootTableRecipe> getLootTableRecipes() {
        List<LootTableRecipe> recipes = new ArrayList<>();

        for (Map.Entry<ResourceLocation, JsonObject> entry : LootTableUtil.LOOT_TABLES.entrySet()) {
            ResourceLocation lootTableLocation = entry.getKey();
            JsonObject lootTableJson = entry.getValue();
            System.out.println(entry.getValue());
            List<ItemStack> possibleOutputs = new ArrayList<>();
            Set<String> itemIds = new HashSet<>(); // Set to track item IDs
            JsonArray pools = lootTableJson.getAsJsonArray("pools");
            System.out.println("Pools: " + pools);
            for (JsonElement poolElement : pools) {
                JsonObject pool = poolElement.getAsJsonObject();
                System.out.println("pool: " + pool);
                JsonArray entries = pool.getAsJsonArray("entries");
                System.out.println("entries: " + entries);

                for (JsonElement entryElement : entries) {
                    JsonObject entry2 = entryElement.getAsJsonObject();
                    System.out.println("entry2: " + entry2);
                    if (entry2.get("type").getAsString().equals("minecraft:item")) {
                        System.out.println("Type equals minecraft:item");
                        String itemId = entry2.get("name").getAsString();
                        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
                        System.out.println(entry2.get("name").getAsString());

                        if (item != null && item != Items.AIR && !itemIds.contains(itemId)) {
                            itemIds.add(itemId); // Add the item ID to the Set
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
        return recipes;
    }
}
