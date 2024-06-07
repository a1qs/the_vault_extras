package com.a1qs.the_vault_extras.mixins;

import com.a1qs.the_vault_extras.init.ModItems;
import iskallia.vault.Vault;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.*;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.crystal.VaultCrystalItem;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancements;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import iskallia.vault.event.AnvilEvents;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(value={AnvilEvents.class}, remap = false)
public class MixinAnvilEvents {

    /**
     * @author JoshWannaPaas
     * @reason Change Cake Hunt to require custom item instead of regular Cake, change xp cost to 1
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

    /**
     * @author a1qs
     * @reason change xp cost to 1 for Paxel Charm applications
     */
    @Overwrite
    @SubscribeEvent
    public static void onApplyPaxelCharm(AnvilUpdateEvent event) {
        ItemStack paxelStack = event.getLeft();
        ItemStack charmStack = event.getRight();
        if (paxelStack.getItem() == iskallia.vault.init.ModItems.VAULT_PAXEL) {
            if (charmStack.getItem() == iskallia.vault.init.ModItems.PAXEL_CHARM) {
                if (PaxelEnhancements.getEnhancement(paxelStack) == null) {
                    ItemStack enhancedStack = paxelStack.copy();
                    PaxelEnhancements.markShouldEnhance(enhancedStack);
                    event.setCost(1);
                    event.setOutput(enhancedStack);
                }
            }
        }
    }

