package com.a1qs.the_vault_extras.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.Config;
import iskallia.vault.config.entry.vending.ProductEntry;
import net.minecraft.item.Items;
import iskallia.vault.util.data.WeightedList;

public class MysteryRuneConfig extends Config {
    @Expose
    public WeightedList<ProductEntry> POOL = new WeightedList<>();

    public String getName() {
        return "mystery_rune";
    }

    protected void reset() {
        this.POOL.add(new ProductEntry(Items.APPLE, 8, null), 3);
    }
}
