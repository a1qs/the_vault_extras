package com.a1qs.the_vault_extras.mixins;

import com.a1qs.the_vault_extras.config.VaultExtrasConfig;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.VaultInfluenceHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collections;
import java.util.List;

@Mixin(value = VaultInfluenceHandler.class, remap = false)
public class MixinVaultInfluenceHandler {
    @Redirect(
            method = "initializeInfluences",
            at = @At(value = "INVOKE", target = "Liskallia/vault/world/vault/VaultRaid;getPlayers()Ljava/util/List;")
    )
    private static List<?> modifyArgument(VaultRaid instance) {
        if(VaultExtrasConfig.ENABLE_COOP_FAVOURS.get()) {
            return Collections.singletonList(instance.getPlayers().get(0));
        }
        return instance.getPlayers();
    }
}
