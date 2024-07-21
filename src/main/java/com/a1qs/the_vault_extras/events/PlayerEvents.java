package com.a1qs.the_vault_extras.events;

import com.a1qs.the_vault_extras.init.ModGameRules;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.crystal.VaultCrystalItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collections;
import java.util.List;

public class PlayerEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            PlayerEntity player = event.player;
            if (player.getEntityWorld().getGameRules().getBoolean(ModGameRules.VAULT_CASUAL_MODE) && ModConfigs.VAULT_MODIFIERS.getByName("Casual") != null) {
                if (player.getHeldItemMainhand().getItem() instanceof VaultCrystalItem) {
                    List<String> requiredModifiers = Collections.singletonList("casual");
                    ItemStack crystal = player.getHeldItemMainhand();
                    CrystalData data = VaultCrystalItem.getData(crystal);
                    if(data.getModifiers().stream().noneMatch(modifier -> requiredModifiers.contains(modifier.getModifierName().toLowerCase())) || data.getModifiers().isEmpty()) {
                        data.addModifier("Casual");
                    }

                } else if (player.getHeldItemOffhand().getItem() instanceof VaultCrystalItem) {
                    List<String> requiredModifiers = Collections.singletonList("casual");
                    ItemStack crystal = player.getHeldItemOffhand();
                    CrystalData data = VaultCrystalItem.getData(crystal);
                    if(data.getModifiers().stream().noneMatch(modifier -> requiredModifiers.contains(modifier.getModifierName().toLowerCase())) || data.getModifiers().isEmpty()) {
                        data.addModifier("Casual");
                    }
                }
            }
        }
    }

}
