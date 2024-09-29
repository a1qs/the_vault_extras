package com.a1qs.the_vault_extras.mixins.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(method = "isMultiplayerEnabled", at = @At("HEAD"), cancellable = true)
    private void allowMultiplayerInDev(CallbackInfoReturnable<Boolean> cir) {
        if(!FMLEnvironment.production) {
            cir.setReturnValue(true);
        }
    }
}
