package com.a1qs.the_vault_extras.item.paxel;

import iskallia.vault.item.paxel.enhancement.PaxelEnhancement;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancements;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Color;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class ReachingEnhancement extends PaxelEnhancement {
    private static final UUID reachModifierUUID = UUID.fromString("3f9601d3-3975-4b67-84f3-326809805c3a");
    protected float extraReach;
    private static int previousSlot = -1;
    private static boolean wasEnhanced = false;

    public ReachingEnhancement(float extraReach) {
        this.extraReach = extraReach;
    }

    public float getExtraReach() {
        return this.extraReach;
    }

    @Override
    public Color getColor() {
        return null;
    }

    @SubscribeEvent
    public static void onInventoryTick(TickEvent.PlayerTickEvent event) {
        if (!event.side.isClient()) {
            if (event.phase == TickEvent.Phase.END) {
                ServerPlayerEntity player = (ServerPlayerEntity) event.player;
                int currentHeldSlotIndex = player.inventory.currentItem;
                ItemStack currentStack = player.inventory.mainInventory.get(currentHeldSlotIndex);
                PaxelEnhancement currentEnhancement = PaxelEnhancements.getEnhancement(currentStack);

                if (previousSlot != currentHeldSlotIndex || !(currentEnhancement instanceof ReachingEnhancement)) {
                    if (wasEnhanced) {
                        // Remove the reach modifier if it was applied previously
                        if (player.getAttribute(ForgeMod.REACH_DISTANCE.get()) != null) {
                            player.getAttribute(ForgeMod.REACH_DISTANCE.get()).removeModifier(reachModifierUUID);
                        }
                        wasEnhanced = false;
                    }
                }

                if (currentEnhancement instanceof ReachingEnhancement && player.getAttribute(ForgeMod.REACH_DISTANCE.get()) != null) {
                    ReachingEnhancement reachingEnhancement = (ReachingEnhancement) currentEnhancement;
                    player.getAttribute(ForgeMod.REACH_DISTANCE.get()).removeModifier(reachModifierUUID);
                    AttributeModifier reachModifier = new AttributeModifier(reachModifierUUID, "CustomReachDistance", reachingEnhancement.getExtraReach(), AttributeModifier.Operation.ADDITION);
                    player.getAttribute(ForgeMod.REACH_DISTANCE.get()).applyNonPersistentModifier(reachModifier);
                    wasEnhanced = true;
                }
            }
        }
    }
}
