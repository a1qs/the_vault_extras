package com.a1qs.the_vault_extras.block.tileentity;

import com.a1qs.the_vault_extras.data.recipes.RecyclerRecipe;
import com.a1qs.the_vault_extras.init.ModRecipeTypes;
import com.a1qs.the_vault_extras.init.ModTileEntities;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.VaultMagnetItem;
import iskallia.vault.item.gear.EtchingItem;
import iskallia.vault.item.gear.VaultGear;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;

public class VaultRecyclerTile extends TileEntity implements ITickableTileEntity {

    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private int smeltTime;
    private int smeltTimeTotal;
    private final Random random = new Random();

    public VaultRecyclerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }


    public int getSmeltTime() {
        return smeltTime;
    }

    public VaultRecyclerTile() {
        this(ModTileEntities.VAULT_RECYCLER_TILE.get());
    }

    @Override
    public void read(@NotNull BlockState state, @NotNull CompoundNBT nbt) {
        super.read(state, nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        this.smeltTime = nbt.getInt("smeltTime");
    }

    @Override
    public @NotNull CompoundNBT write(@NotNull CompoundNBT compound) {
        super.write(compound);
        compound.put("inv", itemHandler.serializeNBT());
        compound.putInt("smeltTime", this.smeltTime);

        return compound;
    }

    public void tick() {
        if(world == null) return;
        if(world.isRemote()) return;

        Inventory inv = new Inventory(itemHandler.getSlots());

        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setInventorySlotContents(i, itemHandler.getStackInSlot(i));
        }

        ItemStack stack = inv.getStackInSlot(0);
        if(!stack.isEmpty()) {
            Optional<RecyclerRecipe> recipe = world.getRecipeManager()
                    .getRecipe(ModRecipeTypes.RECYCLER_RECIPE, inv, world);
            recipe.ifPresent(iRecipe -> {
                ItemStack output = iRecipe.getRecipeOutput();
                ItemStack extraOutput = iRecipe.getExtraOutput();
                float extraChance = iRecipe.getExtraChance();
                smeltTimeTotal = iRecipe.getSmeltTime();
                smeltItem(output, extraOutput, extraChance);
            });
        } else {
            smeltTime = 0;
        }

    }

    private void smeltItem(ItemStack output, ItemStack extraOutput, float chance) {
        // if the stack in the inventory  is 63 or above, don't freaking do it !!
        if(world == null) return;

        if(!(itemHandler.getStackInSlot(1).getCount() >= itemHandler.getSlotLimit(1)-1) && !(itemHandler.getStackInSlot(2).getCount() >= itemHandler.getSlotLimit(2)-1)) {
            smeltTime++;
            if(smeltTime >= smeltTimeTotal) {
                smeltTime = 0;
                itemHandler.extractItem(0, 1, false);
                itemHandler.insertItem(1, output, false);
                assert this.world != null;
                this.world.playSound(null, this.getPos(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 0.5F + (new Random()).nextFloat() * 0.25F, 0.75F + (new Random()).nextFloat() * 0.25F);

                if(world.isRemote) spawnRecycleParticles(this.getPos());



                if(random.nextFloat() < chance && !output.getStack().isEmpty()) {
                    itemHandler.insertItem(2, extraOutput, false);
                }

                markDirty();
            }
        } else {
            smeltTime = 0;
        }
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            // make vault scrap/etching frags/magnetite/pog valid for output slots
            // make vault gear valid for input slots
            // allows users to input vault scrap into the output slots, make a pr if it bothers you
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                if (slot == 1 || slot == 2 || slot == 3) {
                    return stack.getItem() == ModItems.VAULT_SCRAP || stack.getItem() == ModItems.ETCHING_FRAGMENT || stack.getItem() == ModItems.MAGNETITE || stack.getItem() == ModItems.POG;
                }
                if (slot == 0) {
                    return stack.getItem() instanceof VaultGear || stack.getItem() instanceof VaultMagnetItem || stack.getItem() instanceof EtchingItem;
                }

                return false;
            }

            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                if(!isItemValid(slot, stack)) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    @OnlyIn(Dist.CLIENT)
    public static void spawnRecycleParticles(BlockPos pos) {
        World world = Minecraft.getInstance().world;
        if (world != null) {
            int i;
            Random random;
            Vector3d offset;
            for(i = 0; i < 4; ++i) {
                random = world.getRandom();
                offset = new Vector3d(random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1), 0.0, random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1));
                world.addParticle(ParticleTypes.LAVA, true, (double)pos.getX() + 0.5 + offset.x, (double)pos.getY() + random.nextDouble() * 0.15000000596046448 + 0.25, (double)pos.getZ() + 0.5 + offset.z, offset.x / 2.0, random.nextDouble() * 0.1, offset.z / 2.0);
            }

            for(i = 0; i < 3; ++i) {
                random = world.getRandom();
                offset = new Vector3d(random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1), 0.0, random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1));
                world.addParticle(ParticleTypes.LAVA, true, (double)pos.getX() + 0.5 + offset.x, (double)pos.up().getY() + random.nextDouble() * 0.15000000596046448, (double)pos.getZ() + 0.5 + offset.z, offset.x / 20.0, random.nextDouble() * 0.2, offset.z / 20.0);
            }

            for(i = 0; i < 3; ++i) {
                random = world.getRandom();
                offset = new Vector3d(random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1), 0.0, random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1));
                world.addParticle(ParticleTypes.SMOKE, true, (double)pos.getX() + 0.5 + offset.x, (double)pos.up().getY() + random.nextDouble() * 0.15000000596046448, (double)pos.getZ() + 0.5 + offset.z, offset.x / 20.0, random.nextDouble() * 0.2, offset.z / 20.0);
            }

            for(i = 0; i < 3; ++i) {
                random = world.getRandom();
                offset = new Vector3d(random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1), 0.0, random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1));
                world.addParticle(ParticleTypes.LARGE_SMOKE, true, (double)pos.getX() + 0.5 + offset.x, (double)pos.up().getY() + random.nextDouble() * 0.15000000596046448, (double)pos.getZ() + 0.5 + offset.z, offset.x / 10.0, random.nextDouble() * 0.05, offset.z / 10.0);
            }

        }
    }

}
