package com.a1qs.the_vault_extras.util;

import com.a1qs.the_vault_extras.VaultExtras;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.storage.FolderName;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DimensionUtil {
    public static void deleteDimensionData(MinecraftServer server, RegistryKey<World> dimensionKey){
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
                VaultExtras.LOGGER.info("Deleted VaultRaid file: {}", vaultRaidFilePath.toAbsolutePath());
            } else {
                VaultExtras.LOGGER.warn("VaultRaid file not found: {}", vaultRaidFilePath.toAbsolutePath());
            }
            VaultExtras.LOGGER.info("Deleted Dimension: {}", dimensionKey.getLocation());
        } catch (IOException ignored) { }
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
            VaultExtras.LOGGER.info("Attempting to delete {}", directory.toPath());
        }
    }
}
