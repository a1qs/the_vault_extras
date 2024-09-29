package com.a1qs.the_vault_extras.mixins.minecraft;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
public class MixinAnvilScreen {
    @Inject(method = "drawGuiContainerForegroundLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/inventory/AnvilScreen;fill(Lcom/mojang/blaze3d/matrix/MatrixStack;IIIII)V", shift = At.Shift.BEFORE), cancellable = true)
    public void removeTextRender(MatrixStack matrixStack, int x, int y, CallbackInfo ci) {
        ci.cancel();
    }
}
