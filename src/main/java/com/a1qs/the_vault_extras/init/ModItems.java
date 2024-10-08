package com.a1qs.the_vault_extras.init;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.item.*;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;


public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VaultExtras.MOD_ID);

    public static final RegistryObject<Item> VAULT_ANNIHILATOR = ITEMS.register("vault_annihilator",
            () -> new VaultAnnihilator(new Item.Properties().group(ModItemGroup.VAULT_EXTRAS)));

    public static final RegistryObject<Item> ADVANCED_VAULT_PEARL_SPEED = ITEMS.register("advanced_vault_pearl_speed",
            () -> new AdvancedVaultPearl(new Item.Properties()
                    .group(ModItemGroup.VAULT_EXTRAS)
                    .maxDamage(256), 0.0F,4.5F, 2.5F, 10));

    public static final RegistryObject<Item> ADVANCED_VAULT_PEARL_UNBREAKABLE = ITEMS.register("advanced_vault_pearl_unbreakable",
            () -> new AdvancedVaultPearl(new Item.Properties()
                    .group(ModItemGroup.VAULT_EXTRAS)
                    .maxDamage(-1), 0.0F,1.5F, 1.0F, 20));

    public static final RegistryObject<Item> ADVANCED_VAULT_PEARL_EXPLODE = ITEMS.register("advanced_vault_pearl_explode",
            () -> new AdvancedVaultPearl(new Item.Properties()
                    .group(ModItemGroup.VAULT_EXTRAS)
                    .maxDamage(256), 0.0F,1.5F, 1.0F, 20));

    public static final RegistryObject<Item> CAKE_SEAL = ITEMS.register("cake_seal",
            () -> new CakeSeal(new Item.Properties()
                    .group(ModItemGroup.VAULT_EXTRAS)
                    .maxStackSize(1)));

    public static final RegistryObject<Item> VAULT_MEAL = ITEMS.register("vault_meal",
            () -> new Item(new Item.Properties()
                    .group(ModItemGroup.VAULT_EXTRAS)
                    .food(new Food.Builder().hunger(10).saturation(1.5F).fastToEat().build())));

    public static final RegistryObject<Item> BUNNY_CHARM = ITEMS.register("zephyr_charm",
            () -> new AirMobilityItem(new Item.Properties()
                    .group(ModItemGroup.VAULT_EXTRAS)));

    public static final RegistryObject<Item> DEBUG_ITEM = ITEMS.register("debug_item",
            () -> new DebugItem(new Item.Properties()
                    .group(ModItemGroup.VAULT_EXTRAS)));

    public static final RegistryObject<Item> MYSTERY_RUNE = ITEMS.register("mystery_rune",
            () -> new LootableItemExtras( new Item.Properties().group((ModItemGroup.VAULT_EXTRAS)) ,
                    () -> (ModConfigs.MYSTERY_RUNE.POOL.getRandom(new Random())).generateItemStack()));

    public static final RegistryObject<Item> MYSTERY_BOOK = ITEMS.register("mystery_book",
            () -> new LootableItemExtras( new Item.Properties().group((ModItemGroup.VAULT_EXTRAS)) ,
                    () -> (ModConfigs.MYSTERY_BOOK.POOL.getRandom(new Random())).generateItemStack()));

    public static final RegistryObject<Item> INCOMPLETE_CRYSTAL = ITEMS.register("incomplete_crystal",
            () -> new CrystalChargeItem(new Item.Properties()
                    .group(ModItemGroup.VAULT_EXTRAS)));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
