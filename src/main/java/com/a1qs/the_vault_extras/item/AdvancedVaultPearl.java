package com.a1qs.the_vault_extras.item;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.init.ModItems;
import com.a1qs.the_vault_extras.item.entity.AdvancedVaultPearlEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AdvancedVaultPearl extends EnderPearlItem {

    float z;
    float velocity;
    float inaccuracy;
    int cd;

    public AdvancedVaultPearl(Properties builder, float zIn, float velocityIn, float inaccuracyIn, int cdIn) {
        super(builder);
        z = zIn;
        velocity = velocityIn;
        inaccuracy = inaccuracyIn;
        cd = cdIn;
    }

    public @NotNull ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @NotNull Hand handIn) {
        ItemStack stack = player.getHeldItem(handIn);
        world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        player.getCooldownTracker().setCooldown(ModItems.ADVANCED_VAULT_PEARL_SPEED.get(), cd);
        player.getCooldownTracker().setCooldown(ModItems.ADVANCED_VAULT_PEARL_UNBREAKABLE.get(), cd);
        player.getCooldownTracker().setCooldown(ModItems.ADVANCED_VAULT_PEARL_EXPLODE.get(), cd);
        player.getCooldownTracker().setCooldown(iskallia.vault.init.ModItems.VAULT_PEARL, cd);

        if (!world.isRemote) {
            if(stack.getItem() == ModItems.ADVANCED_VAULT_PEARL_EXPLODE.get()) {
                AdvancedVaultPearlEntity pearlEntity = new AdvancedVaultPearlEntity(world, player, true, false);
                pearlEntity.setItem(stack);
                pearlEntity.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, z, velocity, inaccuracy);
                world.addEntity(pearlEntity);
            } else if(stack.getItem() == ModItems.ADVANCED_VAULT_PEARL_SPEED.get()) {
                AdvancedVaultPearlEntity pearlEntity = new AdvancedVaultPearlEntity(world, player, false, true);
                pearlEntity.setItem(stack);
                pearlEntity.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, z, velocity, inaccuracy);
                world.addEntity(pearlEntity);
            } else {
                AdvancedVaultPearlEntity pearlEntity = new AdvancedVaultPearlEntity(world, player, false, false);
                pearlEntity.setItem(stack);
                pearlEntity.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, z, velocity, inaccuracy);
                world.addEntity(pearlEntity);
            }

            if(stack.getMaxDamage() != -1) {
                stack.damageItem(1, player, (e) -> e.sendBreakAnimation(handIn));
            }
        }

        player.addStat(Stats.ITEM_USED.get(this));
        return ActionResult.func_233538_a_(stack, world.isRemote());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        Item item = stack.getItem();
        if (item == ModItems.ADVANCED_VAULT_PEARL_UNBREAKABLE.get()) {
            tooltip.add(new TranslationTextComponent("tooltip." + VaultExtras.MOD_ID + ".advanced_vault_pearl_unbreakable"));
        } else if (item == ModItems.ADVANCED_VAULT_PEARL_SPEED.get()) {
            tooltip.add(new TranslationTextComponent("tooltip." + VaultExtras.MOD_ID + ".advanced_vault_pearl_speed"));
        } else if (item == ModItems.ADVANCED_VAULT_PEARL_EXPLODE.get()) {
            tooltip.add(new TranslationTextComponent("tooltip." + VaultExtras.MOD_ID + ".advanced_vault_pearl_explode"));

        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public boolean showDurabilityBar(ItemStack stack) {
        return stack.getDamage() > 0;
    }

    public void setDamage(ItemStack stack, int damage) {
        int currentDamage = this.getDamage(stack);
        if (damage > currentDamage) {
            super.setDamage(stack, damage);
        }
    }

    public boolean isDamageable() {
        return true;
    }

    public double getDurabilityForDisplay(ItemStack stack) {
        return (double)stack.getDamage() / (double)this.getMaxDamage(stack);
    }

    public boolean isEnchantable(@NotNull ItemStack stack) {
        return false;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

}
