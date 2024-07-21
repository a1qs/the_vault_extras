package com.a1qs.the_vault_extras.init;

import iskallia.vault.mixin.MixinBooleanValue;
import net.minecraft.world.GameRules;

public class ModGameRules {
    public static GameRules.RuleKey<GameRules.BooleanValue> VAULT_CASUAL_MODE;

    public static void initialize() {
        VAULT_CASUAL_MODE = GameRules.register("vaultCasualMode", GameRules.Category.MISC, booleanRule(false));
    }

    public static GameRules.RuleType<GameRules.BooleanValue> booleanRule(boolean defaultValue) {
        return MixinBooleanValue.create(defaultValue);
    }
}
