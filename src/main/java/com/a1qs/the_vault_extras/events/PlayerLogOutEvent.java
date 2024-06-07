package com.a1qs.the_vault_extras.events;

import com.a1qs.the_vault_extras.VaultExtras;
import iskallia.vault.world.data.VaultPartyData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = VaultExtras.MOD_ID)
public class PlayerLogOutEvent {

    @SubscribeEvent
    public static void removeFromPartyUponLogout(PlayerLoggedOutEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        VaultPartyData data = VaultPartyData.get(player.getServerWorld());

        Optional<VaultPartyData.Party> party = data.getParty(player.getUniqueID());
        if (party.isPresent()) {
            if ((party.get()).remove(player.getUniqueID())) {
                (party.get()).getMembers().forEach(uuid -> {
                     ServerPlayerEntity player2 = player.getServerWorld().getServer().getPlayerList().getPlayerByUUID(uuid);
                     if (player2 != null) {
                        player2.sendMessage(new StringTextComponent(player.getName().getString() + " has left the party.").mergeStyle(TextFormatting.GREEN), player.getUniqueID());
                     }
                });
                VaultPartyData.broadcastPartyData(player.getServerWorld());
            }
        }
    }
}
