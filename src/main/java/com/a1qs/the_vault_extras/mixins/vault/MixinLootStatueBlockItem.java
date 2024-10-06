package com.a1qs.the_vault_extras.mixins.vault;

import iskallia.vault.block.item.LootStatueBlockItem;
import iskallia.vault.util.StatueType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = LootStatueBlockItem.class, remap = false)
public class MixinLootStatueBlockItem {

    @Shadow
    @Final
    private StatueType type;

    /**
     * @author Josh
     * @reason Add identifier for drained statues
     */
    @Overwrite
    @OnlyIn(Dist.CLIENT)
    protected void addStatueInformation(CompoundNBT dataTag, List<ITextComponent> toolTip) {

        int itemsRemaining = dataTag.getInt("ItemsRemaining");
        if(itemsRemaining == 0) {
            toolTip.add(new StringTextComponent("\u2620 DRAINED \u2620").mergeStyle(TextFormatting.DARK_RED));
            toolTip.add(StringTextComponent.EMPTY);
        }

        String nickname = dataTag.getString("PlayerNickname");
        toolTip.add(new StringTextComponent("Player: "));
        toolTip.add((new StringTextComponent("- ")).appendSibling((new StringTextComponent(nickname)).mergeStyle(TextFormatting.GOLD)));
        if (this.type.dropsItems()) {
            ITextComponent itemDescriptor = (new StringTextComponent("NOT SELECTED")).mergeStyle(TextFormatting.RED);
            if (dataTag.contains("LootItem")) {
                ItemStack lootItem = ItemStack.read(dataTag.getCompound("LootItem"));
                itemDescriptor = (new StringTextComponent(lootItem.getDisplayName().getString())).mergeStyle(TextFormatting.GREEN);
            }

            toolTip.add(StringTextComponent.EMPTY);
            toolTip.add((new StringTextComponent("Item: ")).mergeStyle(TextFormatting.WHITE));
            toolTip.add((new StringTextComponent("- ")).appendSibling(itemDescriptor));
        }

    }
}
