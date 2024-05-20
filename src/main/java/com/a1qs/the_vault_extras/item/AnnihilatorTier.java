package com.a1qs.the_vault_extras.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

public class AnnihilatorTier implements IItemTier {

    public static final AnnihilatorTier INSTANCE = new AnnihilatorTier();

    public AnnihilatorTier() {
    }

    public int getMaxUses() {
        return -1;
    }

    public float getEfficiency() {
        return 2500F;
    }

    public float getAttackDamage() {
        return 20F;
    }

    public int getHarvestLevel() {
        return 25;
    }

    public int getEnchantability() {
        return 35;
    }

    public Ingredient getRepairMaterial() {
        return Ingredient.EMPTY;
    }
}
