package com.a1qs.the_vault_extras.block.tileentity;

import com.a1qs.the_vault_extras.init.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.Random;

public class SanctifiedPedestalTile extends TileEntity {

    private boolean isUsed = false;
    private static final Random rand = new Random();

    public SanctifiedPedestalTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public SanctifiedPedestalTile() {
        this(ModTileEntities.SANCTIFIED_PEDESTAL_TILE.get());
    }

    public boolean getUsed() {
        return this.isUsed;
    }

    public void setUsed(boolean used) {
        this.isUsed = used;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.isUsed = nbt.getBoolean("activated");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("activated", this.isUsed);

        return compound;
    }
}
