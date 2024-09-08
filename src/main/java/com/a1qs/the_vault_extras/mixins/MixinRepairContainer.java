package com.a1qs.the_vault_extras.mixins;

import com.a1qs.the_vault_extras.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.AbstractRepairContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.util.IWorldPosCallable;
import org.jetbrains.annotations.Nullable;
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
