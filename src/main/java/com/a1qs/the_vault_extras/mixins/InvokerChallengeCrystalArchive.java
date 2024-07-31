package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.world.data.generated.ChallengeCrystalArchive;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = ChallengeCrystalArchive.class, remap = false)
public interface InvokerChallengeCrystalArchive {
    @Invoker("baseData")
    static CrystalData invokeBaseData() {
        throw new AssertionError();
    }

    @Invoker("make")
    static ItemStack invokeMake(CrystalData data) {
        throw new AssertionError();
    }

}
