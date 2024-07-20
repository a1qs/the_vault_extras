package com.a1qs.the_vault_extras.events;


import iskallia.vault.Vault;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class PlayerTabNameEvent {

    private static final Set<UUID> IN_VAULT = new HashSet<>();

    @SubscribeEvent
    public static void onTabListNameFormat(PlayerEvent.@NotNull TabListNameFormat event) {
        PlayerEntity eventPlayer = event.getPlayer();
        if (eventPlayer instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) eventPlayer;
            int vaultLevel = PlayerVaultStatsData.get(player.getServerWorld()).getVaultStats(player).getVaultLevel();

            // Set TextComponents that should be displayed alongside the player name
            StringTextComponent display = new StringTextComponent("");
            StringTextComponent level = new StringTextComponent(TextFormatting.YELLOW + String.valueOf(vaultLevel));
            StringTextComponent space = new StringTextComponent(" ");
            TextComponent playerName = (TextComponent) event.getPlayer().getName();
            display.appendSibling(level).appendSibling(space).appendSibling(playerName);

            // If the player is inside a Vault dimension, append (Vault) to the playername
            if (IN_VAULT.contains(player.getUniqueID())) {
                display.appendSibling(new StringTextComponent(TextFormatting.DARK_GRAY +" (Vault)"));
            }

            event.setDisplayName(display);
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity eventPlayer = event.player;
        World world = eventPlayer.getEntityWorld();
        if (eventPlayer instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) eventPlayer;
            boolean updated;

            // Continue every 5 seconds, not every tick
            if (event.phase != TickEvent.Phase.END && serverPlayerEntity.server.getTickCounter() % 100L != 0L) {
                return;
            }

            // If the player is inside a Vault dimension, add the UUID to a HashSet
            // returns true for the boolean if the player is not part of the HashSet
            if (world.getDimensionKey() == Vault.VAULT_KEY) {
                updated = IN_VAULT.add(serverPlayerEntity.getUniqueID());
            } else {
                updated = IN_VAULT.remove(serverPlayerEntity.getUniqueID());
            }

            //Update the Tablist
            if (updated) {
                serverPlayerEntity.refreshTabListName();
            }
        }
    }
}
