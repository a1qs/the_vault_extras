package com.a1qs.the_vault_extras.block;

import com.a1qs.the_vault_extras.block.tileentity.SanctifiedPedestalTile;
import com.a1qs.the_vault_extras.init.ModParticles;
import com.a1qs.the_vault_extras.init.ModTileEntities;
import iskallia.vault.attribute.EnumAttribute;
import iskallia.vault.attribute.IntegerAttribute;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.item.gear.IdolItem;
import iskallia.vault.item.gear.VaultGear;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;


public class SanctifiedPedestalBlock extends Block {

    public static final BooleanProperty USED = BooleanProperty.create("used");

    public SanctifiedPedestalBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(USED, Boolean.valueOf(false)));
    }

    //todo: write the state to the SanctifiedPedestalBlock#setUsed method
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote) {

            TileEntity tileEntity = worldIn.getTileEntity(pos);

            if(tileEntity instanceof SanctifiedPedestalTile) {
                SanctifiedPedestalTile pedestal = (SanctifiedPedestalTile) tileEntity;
                ItemStack stack = player.getHeldItemMainhand();
                if(stack.getItem() instanceof IdolItem) {
                    if(!state.get(USED)) {

                        Optional<EnumAttribute<VaultGear.State>> attribute = ModAttributes.GEAR_STATE.get(stack);
                        if (attribute.isPresent() && ((EnumAttribute)attribute.get()).getValue(stack) == VaultGear.State.IDENTIFIED) {
                            IntegerAttribute modifiersToRoll = ModAttributes.GEAR_MAX_LEVEL.get(stack).get();
                            if(modifiersToRoll.getValue(stack) <= 10) {
                                ModAttributes.GEAR_MAX_LEVEL.create(stack, modifiersToRoll.getValue(stack) + 2);
                                player.playSound(SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.PLAYERS, 1.0F, 0.75F);
                                player.spawnSweepParticles();

                                BlockState newState = state.with(USED, true);
                                worldIn.setBlockState(pos, newState, 3);
                                pedestal.setUsed(true);
                            } else {
                                player.playSound(SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.PLAYERS, 1.0F, 1.5F);
                                player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "The Pedestal lacks the power to enhance this Idol further."), true);
                            }
                        }
                    }
                } else if (!(stack.getItem() == Items.AIR) && !state.get(USED)) {
                    player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "This item doesnt seem to work.. perhaps I should try something god-related"), true);
                } else {
                    player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Nothing happened, maybe I should try using an item"), true);
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(USED);
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if(!stateIn.get(USED)) {
            if(0.35F < rand.nextFloat()) {
                worldIn.addParticle(ModParticles.WHITE_FLAME.get(), pos.getX() + 0.15D, pos.getY() + 1.1D, pos.getZ() + 0.15D, 0.0D, 0.01D, 0.0D);
                worldIn.addParticle(ModParticles.WHITE_FLAME.get(), pos.getX() + 0.85D, pos.getY() + 1.1D, pos.getZ() + 0.85D, 0.0D, 0.01D, 0.0D);
                worldIn.addParticle(ModParticles.WHITE_FLAME.get(), pos.getX() + 0.85D, pos.getY() + 1.1D, pos.getZ() + 0.15D, 0.0D, 0.01D, 0.0D);
                worldIn.addParticle(ModParticles.WHITE_FLAME.get(), pos.getX() + 0.15D, pos.getY() + 1.1D, pos.getZ() + 0.85D, 0.0D, 0.01D, 0.0D);
            }
        } else {
            worldIn.addParticle(RedstoneParticleData.REDSTONE_DUST, pos.getX() + 0.85D, pos.getY() + 1.1D, pos.getZ() + 0.85D, 0.0D, 0.01D, 0.0D);
            worldIn.addParticle(RedstoneParticleData.REDSTONE_DUST, pos.getX() + 0.85D, pos.getY() + 1.1D, pos.getZ() + 0.15D, 0.0D, 0.01D, 0.0D);
            worldIn.addParticle(RedstoneParticleData.REDSTONE_DUST, pos.getX() + 0.15D, pos.getY() + 1.1D, pos.getZ() + 0.85D, 0.0D, 0.01D, 0.0D);
            worldIn.addParticle(RedstoneParticleData.REDSTONE_DUST, pos.getX() + 0.15D, pos.getY() + 1.1D, pos.getZ() + 0.15D, 0.0D, 0.01D, 0.0D);
        }
    }


    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.SANCTIFIED_PEDESTAL_TILE.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}
