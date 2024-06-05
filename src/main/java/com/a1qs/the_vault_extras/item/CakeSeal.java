package com.a1qs.the_vault_extras.item;

import com.a1qs.the_vault_extras.VaultExtras;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CakeSeal extends Item {
    public CakeSeal(Properties builder) {
        super(builder);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        Item item = stack.getItem();
        tooltip.add(new TranslationTextComponent("tooltip." + VaultExtras.MOD_ID + ".cake_seal1"));
        tooltip.add(new TranslationTextComponent("tooltip." + VaultExtras.MOD_ID + ".cake_seal2"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
