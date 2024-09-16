package com.a1qs.the_vault_extras.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class PillarBlock extends Block {
    public static final BooleanProperty ABOVE = BooleanProperty.create("above");
    public static final BooleanProperty BELOW = BooleanProperty.create("below");
    public static final EnumProperty<Direction.Axis> AXIS;

    public PillarBlock(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(AXIS, Direction.Axis.Y));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        BlockState swapped = state.with(ABOVE, !state.get(ABOVE)).with(BELOW, !state.get(BELOW));
        BlockState state1;
        switch (rotation) {
            case NONE:
                state1 = state;
                return state1;
            case CLOCKWISE_90:
                switch (state.get(AXIS)) {
                    case X:
                        state1 = swapped.with(AXIS, Direction.Axis.Z);
                        return state1;
                    case Y:
                        state1 = state;
                        return state1;
                    case Z:
                        state1 = state.with(AXIS, Direction.Axis.X);
                        return state1;
                    default:
                        throw new IncompatibleClassChangeError();
                }
            case CLOCKWISE_180:
                switch (state.get(AXIS)) {
                    case X:
                    case Z:
                        state1 = swapped;
                        return state1;
                    case Y:
                        state1 = state;
                        return state1;
                    default:
                        throw new IncompatibleClassChangeError();
                }
            case COUNTERCLOCKWISE_90:
                switch (state.get(AXIS)) {
                    case X:
                        state1 = state.with(AXIS, Direction.Axis.Z);
                        return state1;
                    case Y:
                        state1 = state;
                        return state1;
                    case Z:
                        state1 = swapped.with(AXIS, Direction.Axis.X);
                        return state1;
                    default:
                        throw new IncompatibleClassChangeError();
                }
            default:
                throw new IncompatibleClassChangeError();
        }
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        BlockState swapped = state.with(ABOVE, !state.get(ABOVE)).with(BELOW, !state.get(BELOW));
        BlockState result;
        switch (mirror) {
            case NONE:
                result = state;
                break;
            case LEFT_RIGHT:
                result = state.get(AXIS) == Direction.Axis.Z ? swapped : state;
                break;
            case FRONT_BACK:
                result = state.get(AXIS) == Direction.Axis.X ? swapped : state;
                break;
            default:
                throw new IncompatibleClassChangeError();
        }
        return result;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AXIS, ABOVE, BELOW);
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction direction, BlockState neighborState, IWorld world, BlockPos currentPos, BlockPos neighborPos) {
        if (state.hasProperty(AXIS)) {
            return this.updateConnecting(world, currentPos, state, state.get(AXIS));
        }
        return super.updatePostPlacement(state, direction, neighborState, world, currentPos, neighborPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IBlockReader blockReader = context.getWorld();
        BlockPos pos = context.getPos();
        Direction.Axis axis = context.getFace().getAxis();
        return this.updateConnecting(blockReader, pos, super.getStateForPlacement(context), axis);
    }

    protected BlockState updateConnecting(IBlockReader world, BlockPos pos, BlockState state, Direction.Axis axis) {
        BlockState above;
        BlockState below;
        switch (axis) {
            case Y:
                above = world.getBlockState(pos.up());
                below = world.getBlockState(pos.down());
                break;
            case Z:
                above = world.getBlockState(pos.north());
                below = world.getBlockState(pos.south());
                break;
            default:
                above = world.getBlockState(pos.east());
                below = world.getBlockState(pos.west());
        }
        return state.with(ABOVE, above.getBlock() == this && above.hasProperty(AXIS) && above.get(AXIS) == axis)
                .with(BELOW, below.getBlock() == this && below.hasProperty(AXIS) && below.get(AXIS) == axis)
                .with(AXIS, axis);
    }

    static {
        AXIS = BlockStateProperties.AXIS;
    }
}
