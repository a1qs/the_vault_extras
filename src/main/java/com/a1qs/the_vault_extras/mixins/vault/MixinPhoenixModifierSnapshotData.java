package com.a1qs.the_vault_extras.mixins.vault;

import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.data.PhoenixModifierSnapshotData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.modifier.InventoryRestoreModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static iskallia.vault.world.data.PhoenixModifierSnapshotData.get;

@Mixin(value = PhoenixModifierSnapshotData.class, remap = false)
public class MixinPhoenixModifierSnapshotData {
    /**
     * @author JoshWannaPaas
     * @reason Death Snapshots
     */
    @Overwrite
    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity && event.getEntity().world instanceof ServerWorld) {
            ServerPlayerEntity player = (ServerPlayerEntity)event.getEntity();
            ServerWorld world = (ServerWorld)player.world;
            VaultRaid currentRaid = VaultRaidData.get(world).getActiveFor(player);
            if (currentRaid != null
                    && !currentRaid.getActiveModifiersFor(PlayerFilter.of(new PlayerEntity[]{player}), InventoryRestoreModifier.class).isEmpty()) {
                PhoenixModifierSnapshotData data = get(world);
                data.createSnapshot(player);    // Newly Added line. Yes, I just create a snapshot at death before restoring it.
                if (data.hasSnapshot(player)) {
                    player.addTag("the_vault_restore_inventory");
                }
            }
        }
    }
}
