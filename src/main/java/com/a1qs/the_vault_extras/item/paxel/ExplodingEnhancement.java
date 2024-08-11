package com.a1qs.the_vault_extras.item.paxel;

import iskallia.vault.item.paxel.enhancement.PaxelEnhancement;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancements;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Color;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ExplodingEnhancement extends PaxelEnhancement {
    @Override
    public Color getColor() {
        return Color.fromInt(-4318198);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onBlockMined(BlockEvent.BreakEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ItemStack heldStack = player.getHeldItemMainhand();
        if (PaxelEnhancements.getEnhancement(heldStack) instanceof ExplodingEnhancement) {
            ServerWorld world = (ServerWorld) event.getWorld();
            BlockPos breakPos = event.getPos();
            TNTEntity tntEntity = new TNTEntity(world, breakPos.getX(), breakPos.getY(), breakPos.getZ(), null);
            tntEntity.setFuse(0);
            world.addEntity(tntEntity);
        }
    }

}
