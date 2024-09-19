package com.a1qs.the_vault_extras.events;

import com.a1qs.the_vault_extras.config.VaultExtrasConfig;
import com.a1qs.the_vault_extras.mixins.accessors.AccessorVaultRaidData;
import com.a1qs.the_vault_extras.util.DimensionUtil;
import iskallia.vault.Vault;
import iskallia.vault.world.data.VaultRaidData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerTickEvent {
    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            RegistryKey<World> dimensionKey = Vault.VAULT_KEY;
            ServerWorld dimension = server.getWorld(dimensionKey);
            VaultRaidData data = VaultRaidData.get((ServerWorld) event.world);

            if(!((AccessorVaultRaidData) data).getVaults().isEmpty()) return;
            if(!VaultExtrasConfig.ENABLE_VAULT_DELETION.get()) return;
            if(server.getTickCounter() % 6000L != 0L) return;

            if (dimension != null && dimension.getPlayers().isEmpty()) {
                DimensionUtil.deleteDimensionData(server, dimensionKey);
            }
        }

    }
}
