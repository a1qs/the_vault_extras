package com.a1qs.the_vault_extras.init;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.block.tileentity.VaultRecyclerTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
    public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, VaultExtras.MOD_ID);

    public static RegistryObject<TileEntityType<VaultRecyclerTile>> VAULT_RECYCLER_TILE =
            TILE_ENTITIES.register("vault_recycler_tile", () -> TileEntityType.Builder.create(
                    VaultRecyclerTile::new, ModBlocks.VAULT_RECYCLER.get()).build(null)
            );

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }

}
