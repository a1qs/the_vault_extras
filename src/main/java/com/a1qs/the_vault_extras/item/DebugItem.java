package com.a1qs.the_vault_extras.item;

import com.a1qs.the_vault_extras.init.ModConfigs;
import com.a1qs.the_vault_extras.mixins.accessors.AccessorVaultRaidData;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancements;
import iskallia.vault.nbt.VListNBT;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.Main;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Iterator;

public class DebugItem extends Item {
    public DebugItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if(context.getWorld().isRemote) return ActionResultType.PASS;
        VaultRaidData data = VaultRaidData.get((ServerWorld) context.getWorld());
        VListNBT<VaultRaid, CompoundNBT> activeVaults = ((AccessorVaultRaidData) data).getVaults();
        System.out.println(activeVaults.isEmpty());
        System.out.println(activeVaults.stream().iterator());

        getAllEntries();
        return ActionResultType.SUCCESS;
    }

    public static void getAllEntries() {
        Iterator<WeightedList.Entry<iskallia.vault.config.entry.vending.ProductEntry>> iter = ModConfigs.MYSTERY_BOOK.POOL.iterator();
        while (iter.hasNext()) {
            WeightedList.Entry<iskallia.vault.config.entry.vending.ProductEntry> entry = iter.next();
            System.out.println("Item: " + entry.value.getItem() + ", Weight: " + entry.weight);
        }
    }
}
