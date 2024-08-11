package com.a1qs.the_vault_extras.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {

    public static final ItemGroup VAULT_EXTRAS = new ItemGroup("vaultExtrasTab")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(ModItems.VAULT_ANNIHILATOR.get());
        }
    };
}
