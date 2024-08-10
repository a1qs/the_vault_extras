package com.a1qs.the_vault_extras.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class VaultExtrasConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_COOP_FAVOURS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_VAULT_DELETION;

    static {
        BUILDER.push("Configuration file for The Vault Extras");

        ENABLE_COOP_FAVOURS = BUILDER.comment("Enable Favour changes for Vaults in Coop Vaults")
                .define("ENABLE_COOP_FAVOURS", true);

        ENABLE_VAULT_DELETION = BUILDER.comment("EXPERIMENTAL: Enable Vaults being deleted upon leaving them & noone being inside the Vault dimension")
                .define("ENABLE_VAULT_DELETION", false);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
