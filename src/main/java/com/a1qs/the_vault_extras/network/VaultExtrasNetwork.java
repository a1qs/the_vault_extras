package com.a1qs.the_vault_extras.network;

import com.a1qs.the_vault_extras.VaultExtras;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class VaultExtrasNetwork {

    public static final String NETWORK_VERSION = "0.1.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry
            .newSimpleChannel(new ResourceLocation(VaultExtras.MOD_ID, "network"), () -> NETWORK_VERSION,
                    version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));

    public static void init() {
        CHANNEL.registerMessage(0, InputMessage.class, InputMessage::encode, InputMessage::decode, InputMessage::handle);
    }
}
