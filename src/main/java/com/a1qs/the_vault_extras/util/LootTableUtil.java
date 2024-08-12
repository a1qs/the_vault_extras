package com.a1qs.the_vault_extras.util;

import com.google.gson.*;
import iskallia.vault.Vault;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

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
        //ResourceLocation lootTableLocation = new ResourceLocation(Vault.MOD_ID, "chest/lvl0/bosscrate");
        ResourceLocation[] lootTableLocations = {
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/arena"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/bosscrate"),
                new ResourceLocation(Vault.MOD_ID, "chest/lvl0/bosscratenew")
        };

        for(ResourceLocation rl : lootTableLocations) {
            JsonObject lootTableJson = loadLootTable(rl);
            if(lootTableJson != null) {
                LOOT_TABLES.put(rl, lootTableJson);
            }
        }
//        JsonObject lootTableJson = loadLootTable(lootTableLocation);
//
//        if (lootTableJson != null) {
//            LOOT_TABLES.put(lootTableLocation, lootTableJson);
//        }
    }
}
