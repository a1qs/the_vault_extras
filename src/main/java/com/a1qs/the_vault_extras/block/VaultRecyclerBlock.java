package com.a1qs.the_vault_extras.block;

import com.a1qs.the_vault_extras.block.tileentity.VaultRecyclerTile;
import com.a1qs.the_vault_extras.container.VaultRecyclerContainer;
import com.a1qs.the_vault_extras.init.ModTileEntities;
import com.a1qs.the_vault_extras.util.VoxelShapeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

public class VaultRecyclerBlock extends Block {
    public static final VoxelShape VAULT_RECYCLER_SHAPE;

    public VaultRecyclerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VAULT_RECYCLER_SHAPE;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        if(!worldIn.isRemote()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);

            if(tileEntity instanceof VaultRecyclerTile) {
                INamedContainerProvider containerProvider = createContainerProvider(worldIn, pos);

                NetworkHooks.openGui(((ServerPlayerEntity) player), containerProvider, tileEntity.getPos());
            } else {
                throw new IllegalStateException("Container Provider missing!");
            }
        }
        return ActionResultType.SUCCESS;
    }

    private INamedContainerProvider createContainerProvider(World worldIn, BlockPos pos) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("screen.vault_extras.vault_recycler");
            }

            @Nullable
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new VaultRecyclerContainer(i, worldIn, pos, playerInventory, playerEntity);
            }
        };
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.VAULT_RECYCLER_TILE.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.matchesBlock(newState.getBlock())) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            LazyOptional<IItemHandler> c = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
            RecipeWrapper wr = new RecipeWrapper((IItemHandlerModifiable) c.orElseThrow(NullPointerException::new));
            InventoryHelper.dropInventoryItems(worldIn, pos, wr);

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }



    static {
        VoxelShape[] shape = {
                VoxelShapes.create(0.6875, 0.8125, 0, 1, 1, 1),
                VoxelShapes.create(0, 0.8125, 0, 0.3125, 1, 1),
                VoxelShapes.create(0.3125, 0.8125, 0, 0.6875, 1, 0.3125),
                VoxelShapes.create(0.3125, 0.8125, 0.6875, 0.6875, 1, 1),
                VoxelShapes.create(0.25, 0.5625, 0.25, 0.75, 0.75, 0.75),
                VoxelShapes.create(0.25, 0.1875, 0.25, 0.75, 0.375, 0.75),
                VoxelShapes.create(0.296875, 0.375, 0.296875, 0.359375, 0.5625, 0.359375),
                VoxelShapes.create(0.640625, 0.375, 0.296875, 0.703125, 0.5625, 0.359375),
                VoxelShapes.create(0.5625, 0.921875, 0.3125, 0.625, 0.984375, 0.6875),
                VoxelShapes.create(0.375, 0.921875, 0.3125, 0.4375, 0.984375, 0.6875),
                VoxelShapes.create(0.640625, 0.375, 0.640625, 0.703125, 0.5625, 0.703125),
                VoxelShapes.create(0.296875, 0.375, 0.640625, 0.359375, 0.5625, 0.703125),
                VoxelShapes.create(0.640625, 0.375, 0.46875, 0.703125, 0.5625, 0.53125),
                VoxelShapes.create(0.296875, 0.375, 0.46875, 0.359375, 0.5625, 0.53125),
                VoxelShapes.create(0.46875, 0.375, 0.640625, 0.53125, 0.5625, 0.703125),
                VoxelShapes.create(0.46875, 0.375, 0.296875, 0.53125, 0.5625, 0.359375),
                VoxelShapes.create(0.375, 0.375, 0.375, 0.625, 0.5625, 0.625),
                VoxelShapes.create(0.375, 0.71875, 0.375, 0.625, 0.90625, 0.625),
                VoxelShapes.create(0.1875, 0.75, 0.1875, 0.8125, 0.875, 0.8125),
                VoxelShapes.create(0.0625, 0, 0.0625, 0.9375, 0.125, 0.9375),
                VoxelShapes.create(0.1875, 0.0625, 0.1875, 0.8125, 0.1875, 0.8125),
                VoxelShapes.create(0.6875, 0.5, 0.6875, 0.9375, 0.9375, 0.9375),
                VoxelShapes.create(0.75, 0.4375, 0.75, 0.875, 0.5, 0.875),
                VoxelShapes.create(0.125, 0.4375, 0.75, 0.25, 0.5, 0.875),
                VoxelShapes.create(0.125, 0.4375, 0.125, 0.25, 0.5, 0.25),
                VoxelShapes.create(0.75, 0.4375, 0.125, 0.875, 0.5, 0.25),
                VoxelShapes.create(0.0625, 0.5, 0.6875, 0.3125, 0.9375, 0.9375),
                VoxelShapes.create(0.0625, 0.5, 0.0625, 0.3125, 0.9375, 0.3125),
                VoxelShapes.create(0.6875, 0.5, 0.0625, 0.9375, 0.9375, 0.3125),
        };
        VAULT_RECYCLER_SHAPE = VoxelShapeUtil.mergeVoxelShapes(shape);
    }
}
