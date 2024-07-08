package com.a1qs.the_vault_extras.block.tileentity;

import com.a1qs.the_vault_extras.block.DecayedCrystallizer;
import com.a1qs.the_vault_extras.init.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class DecayedCrystallizerTile extends TileEntity implements ITickableTileEntity {
    private int cooldown = 0;

    public DecayedCrystallizerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public DecayedCrystallizerTile() {
        this(ModTileEntities.DECAYED_CRYSTALLIZER_TILE.get());
    }

    @Override
    public void tick() {
        if(cooldown != 0) {
            cooldown--;
            if(this.getCooldown() == 0) {
                getWorld().setBlockState(this.pos, this.getBlockState().with(DecayedCrystallizer.ONCOOLDOWN, false));
            } else {
                getWorld().setBlockState(this.pos, this.getBlockState().with(DecayedCrystallizer.ONCOOLDOWN, true));
            }
        }
    }


    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.cooldown = nbt.getInt("cooldown");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("cooldown", this.cooldown);

        return compound;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getCooldown() {
        return cooldown;
    }
}
