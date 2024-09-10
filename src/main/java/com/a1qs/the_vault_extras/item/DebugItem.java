package com.a1qs.the_vault_extras.item;

import com.a1qs.the_vault_extras.init.ModConfigs;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancements;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.server.Main;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Iterator;

public class DebugItem extends Item {
    public DebugItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
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
