package com.a1qs.the_vault_extras.init;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.item.Firestone;
import com.a1qs.the_vault_extras.item.VaultAnnihilator;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;



public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VaultExtras.MOD_ID);

    public static final RegistryObject<Item> TEST_ITEM = ITEMS.register("test_item",
            () -> new Item(new Item.Properties().group(ModItemGroup.VAULT_EXTRAS)));

    // Item Implemented from a tutorial
    public static final RegistryObject<Item> FIRESTONE = ITEMS.register("firestone",
            () -> new Firestone(new Item.Properties().group(ModItemGroup.VAULT_EXTRAS).maxDamage(8)));

    public static final RegistryObject<Item> VAULT_ANNIHILATOR = ITEMS.register("vault_annihilator",
            () -> new VaultAnnihilator(new Item.Properties().group(ModItemGroup.VAULT_EXTRAS)));






    public static void register (IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
