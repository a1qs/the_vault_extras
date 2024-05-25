package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.config.VaultGeneralConfig;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = VaultGeneralConfig.class, remap = false)
public class VaultGeneralConfigMixin {


    @Inject(method = "cancelItemInteraction", at = @At(value="RETURN"), cancellable = true)
    private static void cancelItemInteractionInfo(PlayerInteractEvent event, CallbackInfo ci){
        if(event.isCanceled()) {
            event.getPlayer().sendStatusMessage(new StringTextComponent(TextFormatting.RED +"This item is disabled in the Vault :)"), true);
        }
    }

    @Inject(method = "cancelBlockInteraction", at = @At(value="RETURN"), cancellable = true)
    private static void cancelBlockInteractionInfo(PlayerInteractEvent event, CallbackInfo ci){
        if(event.isCanceled()) {
            event.getPlayer().sendStatusMessage(new StringTextComponent(TextFormatting.RED +"This Block is disabled in the Vault :)"), true);
        }
    }
}
