package com.a1qs.the_vault_extras.mixins;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.RepairContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(RepairContainer.class)
public abstract class MixinRepairContainer {





    @Redirect(method = "func_230301_a_", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExperienceLevel(I)V"))
    protected void redirectAddExperienceLevel(PlayerEntity instance, int i) {
        if (!instance.abilities.isCreativeMode) {
            //empty redirect block to stop the method from running lol
        }
    }

    /**
     * @author a1qs
     * @reason remove XP requirement
     */
    @Overwrite
    protected boolean func_230303_b_(PlayerEntity player, boolean p_230303_2_) {
        return true;
    }
}
