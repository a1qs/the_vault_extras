package com.a1qs.the_vault_extras.mixins.vault;

import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.set.PlayerSet;
import iskallia.vault.world.data.PhoenixSetSnapshotData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static iskallia.vault.world.data.PhoenixSetSnapshotData.get;

@Mixin(value = PhoenixSetSnapshotData.class, remap = false)
public class MixinPhoenixSetSnapshotData {

    /**
     * @author JoshWannaPaas
     * @reason Death Snapshots
     */
    @Overwrite
    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayerEntity) ||
                !((event.getEntity()).world instanceof ServerWorld)) {
            return;
        }
        ServerPlayerEntity player = (ServerPlayerEntity)event.getEntity();
        ServerWorld world = (ServerWorld)player.world;
        VaultRaid vault = VaultRaidData.get(world).getAt(world, player.getPosition());
        if (vault != null &&
                vault.getProperties().exists(VaultRaid.PARENT)) {
            CrystalData data = (CrystalData)vault.getProperties().getBaseOrDefault(VaultRaid.CRYSTAL_DATA, CrystalData.EMPTY);
            return;
        }
        if (PlayerSet.isActive(VaultGear.Set.PHOENIX, (LivingEntity)player)) {
            PhoenixSetSnapshotData data = get(world);
            data.createSnapshot(player);    // Newly Added line. Yes, I just create a snapshot at death before restoring it.
            if (data.hasSnapshot((PlayerEntity)player))
            {
                player.addTag("the_vault_restore_phoenixset");
            }
        }
    }
}
