package com.a1qs.the_vault_extras.mixins.vault;

import iskallia.vault.event.ActiveFlags;
import iskallia.vault.item.paxel.enhancement.HammerEnhancement;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancements;
import iskallia.vault.util.BlockDropCaptureHelper;
import iskallia.vault.util.BlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Iterator;
import java.util.List;

import static iskallia.vault.item.paxel.enhancement.HammerEnhancement.breakPoses;
import static iskallia.vault.item.paxel.enhancement.HammerEnhancement.calcBreakAxis;

@Mixin(value = HammerEnhancement.class, remap = false)
public class MixinHammerEnhancement {

    /**
     * @author Josh
     * @reason Make hammers only use 1 durability per swing instead of per block
     */
    @SubscribeEvent(priority = EventPriority.LOW)
    @Overwrite
    public static void onBlockMined(BlockEvent.BreakEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
        ItemStack heldStack = player.getHeldItemMainhand();
        if (PaxelEnhancements.getEnhancement(heldStack) instanceof HammerEnhancement) {
            ServerWorld world = (ServerWorld)event.getWorld();
            BlockPos centerPos = event.getPos();
            Direction.Axis axis = calcBreakAxis(player, centerPos);
            List<BlockPos> sidePoses = breakPoses(centerPos, axis);
            BlockState centerState = world.getBlockState(centerPos);
            float centerHardness = centerState.getBlockHardness(world, centerPos);
            ActiveFlags.IS_AOE_MINING.runIfNotSet(() -> {
                Iterator var5 = sidePoses.iterator();

                while(var5.hasNext()) {
                    BlockPos sidePos = (BlockPos)var5.next();
                    BlockState state = world.getBlockState(sidePos);
                    if (!state.getBlock().isAir(state, world, sidePos)) {
                        float sideHardness = state.getBlockHardness(world, sidePos);
                        if (!(sideHardness > centerHardness) && sideHardness != -1.0F) {
                            BlockDropCaptureHelper.startCapturing();

                            try {
                                BlockHelper.breakBlock(world, player, sidePos, true, true);
                            } finally {
                                BlockDropCaptureHelper.getCapturedStacksAndStop().forEach((entity) -> {
                                    Block.spawnAsEntity(world, entity.getPosition(), entity.getItem());
                                });
                            }
                        }
                    }
                }
            });
        }
    }
}
