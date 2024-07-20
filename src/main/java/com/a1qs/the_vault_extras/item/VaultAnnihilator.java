package com.a1qs.the_vault_extras.item;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.init.ModItemGroup;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.ToolItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class VaultAnnihilator extends ToolItem {
    public static final ToolType ANNIHILATOR_TOOL_TYPE = ToolType.get("paxel");

    public VaultAnnihilator(Properties id) {
        super(3.0F, -3.0F, AnnihilatorTier.INSTANCE, Collections.emptySet(), (new Item.Properties())
                        .group(ModItemGroup.VAULT_EXTRAS)
                        .addToolType(ANNIHILATOR_TOOL_TYPE, AnnihilatorTier.INSTANCE.getHarvestLevel())
                        .rarity(Rarity.EPIC)
                        .isImmuneToFire()
                        );
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        return super.getAttributeModifiers(slot, stack);
    }

    public int getHarvestLevel(ItemStack stack, ToolType tool, PlayerEntity player, BlockState blockState) {
        return this.getTier().getHarvestLevel();
    }

    public boolean canHarvestBlock(BlockState state) {
        ToolType harvestTool = state.getHarvestTool();
        if ((harvestTool == ToolType.AXE || harvestTool == ToolType.PICKAXE || harvestTool == ToolType.SHOVEL) && this.getTier().getHarvestLevel() >= state.getHarvestLevel()) {
            return true;
        } else if (!state.matchesBlock(Blocks.SNOW) && !state.matchesBlock(Blocks.SNOW_BLOCK)) {
            Material material = state.getMaterial();
            return material == Material.ROCK || material == Material.IRON || material == Material.ANVIL;
        } else {
            return true;
        }
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.SILK_TOUCH) || EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.FORTUNE);
    }

    public float getDestroySpeed(@Nonnull ItemStack stack, BlockState state) {
        return this.getTier().getEfficiency();
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip." + VaultExtras.MOD_ID + ".vault_annihilator1"));
        tooltip.add(new TranslationTextComponent("tooltip." + VaultExtras.MOD_ID + ".vault_annihilator2"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
