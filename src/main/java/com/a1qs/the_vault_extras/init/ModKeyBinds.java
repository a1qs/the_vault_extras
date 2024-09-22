package com.a1qs.the_vault_extras.init;

import com.a1qs.the_vault_extras.VaultExtras;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

public class ModKeyBinds {
    public static KeyBinding magnetToggle;
    public static KeyBinding veinminerHold;

    public static void register(final FMLClientSetupEvent event) {
        magnetToggle = create("magnet_toggle", GLFW.GLFW_KEY_KP_SUBTRACT);
        veinminerHold = create("veinminer_hold", GLFW.GLFW_KEY_UNKNOWN);

        ClientRegistry.registerKeyBinding(magnetToggle);
        ClientRegistry.registerKeyBinding(veinminerHold);
    }

    private static KeyBinding create (String name, int key) {
        return new KeyBinding("key." + VaultExtras.MOD_ID + "." + name, key, "key.category." + VaultExtras.MOD_ID);
    }

}
