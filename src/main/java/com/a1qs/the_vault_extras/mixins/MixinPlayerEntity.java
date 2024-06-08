package com.a1qs.the_vault_extras.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ToolItem;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PlayerEntity.class)
public class MixinPlayerEntity
{
    @Redirect(method = "getDigSpeed", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;onGround:Z"))
    private boolean redirectOnGround(PlayerEntity player, BlockState state, BlockPos pos) {
        if (player.getHeldItemMainhand().getItem() instanceof ToolItem)
        {
            return true;
        }

        return player.isOnGround();
    }
}