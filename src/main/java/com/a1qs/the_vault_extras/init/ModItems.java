package com.a1qs.the_vault_extras.init;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.item.AdvancedVaultPearl;
import com.a1qs.the_vault_extras.item.VaultAnnihilator;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;



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




    public static void register (IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
