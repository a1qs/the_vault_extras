package com.a1qs.the_vault_extras.mixins;

import com.a1qs.the_vault_extras.init.ModItems;
import iskallia.vault.Vault;
import iskallia.vault.event.AnvilEvents;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.crystal.VaultCrystalItem;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value={AnvilEvents.class}, remap = false)
public class MixinAnvilEvents {

    /**
     * @author JoshWannaPaas
     * @reason Change Cake Hunt to require custom item instead of regular Cake
     */

    @Overwrite
    @SubscribeEvent
    public static void onApplyCake(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() == ModItems.CAKE_SEAL.get()) {
            ItemStack output = event.getLeft().copy();
            CrystalData data = VaultCrystalItem.getData(output);
            if (!data.getModifiers().isEmpty() || (data.getSelectedObjective() != null && data.getType() == CrystalData.Type.COOP)) {
                return;
            }

            VaultRaid.init();
            data.setSelectedObjective(Vault.id("cake_hunt"));
            VaultCrystalItem.setRandomSeed(output);
            event.setOutput(output);
            event.setMaterialCost(1);
            event.setCost(1);
        }
    }
}
