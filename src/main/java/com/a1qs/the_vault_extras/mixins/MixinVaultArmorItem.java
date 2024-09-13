package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.item.gear.VaultArmorItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = VaultArmorItem.class, remap = false)
public class MixinVaultArmorItem {

    @OnlyIn(Dist.CLIENT)
    @Inject(method = "addInformation", at = @At(value = "TAIL"))
    public void addDuraInfo(ItemStack itemStack, World world, List<ITextComponent> tooltip, ITooltipFlag flag, CallbackInfo ci) {
        StringTextComponent durabilityLabel = new StringTextComponent("Durability: ");
        durabilityLabel.setStyle(Style.EMPTY.applyFormatting(TextFormatting.WHITE));

        StringTextComponent durabilityValues = new StringTextComponent(itemStack.getMaxDamage() - itemStack.getDamage() + "/" + itemStack.getMaxDamage());
        durabilityValues.setStyle(Style.EMPTY.applyFormatting(TextFormatting.GRAY));

            tooltip.add(durabilityLabel.appendSibling(durabilityValues));
    }
}
