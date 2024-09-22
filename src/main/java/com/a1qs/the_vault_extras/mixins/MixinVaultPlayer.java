package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.init.ModGameRules;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.vault.player.VaultPlayer;
import iskallia.vault.world.vault.time.VaultTimer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;
import java.util.function.Function;


@Mixin(value = VaultPlayer.class, remap = false)
public abstract class MixinVaultPlayer {


    @Shadow public abstract VaultTimer getTimer();

    @Shadow public abstract UUID getPlayerId();

    @Shadow public abstract <T> T mapIfPresent(MinecraftServer server, Function<ServerPlayerEntity, T> action, T _default);

    /**
     * @author a1qs
     * @reason change XP gained from Vaults
     */
    @Overwrite
    public void grantVaultExp(MinecraftServer server, float multiplier) {
        PlayerVaultStatsData data = PlayerVaultStatsData.get(server);
        PlayerVaultStats stats = data.getVaultStats(this.getPlayerId());
        float expGrantedPercent = MathHelper.clamp((float)this.getTimer().runTime / ((float)this.getTimer().getStartTime()/2), 0.0F, 1.0F);
        expGrantedPercent *= multiplier;
        //noinspection UnnecessaryUnboxing
        expGrantedPercent *= (float)(this.mapIfPresent(
                server, player -> MathHelper.clamp(player.getServerWorld().getGameRules().getInt(ModGameRules.EXP_MULTIPLIER), 0, 25), 1
        ))
                .intValue();

        expGrantedPercent *= 3; //3x base multiplier we ball
        int vaultLevel = stats.getVaultLevel();
        expGrantedPercent *= MathHelper.clamp(1.0F - (float)vaultLevel / 500.0F, 0.0F, 1.0F);
        float remainingPercent = 1.0F - (float)stats.getExp() / (float)stats.getTnl();
        if (expGrantedPercent > remainingPercent) {
            expGrantedPercent -= remainingPercent;
            int remaining = stats.getTnl() - stats.getExp();
            stats.addVaultExp(server, remaining);
        }

        int expGranted = MathHelper.floor((float)stats.getTnl() * expGrantedPercent);
        stats.addVaultExp(server, expGranted);
        data.markDirty();
    }
}
