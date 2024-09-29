package com.a1qs.the_vault_extras.mixins.minecraft;

import iskallia.vault.Vault;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.potion.EffectUtils;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(value = PlayerEntity.class, remap = false)
public class MixinPlayerEntity {

    /**
     * @author a1qs
     * @reason fix bug with mining speed bugging out
     */
    @Overwrite
    public float getDigSpeed(BlockState state, @Nullable BlockPos pos) {
        PlayerEntity instance = (PlayerEntity) (Object) this;
        float f = instance.inventory.getDestroySpeed(state);
        if (f > 1.0F) {
            int i = EnchantmentHelper.getEfficiencyModifier(instance);
            ItemStack itemstack = instance.getHeldItemMainhand();
            if (i > 0 && !itemstack.isEmpty()) {
                f += (float)(i * i + 1);
            }
        }

        if (EffectUtils.hasMiningSpeedup(instance)) {
            f *= 1.0F + (float)(EffectUtils.getMiningSpeedup(instance) + 1) * 0.2F;
        }

        if (instance.isPotionActive(Effects.MINING_FATIGUE)) {
            float f1;
            switch(instance.getActivePotionEffect(Effects.MINING_FATIGUE).getAmplifier()) {
                case 0:
                    f1 = 0.3F;
                    break;
                case 1:
                    f1 = 0.09F;
                    break;
                case 2:
                    f1 = 0.0027F;
                    break;
                case 3:
                default:
                    f1 = 8.1E-4F;
            }

            f *= f1;
        }

        if (instance.areEyesInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(instance)) {
            f /= 5.0F;
        }

        if (the_vault_extras$shouldApplyAirPenalty(instance) && !instance.isOnGround()) {
            f /= 5.0F;
        }

        f = net.minecraftforge.event.ForgeEventFactory.getBreakSpeed(instance, state, f, pos);
        return f;
    }

    @Unique
    private static boolean the_vault_extras$shouldApplyAirPenalty(PlayerEntity player) {
        return player.getEntityWorld().getDimensionKey() != Vault.VAULT_KEY || !(player.getHeldItemMainhand().getItem() instanceof ToolItem);
    }
}