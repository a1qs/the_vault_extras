package com.a1qs.the_vault_extras.mixins.invokers;

import iskallia.vault.item.paxel.enhancement.PaxelEnhancement;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = PaxelEnhancements.class, remap = false)
public interface InvokerPaxelEnhancements {
    @Invoker("register")
    static <T extends PaxelEnhancement> T invokeRegister(String name, T enhancement) {
        throw new AssertionError();
    }
}
