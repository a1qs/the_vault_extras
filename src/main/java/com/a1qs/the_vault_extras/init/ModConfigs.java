package com.a1qs.the_vault_extras.init;

import com.a1qs.the_vault_extras.config.MysteryBookConfig;
import com.a1qs.the_vault_extras.config.MysteryRuneConfig;

public class ModConfigs {
    public static void register() {
        MYSTERY_RUNE = new MysteryRuneConfig().readConfig();
        MYSTERY_BOOK = new MysteryBookConfig().readConfig();
    }

    public static MysteryRuneConfig MYSTERY_RUNE;
    public static MysteryBookConfig MYSTERY_BOOK;

    public static void registerCompressionConfigs() {

    }
}
