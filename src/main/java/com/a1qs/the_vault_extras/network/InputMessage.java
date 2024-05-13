package com.a1qs.the_vault_extras.network;


import iskallia.vault.init.ModItems;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Supplier;

public class InputMessage {

    public int key;

    public InputMessage() {
    }

    public InputMessage(int key) {
        this.key = key;
    }

    public static void encode(InputMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.key);
    }

    public static InputMessage decode(PacketBuffer buffer) {
        return new InputMessage(buffer.readInt());
    }

    public static void handle (InputMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() ->{
            ServerPlayerEntity player = context.getSender();

            assert player != null;
            PlayerInventory inventory = player.inventory;


            // I don't care that this code is disgusting, it works. and im happy. deal with it

            // Check for Omega magnet
            if(CuriosApi.getCuriosHelper().findFirstCurio(inventory.player, ModItems.VAULT_MAGNET_OMEGA).isPresent()) {
                ItemStack stack = CuriosApi.getCuriosHelper().findFirstCurio(inventory.player, ModItems.VAULT_MAGNET_OMEGA).get().getStack();
                System.out.println(stack.getDamage());
                System.out.println(stack.getMaxDamage());

                if(stack.getOrCreateTag().getBoolean("Enabled")) {
                    CompoundNBT tag = stack.getOrCreateTag();
                    // disable magnet
                    tag.putBoolean("Enabled", false);
                    stack.setTag(tag);
                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2F, 0.8F);
                    player.sendStatusMessage(new StringTextComponent("Toggled Magnet: " + TextFormatting.RED + "OFF"), true);
                } else if (stack.getDamage() < stack.getMaxDamage() -1) {

                    CompoundNBT tag = stack.getOrCreateTag();
                    // enable magnet
                    tag.putBoolean("Enabled", true);
                    stack.setTag(tag);
                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.4F, 0.2F);

                    player.sendStatusMessage(new StringTextComponent("Toggled Magnet: " + TextFormatting.GREEN + "ON"), true);
                }
            }
            // Check for Strong magnet
            if(CuriosApi.getCuriosHelper().findFirstCurio(inventory.player, ModItems.VAULT_MAGNET_STRONG).isPresent()) {
                ItemStack stack = CuriosApi.getCuriosHelper().findFirstCurio(inventory.player, ModItems.VAULT_MAGNET_STRONG).get().getStack();
                if(stack.getOrCreateTag().getBoolean("Enabled")) {
                    CompoundNBT tag = stack.getOrCreateTag();
                    tag.putBoolean("Enabled", false);
                    stack.setTag(tag);
                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2F, 0.8F);
                    player.sendStatusMessage(new StringTextComponent("Toggled Magnet: " + TextFormatting.RED + "OFF"), true);
                } else if (stack.getDamage() < stack.getMaxDamage() -1) {
                    CompoundNBT tag = stack.getOrCreateTag();
                    tag.putBoolean("Enabled", true);
                    stack.setTag(tag);
                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.4F, 0.2F);
                    player.sendStatusMessage(new StringTextComponent("Toggled Magnet: " + TextFormatting.GREEN + "ON"), true);
                }
            }
            // Check for Weak magnet
            if(CuriosApi.getCuriosHelper().findFirstCurio(inventory.player, ModItems.VAULT_MAGNET_WEAK).isPresent()) {
                ItemStack stack = CuriosApi.getCuriosHelper().findFirstCurio(inventory.player, ModItems.VAULT_MAGNET_WEAK).get().getStack();
                if(stack.getOrCreateTag().getBoolean("Enabled")) {
                    CompoundNBT tag = stack.getOrCreateTag();
                    tag.putBoolean("Enabled", false);
                    stack.setTag(tag);
                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2F, 0.8F);
                    player.sendStatusMessage(new StringTextComponent("Toggled Magnet: " + TextFormatting.RED + "OFF"), true);
                } else if (stack.getDamage() < stack.getMaxDamage() -1) {
                    CompoundNBT tag = stack.getOrCreateTag();
                    tag.putBoolean("Enabled", true);
                    stack.setTag(tag);
                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.4F, 0.2F);
                    player.sendStatusMessage(new StringTextComponent("Toggled Magnet: " + TextFormatting.GREEN + "ON"), true);
                }
            }
        });

        context.setPacketHandled(true);
    }
}
