package com.a1qs.the_vault_extras.mixins.vault;

import com.a1qs.the_vault_extras.init.ModConfigs;
import com.mojang.brigadier.context.CommandContext;
import iskallia.vault.command.ReloadConfigsCommand;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ReloadConfigsCommand.class}, remap = false)
public class MixinReloadConfigsCommand {
    @Inject(method = "reloadConfigs", at = @At(value="HEAD"))
    private void reloadConfigsExtras(CommandContext<CommandSource> context, CallbackInfoReturnable<Integer> cir) {
        try {
            ModConfigs.register();
        } catch (Exception var3) {
            var3.printStackTrace();
            throw var3;
        }
    }
}
