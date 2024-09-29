package com.a1qs.the_vault_extras.mixins.vault;

import iskallia.vault.event.GearAnvilEvents;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.item.gear.applicable.VaultRepairCoreItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = GearAnvilEvents.class, remap = false)
public class MixinGearAnvilEvents {
    /**
     * @author a1qs
     * @reason remove dura reduction upon repairing
     */
    @Overwrite
    @SubscribeEvent
    public static void onApplyRepairCore(AnvilUpdateEvent event) {
        if (event.getRight().getItem() instanceof VaultRepairCoreItem) {
            if (event.getLeft().getItem() instanceof VaultGear) {
                ItemStack output = event.getLeft().copy();
                ItemStack repairCore = event.getRight();
                int repairLevel = ((VaultRepairCoreItem)repairCore.getItem()).getVaultGearTier();
                if (ModAttributes.GEAR_TIER.getOrDefault(output, 0).getValue(output) != repairLevel) {
                    return;
                }

                int maxRepairs = ModAttributes.MAX_REPAIRS.getOrDefault(output, -1).getValue(output);
                int curRepairs = ModAttributes.CURRENT_REPAIRS.getOrDefault(output, 0).getValue(output);
                if (maxRepairs == -1 || curRepairs >= maxRepairs) {
                    return;
                }

                ModAttributes.CURRENT_REPAIRS.create(output, curRepairs + 1);
                output.setDamage(0);
                event.setOutput(output);
                event.setMaterialCost(1);
                event.setCost(1);
            }
        }
    }
}