    /**
     * @author a1qs
     * @reason change xp cost to 1 for Seal applications
     */
    @Overwrite
    @SubscribeEvent
    public static void onApplySeal(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() instanceof ItemVaultCrystalSeal) {
            ItemStack output = event.getLeft().copy();
            CrystalData data = VaultCrystalItem.getData(output);
            if (!data.getModifiers().isEmpty() || data.getSelectedObjective() != null) {
                return;
            }

            VaultRaid.init();
            ResourceLocation objectiveKey = ((ItemVaultCrystalSeal)event.getRight().getItem()).getObjectiveId();
            VaultObjective objective = VaultObjective.getObjective(objectiveKey);
            if (objective != null) {
                data.setSelectedObjective(objectiveKey);
                VaultCrystalItem.setRandomSeed(output);
                event.setOutput(output);
                event.setMaterialCost(1);
                event.setCost(1);
            }
        }
    }

    /**
     * @author a1qs
     * @reason change xp cost to 1 for Raffle Seal applications
     */
    @Overwrite
    @SubscribeEvent
    public static void onApplyRaffleSeal(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() instanceof ItemVaultRaffleSeal) {
            ItemStack output = event.getLeft().copy();
            CrystalData data = VaultCrystalItem.getData(output);
            if (!data.getModifiers().isEmpty() || data.getSelectedObjective() != null) {
                return;
            }

            String playerName = ItemVaultRaffleSeal.getPlayerName(event.getRight());
            if (playerName.isEmpty()) {
                return;
            }

            data.setPlayerBossName(playerName);
            event.setOutput(output);
            event.setMaterialCost(1);
            event.setCost(1);
        }
    }

    /**
     * @author a1qs
     * @reason change xp cost to 1 for Catalyst applications
     */
    @Overwrite
    @SubscribeEvent
    public static void onApplyCatalyst(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() instanceof VaultCatalystItem) {
            ItemStack output = event.getLeft().copy();
            CrystalData data = VaultCrystalItem.getData(output);
            if (!data.canModifyWithCrafting()) {
                return;
            }

            List<String> modifiers = VaultCatalystItem.getCrystalCombinationModifiers(event.getRight(), event.getLeft());
            if (modifiers == null || modifiers.isEmpty()) {
                return;
            }

            modifiers.forEach(modifier -> data.addCatalystModifier(modifier, true, CrystalData.Modifier.Operation.ADD));
            event.setOutput(output);
            event.setCost(1);
            event.setMaterialCost(1);
        }
    }

    /**
     * @author a1qs
     * @reason change xp cost to 1 for Rune applications
     */
    @Overwrite
    @SubscribeEvent
    public static void onApplyRune(AnvilUpdateEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() instanceof VaultRuneItem) {
                ServerPlayerEntity sPlayer = (ServerPlayerEntity)event.getPlayer();
                int level = PlayerVaultStatsData.get(sPlayer.getServerWorld()).getVaultStats(sPlayer).getVaultLevel();
                int minLevel = ModConfigs.VAULT_RUNE.getMinimumLevel(event.getRight().getItem()).orElse(0);
                if (level < minLevel) {
                    return;
                }

                ItemStack output = event.getLeft().copy();
                CrystalData data = VaultCrystalItem.getData(output);
                if (!data.canModifyWithCrafting()) {
                    return;
                }

                VaultRuneItem runeItem = (VaultRuneItem)event.getRight().getItem();
                if (!data.canAddRoom(runeItem.getRoomName())) {
                    return;
                }

                int amount = event.getRight().getCount();

                for(int i = 0; i < amount; ++i) {
                    data.addGuaranteedRoom(runeItem.getRoomName());
                }

                event.setOutput(output);
                event.setCost(1);
                event.setMaterialCost(amount);
            }
        }
    }

    /**
     * @author a1qs
     * @reason change xp cost to 1 for Painite Star applications
     */
    @Overwrite
    @SubscribeEvent
    public static void onApplyPainiteStar(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() == iskallia.vault.init.ModItems.PAINITE_STAR) {
            ItemStack output = event.getLeft().copy();
            if (!VaultCrystalItem.getData(output).canBeModified()) {
                return;
            }

            VaultCrystalItem.setRandomSeed(output);
            event.setOutput(output);
            event.setCost(1);
            event.setMaterialCost(1);
        }
    }

    /**
     * @author a1qs
     * @reason change xp cost to 1 for Soul Flame applications
     */
    @Overwrite
    @SubscribeEvent
    public static void onApplyInhibitor(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() instanceof VaultInhibitorItem) {
            ItemStack output = event.getLeft().copy();
            CrystalData data = VaultCrystalItem.getData(output);
            if (!data.canModifyWithCrafting()) {
                return;
            }

            List<CrystalData.Modifier> crystalModifiers = data.getModifiers();
            List<String> inhibitorModifiers = VaultInhibitorItem.getCrystalCombinationModifiers(event.getRight(), event.getLeft());
            if (crystalModifiers.isEmpty() || inhibitorModifiers == null || inhibitorModifiers.isEmpty()) {
                return;
            }

            inhibitorModifiers.forEach(modifier -> data.removeCatalystModifier(modifier, true, CrystalData.Modifier.Operation.ADD));
            VaultCrystalItem.markAttemptExhaust(output);
            VaultCrystalItem.setRandomSeed(output);
            event.setOutput(output);
            event.setCost(inhibitorModifiers.size() * 8);
            event.setMaterialCost(1);
        }
    }

    /**
     * @author a1qs
     * @reason change xp cost to 1 for Soul Flame applications
     */
    @Overwrite
    @SubscribeEvent
    public static void onApplySoulFlame(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() == iskallia.vault.init.ModItems.SOUL_FLAME) {
            ItemStack output = event.getLeft().copy();
            CrystalData data = VaultCrystalItem.getData(output);
            if (data.getType() == CrystalData.Type.FINAL_LOBBY) {
                return;
            }

            if (!data.getModifiers().isEmpty()) {
                return;
            }

            if (!data.canAddModifier("Afterlife", CrystalData.Modifier.Operation.ADD)) {
                return;
            }

            if (data.addCatalystModifier("Afterlife", false, CrystalData.Modifier.Operation.ADD)) {
                event.setOutput(output);
                event.setMaterialCost(1);
                event.setCost(1);
            }
        }
    }

    /**
     * @author a1qs
     * @reason change xp cost to 1 for Soul Flame applications
     */
    @Overwrite
    @SubscribeEvent
    public static void onRepairMagnet(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultMagnetItem) {
            if (event.getRight().getItem() == iskallia.vault.init.ModItems.MAGNETITE) {
                if (event.getLeft().getDamage() != 0 && event.getLeft().getOrCreateTag().getInt("TotalRepairs") < 30) {
                    ItemStack magnet = event.getLeft();
                    ItemStack magnetite = event.getRight();
                    ItemStack output = magnet.copy();
                    CompoundNBT nbt = output.getOrCreateTag();
                    if (!nbt.contains("TotalRepairs")) {
                        nbt.putInt("TotalRepairs", 0);
                        output.setTag(nbt);
                    }

                    int damage = magnet.getDamage();
                    int repairAmount = (int)Math.ceil((double)magnet.getMaxDamage() * 0.1);
                    int newDamage = Math.max(0, damage - magnetite.getCount() * repairAmount);
                    int materialCost = (int)Math.ceil((double)(damage - newDamage) / (double)repairAmount);
                    event.setMaterialCost(materialCost);
                    event.setCost(1);
                    nbt.putInt("TotalRepairs", (int)Math.ceil((double)(materialCost + nbt.getInt("TotalRepairs"))));
                    output.setTag(nbt);
                    output.setDamage(newDamage);
                    event.setOutput(output);
                } else {
                    event.setCanceled(true);
                }
            }
        }
    }

}
