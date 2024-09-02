package com.a1qs.the_vault_extras.events;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.config.VaultExtrasConfig;
import iskallia.vault.Vault;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mod.EventBusSubscriber
public class DeleteDimensionEvent {

    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        if(VaultExtrasConfig.ENABLE_VAULT_DELETION.get()) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            RegistryKey<World> dimensionKey = getDimensionToRemove();
            if(event.getFrom() == dimensionKey) {
                ServerWorld dimension = server.getWorld(dimensionKey);
                if (dimension != null && dimension.getPlayers().isEmpty()) {
                    deleteDimensionData(server, dimensionKey);
                }
            }
        }
    }

    private static void deleteDimensionData(MinecraftServer server, RegistryKey<World> dimensionKey){
        //todo: TEST THIS ON SERVERS OH GOD OH FUCK OH GOD AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
        Path dimensionsPath = server.func_240776_a_(new FolderName("dimensions"));
        Path vaultPath = dimensionsPath.resolve("the_vault").resolve("vault").normalize();
        Path poiPath = vaultPath.resolve("poi").normalize();
        Path regionPath = vaultPath.resolve("region").normalize();
        Path vaultRaidFilePath = server.func_240776_a_(new FolderName("data")).resolve("the_vault_VaultRaid.dat").normalize();

        try {
            deleteDirectoryRecursively(poiPath.toFile(), null);
            deleteDirectoryRecursively(regionPath.toFile(), null);
            if(Files.exists(vaultRaidFilePath)) {
                Files.deleteIfExists(vaultRaidFilePath);
                VaultExtras.LOGGER.info("Deleted VaultRaid file: {}", vaultRaidFilePath);
            } else {
                VaultExtras.LOGGER.warn("VaultRaid file not found: {}", vaultRaidFilePath);
            }
            VaultExtras.LOGGER.info("Deleted Dimension: {}", dimensionKey.getLocation());
        } catch (IOException e) {
            VaultExtras.LOGGER.error("Failed to delete dimension directory: {}", e.getMessage());
        }
    }

    private static void deleteDirectoryRecursively(File directory, Path keepPath) throws IOException {
        if(directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for(File file : files) {
                    if(!file.toPath().normalize().equals(keepPath)) {
                        deleteDirectoryRecursively(file, keepPath);
                    }
                }
            }
        }
        if(!directory.toPath().normalize().equals(keepPath)) {
            Files.delete(directory.toPath());
        }
    }

    private static RegistryKey<World> getDimensionToRemove() {
        return Vault.VAULT_KEY;
    }
}
