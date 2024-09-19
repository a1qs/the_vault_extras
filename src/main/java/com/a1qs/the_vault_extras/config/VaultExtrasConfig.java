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

        ENABLE_VAULT_DELETION = BUILDER.comment("Enable Vaults being deleted with noone running a Vault.")
                .define("ENABLE_VAULT_DELETION", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
