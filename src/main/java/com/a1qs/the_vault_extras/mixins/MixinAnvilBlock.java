package com.a1qs.the_vault_extras.mixins;

import com.a1qs.the_vault_extras.init.ModBlocks;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={AnvilBlock.class})
public class MixinAnvilBlock {
    @Inject(method = "damage" , at = @At(value="HEAD") , cancellable = true)
    private static void damage(BlockState state, CallbackInfoReturnable<BlockState> cir) {
        if (state.matchesBlock(ModBlocks.VAULT_ANVIL.get())) {
            cir.setReturnValue(state);
        }
    }
}
