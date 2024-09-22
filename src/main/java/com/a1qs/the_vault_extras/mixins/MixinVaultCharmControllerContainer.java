package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.container.VaultCharmControllerContainer;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = VaultCharmControllerContainer.class, remap = false)
public class MixinVaultCharmControllerContainer {

    // Fixes the scrolling in the Vault Charm Controller, ported to VH1.16.5
    // https://github.com/BONNePlayground/VaultHunterBetterJunkController/blob/master/src/main/java/lv/id/bonne/vaulthunters/junkcontroller/mixin/VaultCharmControllerContainerMixin.java

    @Shadow(remap = false)
    private int currentStart;
    @Final
    @Shadow(remap = false)
    private int inventorySize;
    /**
     * The original scrollTo method does not update currentStart to correct position.
     * It could be `inject` with before `shiftInventoryIndexes` but it adds +9 and -9 depending
     * on scroll direction, that is not necessary as start is calculated based on scroll position.
     * @param scroll new scroll position
     * @param ci callback
     */
    @Inject(method = "scrollTo",
            at = @At(value = "INVOKE", target = "Liskallia/vault/container/VaultCharmControllerContainer;updateVisibleItems()V"),
            remap = false)
    public void injectProperStartValue(float scroll, CallbackInfo ci) {
        // Make current start to a correct value
        this.currentStart = Math.round(MathHelper.clamp(scroll * (this.inventorySize - 54), 0, Math.max(0, this.inventorySize - 54)));
    }



}
