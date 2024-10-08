package com.a1qs.the_vault_extras.mixins.vault;

import com.a1qs.the_vault_extras.events.ModSounds;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.chest.MobTrapEffect;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = MobTrapEffect.class, remap = false)

public class MixinMobTrapEffects {

    @Inject(method = "apply", at = @At(value="HEAD"), cancellable = true)
    public void apply(VaultRaid vault, VaultPlayer player, ServerWorld world, CallbackInfo ci) {
        ServerPlayerEntity playerEntity = (ServerPlayerEntity) world.getEntityByUuid(player.getPlayerId());
        assert playerEntity != null;

        playerEntity.world.playSound(playerEntity, playerEntity.getPosY(), playerEntity.getPosY(), playerEntity.getPosZ(), ModSounds.MOB_TRAP_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
        playerEntity.playSound(ModSounds.MOB_TRAP_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);

    }

}
