package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.Vault;
import iskallia.vault.config.DurabilityConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.gear.VaultArmorItem;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.set.PlayerSet;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.UnbreakableTalent;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Random;

@Mixin(value = {ItemStack.class}, priority = 1002)
public abstract class MixinItemStack extends net.minecraftforge.common.capabilities.CapabilityProvider<ItemStack> implements net.minecraftforge.common.extensions.IForgeItemStack {

    protected MixinItemStack(Class<ItemStack> baseClass) {
        super(baseClass);
    }

    @Shadow
    @Final
    @Deprecated
    private Item item;



    @Shadow()
    public abstract boolean isDamageable();

    @Shadow
    public abstract int getDamage();

    @Shadow
    public abstract void setDamage(int var1);

    @Shadow
    public abstract int getMaxDamage();

    @Shadow
    public abstract Item getItem();

    /**
     * @author a1qs
     * @reason remove dura damage from player and make elytra 5x as rare to take dura
     */
    @Overwrite
    public boolean attemptDamageItem(int damage, Random rand, @Nullable ServerPlayerEntity damager) {
        if (!this.isDamageable()) {
            return false;
        } else if (this.item == Items.ELYTRA && (new Random()).nextInt(5) != 0) {
            return false;
        } else if (damager != null && this.getItem() instanceof VaultArmorItem && PlayerSet.isActive(VaultGear.Set.ZOD, damager)) {
            return false;
        } else {
            ItemStack stack = (ItemStack) (Object) this;
            int unbreakingLevel;
            if (damage > 0) {
                /*This is part of the ACTUAL Mixin*/
                if (this.item instanceof VaultGear) {
                    if (damager.getEntityWorld().getDimensionKey() != Vault.VAULT_KEY) {
                        return false;
                    }
                }
                /*End of the actual part*/

                unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
                if (damager != null) {
                    TalentTree abilities = PlayerTalentsData.get(damager.getServerWorld()).getTalents(damager);

                    UnbreakableTalent talent;
                    for (Iterator var6 = abilities.getTalents(UnbreakableTalent.class).iterator(); var6.hasNext(); unbreakingLevel = (int) ((float) unbreakingLevel + talent.getExtraUnbreaking())) {
                        talent = (UnbreakableTalent) var6.next();
                    }
                }

                int damageNegation = 0;
                boolean isArmor = stack.getItem() instanceof ArmorItem;
                DurabilityConfig cfg = ModConfigs.DURBILITY;
                float chance = isArmor ? cfg.getArmorDurabilityIgnoreChance(unbreakingLevel) : cfg.getDurabilityIgnoreChance(unbreakingLevel);


                for (int k = 0; unbreakingLevel > 0 && k < damage; ++k) {
                    if (rand.nextFloat() < chance) {
                        ++damageNegation;
                    }
                }

                damage -= damageNegation;
                if (damage <= 0) {
                    return false;
                }
            }

            if (damager != null && damage != 0) {
                CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(damager, stack, this.getDamage() + damage);
            }

            unbreakingLevel = this.getDamage() + damage;
            this.setDamage(unbreakingLevel);
            return unbreakingLevel >= this.getMaxDamage();
        }
    }
}
