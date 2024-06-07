package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.world.vault.gen.VaultRoomNames;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(value = VaultRoomNames.class, remap = false)
public class MixinVaultRoomNames {

    @Inject(method = "getName", at = @At(value="HEAD"), cancellable = true)
    @Nullable
    private static void nameAdditions(String filterKey, CallbackInfoReturnable<ITextComponent> cir) {
        switch (filterKey) {
            case "douwsky":
                cir.setReturnValue(new StringTextComponent("Douwsky").mergeStyle(TextFormatting.DARK_PURPLE));
                break;
            case "library":
                cir.setReturnValue(new StringTextComponent("Library").mergeStyle(TextFormatting.YELLOW));
                break;
            case "colosseum":
                cir.setReturnValue(new StringTextComponent("Colosseum").mergeStyle(TextFormatting.WHITE));
                break;
            case "contest_toyroom":
                cir.setReturnValue(new StringTextComponent("Contest: Toyroom").mergeStyle(TextFormatting.DARK_AQUA));
                break;
            case "contest_squid":
                cir.setReturnValue(new StringTextComponent("Contest: Squid").mergeStyle(TextFormatting.DARK_AQUA));
                break;
        }
    }
}
