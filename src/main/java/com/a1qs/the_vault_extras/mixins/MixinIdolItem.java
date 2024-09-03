package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.item.gear.IdolItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = IdolItem.class, remap = false)
public class MixinIdolItem {

    @Inject(method = "addInformation", at = @At(value = "TAIL"))
    public void addDuraInfo(ItemStack itemStack, World world, List<ITextComponent> tooltip, ITooltipFlag flag, CallbackInfo ci) {
        StringTextComponent durabilityLabel = new StringTextComponent("Durability: ");
        durabilityLabel.setStyle(Style.EMPTY.applyFormatting(TextFormatting.WHITE));

        StringTextComponent durabilityValues = new StringTextComponent(itemStack.getMaxDamage() - itemStack.getDamage() + "/" + itemStack.getMaxDamage());
        durabilityValues.setStyle(Style.EMPTY.applyFormatting(TextFormatting.GRAY));

        tooltip.add(durabilityLabel.appendSibling(durabilityValues));
    }
}
