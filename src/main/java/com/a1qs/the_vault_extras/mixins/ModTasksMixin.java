package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.init.ModTasks;

import iskallia.vault.util.scheduler.DailyScheduler;
import iskallia.vault.world.data.SoulShardTraderData;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModTasks.class, remap = false)
public class ModTasksMixin {

    @Inject(method = "initTasks", at = @At("HEAD"), cancellable = true)
    private static void addInitTasks (DailyScheduler scheduler, MinecraftServer server, CallbackInfo ci){

        //make soul shard shop reset every 2 hours
        scheduler.scheduleServer(1, () -> SoulShardTraderData.get(server).resetDailyTrades());
        scheduler.scheduleServer(5, () -> SoulShardTraderData.get(server).resetDailyTrades());
        scheduler.scheduleServer(7, () -> SoulShardTraderData.get(server).resetDailyTrades());
        scheduler.scheduleServer(9, () -> SoulShardTraderData.get(server).resetDailyTrades());
        scheduler.scheduleServer(11, () -> SoulShardTraderData.get(server).resetDailyTrades());
        scheduler.scheduleServer(13, () -> SoulShardTraderData.get(server).resetDailyTrades());
        scheduler.scheduleServer(17, () -> SoulShardTraderData.get(server).resetDailyTrades());
        scheduler.scheduleServer(19, () -> SoulShardTraderData.get(server).resetDailyTrades());
        scheduler.scheduleServer(21, () -> SoulShardTraderData.get(server).resetDailyTrades());
        scheduler.scheduleServer(23, () -> SoulShardTraderData.get(server).resetDailyTrades());
    }

}
