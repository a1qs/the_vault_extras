package com.a1qs.the_vault_extras.block.tileentity;

import com.a1qs.the_vault_extras.data.recipes.VaultRecyclerRecipe;
import com.a1qs.the_vault_extras.init.ModRecipeTypes;
import com.a1qs.the_vault_extras.init.ModTileEntities;
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

public class VaultRecyclerTile extends TileEntity /*implements ITickableTileEntity*/ {

    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private int smeltTime;
    private int smeltTimeTotal = 40;

    public VaultRecyclerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public VaultRecyclerTile() {
        this(ModTileEntities.VAULT_RECYCLER_TILE.get());
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        this.smeltTime = nbt.getInt("SmeltTime");
        this.smeltTimeTotal = nbt.getInt("SmeltTimeTotal");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("inv", itemHandler.serializeNBT());
        compound.putInt("SmeltTime", this.smeltTime);
        compound.putInt("SmeltTime", this.smeltTimeTotal);

        return compound;
    }
    /*
    public void tick() {
        if(world.isRemote) {
            return;
        }
        smelt();
    }

    public void smelt() {
        Inventory inv = new Inventory(itemHandler.getSlots());

        Optional<VaultRecyclerRecipe> recipe = world.getRecipeManager()
                .getRecipe(ModRecipeTypes.RECYCLER_RECIPE, inv, world);

        recipe.ifPresent(iRecipe -> {
            System.out.println("IT EXISTS IM JUST MADGE!");
            ItemStack output = iRecipe.getRecipeOutput();
            int smeltTime = iRecipe.getSmeltTime();
            int smeltTimeTotal = 20;
            ++smeltTime;

            if(smeltTime == smeltTimeTotal) {
                smeltTime = 0;
                itemHandler.extractItem(0, 1, false);
                itemHandler.insertItem(1, output, false);

            }
            markDirty();
        });
    }*/

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
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
