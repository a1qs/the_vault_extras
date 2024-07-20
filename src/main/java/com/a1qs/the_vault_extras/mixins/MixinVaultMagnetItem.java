package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.Vault;
import iskallia.vault.config.DurabilityConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.VaultMagnetItem;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.UnbreakableTalent;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import static iskallia.vault.item.VaultMagnetItem.isMagnet;

@Mixin(value = VaultMagnetItem.class, remap = false)
public abstract class MixinVaultMagnetItem {

    private static final Random random = new Random();

    @Final
    @Shadow
    private static final HashMap<UUID, UUID> pulledItems = new HashMap();

    /**
     * @author a1qs
     * @reason Curios Compatablility & making Unbreaking work with Magnets
     */
    @SubscribeEvent
    @Overwrite
    public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        PlayerEntity player = event.getPlayer();
        PlayerInventory inventory = player.inventory;
        pulledItems.remove(event.getOriginalEntity().getUniqueID());
        int unbreakingLevel;
        DurabilityConfig cfg = ModConfigs.DURBILITY;

        for(int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (isMagnet(stack) && stack.getOrCreateTag().getBoolean("Enabled")) {
                    unbreakingLevel = calculateUnbreakingLevel(player, stack);
                    float chance = cfg.getDurabilityIgnoreChance(unbreakingLevel);
                    if(player.getEntityWorld().getDimensionKey() == Vault.VAULT_KEY) {
                        if (random.nextFloat() > chance) {
                            stack.damageItem(1, player, (onBroken) -> {});
                        }
                    }
                } else {
                    LazyOptional<IItemHandler> itemHandler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                    itemHandler.ifPresent((h) -> {
                        for(int j = 0; j < h.getSlots(); ++j) {
                            ItemStack stackInHandler = h.getStackInSlot(j);
                            if (isMagnet(stackInHandler) && stack.getOrCreateTag().getBoolean("Enabled")) {
                                int handlerUnbreakingLevel = calculateUnbreakingLevel(player, stack);
                                float chance = cfg.getDurabilityIgnoreChance(handlerUnbreakingLevel);
                                if(player.getEntityWorld().getDimensionKey() == Vault.VAULT_KEY) {
                                    if (random.nextFloat() > chance) {
                                        stackInHandler.damageItem(1, player, (onBroken) -> {});
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }

        Item[] vaultMagnets = {ModItems.VAULT_MAGNET_OMEGA, ModItems.VAULT_MAGNET_STRONG, ModItems.VAULT_MAGNET_WEAK};
        for (Item magnet : vaultMagnets) {
            if (CuriosApi.getCuriosHelper().findFirstCurio(inventory.player, magnet).isPresent()) {
                ItemStack stack = CuriosApi.getCuriosHelper().findFirstCurio(inventory.player, magnet).get().getStack();

                if (stack.getOrCreateTag().getBoolean("Enabled")) {
                    unbreakingLevel = calculateUnbreakingLevel(player, stack);
                    float chance = cfg.getDurabilityIgnoreChance(unbreakingLevel);
                    if(player.getEntityWorld().getDimensionKey() == Vault.VAULT_KEY) {
                        if (random.nextFloat() > chance) {
                            stack.damageItem(1, player, (onBroken) -> {});
                        }
                    }
                }
                break;
            }
        }
    }

    /**
     * @author a1qs
     * @reason make Vault Magnets enchantable with Unbreaking
     */
    @Overwrite
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.UNBREAKING);
    }

    private static int calculateUnbreakingLevel(PlayerEntity player, ItemStack stack) {
        int unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);

        if(player instanceof ServerPlayerEntity) {
            unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            TalentTree abilities = PlayerTalentsData.get(serverPlayer.getServerWorld()).getTalents(serverPlayer);
            UnbreakableTalent talent;
            for (Iterator var6 = abilities.getTalents(UnbreakableTalent.class).iterator(); var6.hasNext(); unbreakingLevel = (int) ((float) unbreakingLevel + talent.getExtraUnbreaking())) {
                talent = (UnbreakableTalent) var6.next();
            }
        }
        return unbreakingLevel;
    }
}
