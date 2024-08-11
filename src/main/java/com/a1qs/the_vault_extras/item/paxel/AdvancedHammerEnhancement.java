package com.a1qs.the_vault_extras.item.paxel;

import iskallia.vault.event.ActiveFlags;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancement;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancements;
import iskallia.vault.util.BlockDropCaptureHelper;
import iskallia.vault.util.BlockHelper;

import java.util.LinkedList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.Color;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class AdvancedHammerEnhancement extends PaxelEnhancement {

    public Color getColor() {
        return Color.fromInt(-10042064);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onBlockMined(BlockEvent.BreakEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
        ItemStack heldStack = player.getHeldItemMainhand();
        if (PaxelEnhancements.getEnhancement(heldStack) instanceof AdvancedHammerEnhancement) {
            ServerWorld world = (ServerWorld)event.getWorld();
            BlockPos centerPos = event.getPos();
            Direction.Axis axis = calcBreakAxis(player, centerPos);
            List<BlockPos> sidePoses = breakPoses(centerPos, axis);
            BlockState centerState = world.getBlockState(centerPos);
            float centerHardness = centerState.getBlockHardness(world, centerPos);
            ActiveFlags.IS_AOE_MINING.runIfNotSet(() -> {

                for (BlockPos sidePos : sidePoses) {
                    BlockState state = world.getBlockState(sidePos);
                    if (!state.getBlock().isAir(state, world, sidePos)) {
                        float sideHardness = state.getBlockHardness(world, sidePos);
                        if (!(sideHardness > centerHardness) && sideHardness != -1.0F) {
                            BlockDropCaptureHelper.startCapturing();

                            try {
                                BlockHelper.breakBlock(world, player, sidePos, true, true);
                                BlockHelper.damageMiningItem(heldStack, player, 1);
                            } finally {
                                BlockDropCaptureHelper.getCapturedStacksAndStop().forEach((entity) -> Block.spawnAsEntity(world, entity.getPosition(), entity.getItem()));
                            }
                        }
                    }
                }

            });
        }
    }

    public static Direction.Axis calcBreakAxis(ServerPlayerEntity player, BlockPos blockPos) {
        Vector3d eyePosition = player.getEyePosition(1.0F);
        Vector3d look = player.getLook(1.0F);
        Vector3d endPos = eyePosition.add(look.x * 5.0, look.y * 5.0, look.z * 5.0);
        RayTraceContext rayTraceContext = new RayTraceContext(player.getEyePosition(1.0F), endPos, BlockMode.OUTLINE, FluidMode.NONE, player);
        BlockRayTraceResult result = player.world.rayTraceBlocks(rayTraceContext);
        return result.getFace().getAxis();
    }

    public static List<BlockPos> breakPoses(BlockPos blockPos, Direction.Axis axis) {
        List<BlockPos> poses = new LinkedList<>();
        if (axis == Axis.Y) {
            poses.add(blockPos.west());
            poses.add(blockPos.west().north());
            poses.add(blockPos.west().south());
            poses.add(blockPos.east());
            poses.add(blockPos.east().north());
            poses.add(blockPos.east().south());
            poses.add(blockPos.north());
            poses.add(blockPos.south());

            poses.add(blockPos.south(2));
            poses.add(blockPos.north(2));

            poses.add(blockPos.west().south(2));
            poses.add(blockPos.west().north(2));
            poses.add(blockPos.west(2));
            poses.add(blockPos.west(2).north());
            poses.add(blockPos.west(2).north(2));
            poses.add(blockPos.west(2).south());
            poses.add(blockPos.west(2).south(2));

            poses.add(blockPos.east().south(2));
            poses.add(blockPos.east().north(2));
            poses.add(blockPos.east(2));
            poses.add(blockPos.east(2).north());
            poses.add(blockPos.east(2).south());
            poses.add(blockPos.east(2).north(2));
            poses.add(blockPos.east(2).south(2));
        } else if (axis == Axis.X) {
            poses.add(blockPos.up());
            poses.add(blockPos.up().north());
            poses.add(blockPos.up().south());
            poses.add(blockPos.down());
            poses.add(blockPos.down().north());
            poses.add(blockPos.down().south());
            poses.add(blockPos.north());
            poses.add(blockPos.south());
            poses.add(blockPos.up().north(2));
            poses.add(blockPos.up().south(2));
            poses.add(blockPos.up(2));
            poses.add(blockPos.up(2).north());
            poses.add(blockPos.up(2).south());
            poses.add(blockPos.up(2).north(2));
            poses.add(blockPos.up(2).south(2));
            poses.add(blockPos.up(3));
            poses.add(blockPos.up(3).north());
            poses.add(blockPos.up(3).north(2));
            poses.add(blockPos.up(3).south());
            poses.add(blockPos.up(3).south(2));
            poses.add(blockPos.north(2));
            poses.add(blockPos.south(2));
            poses.add(blockPos.down().north(2));
            poses.add(blockPos.down().south(2));

        } else if (axis == Axis.Z) {
            poses.add(blockPos.up());
            poses.add(blockPos.up().west());
            poses.add(blockPos.up().east());
            poses.add(blockPos.down());
            poses.add(blockPos.down().west());
            poses.add(blockPos.down().east());
            poses.add(blockPos.west());
            poses.add(blockPos.east());
            poses.add(blockPos.up(2));
            poses.add(blockPos.up(2).west());
            poses.add(blockPos.up(2).west(2));
            poses.add(blockPos.up(2).east());
            poses.add(blockPos.up(2).east(2));
            poses.add(blockPos.up().west(2));
            poses.add(blockPos.up().east(2));
            poses.add(blockPos.west(3));
            poses.add(blockPos.east(3));
            poses.add(blockPos.down().west(2));
            poses.add(blockPos.down().east(2));
            poses.add(blockPos.down(2));
            poses.add(blockPos.down(2).east(1));
            poses.add(blockPos.down(2).east(2));
            poses.add(blockPos.down(2).west());
            poses.add(blockPos.down(2).west(2));
        }

        return poses;
    }
}

