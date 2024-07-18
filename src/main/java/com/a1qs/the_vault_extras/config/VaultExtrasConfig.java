package com.a1qs.the_vault_extras.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class VaultExtrasConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_COOP_FAVOURS;

    static {
        BUILDER.push("Configuration file for The Vault Extras");

        ENABLE_COOP_FAVOURS = BUILDER.comment("EXPERIMENTAL: Enable Favour changes for Vaults in Coop Vaults")
                .define("ENABLE_COOP_FAVOURS", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
