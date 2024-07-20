package com.a1qs.the_vault_extras.block;

import com.a1qs.the_vault_extras.block.tileentity.DecayedCrystallizerTile;
import com.a1qs.the_vault_extras.init.ModTileEntities;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class DecayedCrystallizer extends Block {
    private static final Random rand = new Random();
    public static final BooleanProperty ONCOOLDOWN = BooleanProperty.create("oncooldown");
    private static final VoxelShape shape = VoxelShapes.create(.2, .2, .2, .8, .8, .8);

    public DecayedCrystallizer(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(ONCOOLDOWN, Boolean.valueOf(false)));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return shape;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            ServerWorld serverWorld = (ServerWorld) worldIn;

            if(tileEntity instanceof DecayedCrystallizerTile) {
                DecayedCrystallizerTile tile = (DecayedCrystallizerTile) tileEntity;

                //Allow the player to move the block via crouch-rightclicking it
                if(player.isCrouching()) {
                    ItemStack itemStack = new ItemStack(this);
                    CompoundNBT tileNBT = tile.getTileData();
                    itemStack.getOrCreateTag().put("BlockEntityTag", tileNBT);
                    itemStack.getChildTag("BlockEntityTag").putInt("cooldown", tile.getCooldown());
                    itemStack.getChildTag("BlockEntityTag").putString("id", ModTileEntities.DECAYED_CRYSTALLIZER_TILE.getId().toString());
                    worldIn.removeBlock(pos, false);
                    worldIn.removeTileEntity(pos);
                    serverWorld.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack));
                }

                if(tile.getCooldown() == 0 && !player.isCrouching()) {
                    tile.setCooldown(3600);

                    Style customStyle = Style.EMPTY.setColor(Color.fromInt(10598235)).applyFormatting(TextFormatting.BOLD);
                    ItemStack crystal = new ItemStack(ModItems.VAULT_CRYSTAL);
                    CrystalData data = new CrystalData(crystal);
                    int level = PlayerVaultStatsData.get((ServerWorld)worldIn).getVaultStats(player).getVaultLevel();
                    data.setSelectedObjective(VaultRaid.SUMMON_AND_KILL_BOSS.get().getId());
                    data.setTargetObjectiveCount(11);
                    data.setCanTriggerInfluences(false);
                    data.setPreventsRandomModifiers(true);
                    data.addModifier("Very Unlucky");
                    data.addModifier("Hurry");
                    data.addModifier("No Extras");
                    crystal.setDisplayName(new StringTextComponent("Rotten Vault Crystal").setStyle(customStyle));
                    if(level < 50) {
                        data.addModifier("Beginner's Grace");
                        crystal.setDisplayName(new StringTextComponent("Beginner's Rotten Vault Crystal").setStyle(customStyle));
                    }
                    data.setModifiable(false);

                    RedstoneParticleData particleData = new RedstoneParticleData(0.0F, 1.0F, 0.0F, 1.0F);
                    for(int i = 0; i < 10; ++i) {
                        float offset = 0.1F * (float)i;
                        if (serverWorld.rand.nextFloat() < 0.5F) {
                            offset *= -1.0F;
                        }
                        serverWorld.spawnParticle(particleData, pos.getX() + 0.5, pos.getY() + 1.6, pos.getZ() + 0.5, 10, offset, offset, offset, 1.0);
                    }
                    serverWorld.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 0.7F, 1.5F);
                    serverWorld.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5, pos.getY() + 0.9, pos.getZ() + 0.5, crystal));

                } else {
                    player.sendStatusMessage(new StringTextComponent(
                            TextFormatting.RED + "" + TextFormatting.BOLD + "On cooldown for another " + TextFormatting.YELLOW + TextFormatting.BOLD + tile.getCooldown()/20 + " seconds"), true);
                }
            }
        }

        return ActionResultType.SUCCESS;
    }


    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.DECAYED_CRYSTALLIZER_TILE.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }



    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (!stateIn.get(ONCOOLDOWN)) {
            summonParticles(pos, worldIn, ParticleTypes.WITCH);
        } else {
            summonParticles(pos, worldIn, ParticleTypes.CRIT);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ONCOOLDOWN);
    }

    private void summonParticles(BlockPos pos, World worldIn, IParticleData particleType) {
        double d0 = (double)pos.getX() + rand.nextDouble();
        double d1 = (double)pos.getY() + rand.nextDouble();
        double d2 = (double)pos.getZ() + rand.nextDouble();
        double d3 = ((double)rand.nextFloat() - 0.5) * 0.5;
        double d4 = ((double)rand.nextFloat() - 0.5) * 0.5;
        double d5 = ((double)rand.nextFloat() - 0.5) * 0.5;
        int j = rand.nextInt(2) * 2 - 1;
        if (!worldIn.getBlockState(pos.west()).matchesBlock(this) && !worldIn.getBlockState(pos.east()).matchesBlock(this)) {
            d0 = (double)pos.getX() + 0.5 + 0.25 * (double)j;
            d3 = rand.nextFloat() * 2.0F * (float)j;
        } else {
            d2 = (double)pos.getZ() + 0.5 + 0.25 * (double)j;
            d5 = rand.nextFloat() * 2.0F * (float)j;
        }

        worldIn.addParticle(particleType, d0, d1, d2, d3, d4, d5);
    }
}
