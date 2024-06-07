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
public class MixinModTasks {

    @Inject(method = "initTasks", at = @At("HEAD"), cancellable = true)
    private static void addInitTasks (DailyScheduler scheduler, MinecraftServer server, CallbackInfo ci){

        int[] scheduleTimes = {1, 5, 7, 9, 11, 13, 17, 19, 21, 23};
        for (int time : scheduleTimes) {
            scheduler.scheduleServer(time, () -> SoulShardTraderData.get(server).resetDailyTrades());
        }
    }

}
