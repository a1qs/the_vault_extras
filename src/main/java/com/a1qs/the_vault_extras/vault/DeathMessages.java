package com.a1qs.the_vault_extras.vault;


import iskallia.vault.world.data.PlayerFavourData;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;


public class DeathMessages {
    private static final Random rand = new Random();
    private static final Map<PlayerFavourData.VaultGodType, List<String>> deathMessagesMap = new HashMap<>();

    public DeathMessages () {
    }

    public static IFormattableTextComponent deathMessage(PlayerFavourData.VaultGodType god) {
        String message = getDeathMessages(god);
        IFormattableTextComponent vgName = (new StringTextComponent(god.getName())).mergeStyle(god.getChatColor());
        vgName.modifyStyle((style) -> style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, getHoverChatComponent(god))));

        return new StringTextComponent("[VG] ").appendSibling(vgName)
                .mergeStyle(TextFormatting.DARK_PURPLE)
                .appendSibling((new StringTextComponent(": ")).mergeStyle(TextFormatting.WHITE))
                .appendSibling(new StringTextComponent(message));


    }


    private static String getDeathMessages(PlayerFavourData.VaultGodType god) {
        List<String> messages = deathMessagesMap.get(god);
        return messages.get(rand.nextInt(messages.size()));
    }

    private static ITextComponent getHoverChatComponent(PlayerFavourData.VaultGodType god) {
        return (new StringTextComponent("[Vault God] ")).mergeStyle(TextFormatting.WHITE).appendSibling((new StringTextComponent(god.getName() + ", " + god.getTitle())).mergeStyle(god.getChatColor()));
    }

    static {
        List<String> benevolentMessages = new ArrayList<>();
        benevolentMessages.add("Be reborn past your misgivings");
        benevolentMessages.add("Nature may feast upon you");
        benevolentMessages.add("Your life has been led astray - rife with ugliness without a trace of beauty");
        benevolentMessages.add("Nature; cleansing itself of your taint");
        deathMessagesMap.put(PlayerFavourData.VaultGodType.BENEVOLENT, benevolentMessages);

        List<String> omniscientMessages = new ArrayList<>();
        omniscientMessages.add("Greed consumes you; wealth remains an unreachable dream.");
        omniscientMessages.add("The void absorbs your essence, unaltered.");
        omniscientMessages.add("Opulence, turns to ash in the moment of your demise.");
        omniscientMessages.add("In your final moments, no fortune can guide you.");
        deathMessagesMap.put(PlayerFavourData.VaultGodType.OMNISCIENT, omniscientMessages);

        List<String> timekeeperMessages = new ArrayList<>();
        timekeeperMessages.add("Your death shall bear no consequence upon the continuum of time.");
        timekeeperMessages.add("Your riches scatter into the relentless flow of time.");
        timekeeperMessages.add("Lost in the relentless flow of time, you drown.");
        timekeeperMessages.add("You vanish; into the immutable emptiness of time.");
        deathMessagesMap.put(PlayerFavourData.VaultGodType.TIMEKEEPER, timekeeperMessages);

        List<String> malevolenceMessages = new ArrayList<>();
        malevolenceMessages.add("Your death returns you to your rightful place");
        malevolenceMessages.add("Perish under the weight of my relentless strength");
        malevolenceMessages.add("Your weakness repulses me");
        malevolenceMessages.add("Choke on your suffering, mortal");
        deathMessagesMap.put(PlayerFavourData.VaultGodType.MALEVOLENCE, malevolenceMessages);
    }
}
