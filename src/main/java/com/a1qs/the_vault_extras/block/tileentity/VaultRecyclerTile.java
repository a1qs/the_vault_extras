package com.a1qs.the_vault_extras.block.tileentity;

import com.a1qs.the_vault_extras.init.ModRecipeTypes;
import com.a1qs.the_vault_extras.init.ModTileEntities;
import com.a1qs.the_vault_extras.data.recipes.RecyclerRecipe;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.gear.VaultGear;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
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
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        this.smeltTime = nbt.getInt("smeltTime");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("inv", itemHandler.serializeNBT());
        compound.putInt("smeltTime", this.smeltTime);

        return compound;
    }

    public void tick() {
        if(!world.isRemote) {
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
            }
        }
    }


    private void smeltItem(ItemStack output, ItemStack extraOutput, float chance) {
        // if the stack in the inventory  is 63 or above, don't freaking do it !!
        if(!(itemHandler.getStackInSlot(1).getCount() >= itemHandler.getSlotLimit(1)-1) && !(itemHandler.getStackInSlot(2).getCount() >= itemHandler.getSlotLimit(2)-1)) {
            smeltTime++;
            if(smeltTime >= smeltTimeTotal) {
                smeltTime = 0;
                itemHandler.extractItem(0, 1, false);
                itemHandler.insertItem(1, output, false);

                if(random.nextFloat() < chance && !output.getStack().isEmpty()) {
                    System.out.println(extraOutput);
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

            // make vault scrap valid for output slots
            // make vault gear valid for input slots
            // allows users to input vault scrap into the output slots, make a pr if it bothers you
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                if (slot == 1 || slot == 2 || slot == 3) {
                    return stack.getItem() == ModItems.VAULT_SCRAP;
                }
                if (slot == 0) {
                    return stack.getItem() instanceof VaultGear;

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

}
