package com.a1qs.the_vault_extras.events;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.init.ModKeyBinds;
import com.a1qs.the_vault_extras.network.MagnetMessage;
import com.a1qs.the_vault_extras.network.VaultExtrasNetwork;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.AbilityKeyMessage;
import iskallia.vault.network.message.AbilityQuickselectMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VaultExtras.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class InputEvents {

    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world == null) return;
        onInput(mc, event.getKey(), event.getAction());
    }

    @SubscribeEvent
    public static void onMousePress(InputEvent.MouseInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world == null) return;
        onInput(mc, event.getButton(), event.getAction());
    }

    private static void onInput(Minecraft mc, int key, int action) {
        if(mc.currentScreen == null && ModKeyBinds.magnetToggle.isPressed()) {
            VaultExtrasNetwork.CHANNEL.sendToServer(new MagnetMessage(key));
        }

        if (mc.currentScreen == null && key != -1) {
            if (ModKeyBinds.veinminerHold.getKey().getKeyCode() == key) {
                if (action == 0) {

                    ModNetwork.CHANNEL.sendToServer(new AbilityKeyMessage(true, false, false, false));
                } else if (action == 1) {
                    ModNetwork.CHANNEL.sendToServer(new AbilityQuickselectMessage("Vein Miner"));
                    ModNetwork.CHANNEL.sendToServer(new AbilityKeyMessage(false, true, false, false));
                }
            }
        }
    }
}
