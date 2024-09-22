package com.a1qs.the_vault_extras.block.tileentity;

import com.a1qs.the_vault_extras.block.DecayedCrystallizer;
import com.a1qs.the_vault_extras.init.ModItems;
import com.a1qs.the_vault_extras.init.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.jetbrains.annotations.Nullable;

public class DecayedCrystallizerTile extends TileEntity implements ITickableTileEntity {
    private int cooldown = 0;
    private final int cooldownTime = 3600;
    private ItemStack displayedStack = new ItemStack(iskallia.vault.init.ModItems.VAULT_CRYSTAL);

    public DecayedCrystallizerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public DecayedCrystallizerTile() {
        this(ModTileEntities.DECAYED_CRYSTALLIZER_TILE.get());
    }

    @Override
    public void tick() {
        if (cooldown != 0) {
            cooldown--;

            double cooldownProgress = (double) cooldown / cooldownTime;

            if (cooldownProgress >= 0.75) {
                updateDisplaystack(cooldownProgress);
            } else if (cooldownProgress >= 0.5) {
                updateDisplaystack(cooldownProgress);
            } else if (cooldownProgress >= 0.25) {
                updateDisplaystack(cooldownProgress);
            } else {
                updateDisplaystack(cooldownProgress);
            }

            boolean onCooldown = (cooldown > 0);
            if (this.getBlockState().get(DecayedCrystallizer.ONCOOLDOWN) != onCooldown) {
                getWorld().setBlockState(this.pos, this.getBlockState().with(DecayedCrystallizer.ONCOOLDOWN, onCooldown));
            }
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        displayedStack = ItemStack.read(nbt.getCompound("DisplayedItem"));
        this.cooldown = nbt.getInt("cooldown");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("DisplayedItem", displayedStack.write(new CompoundNBT()));
        compound.putInt("cooldown", this.cooldown);

        return compound;
    }


    public void updateDisplaystack(double completion) {
        ItemStack displayStack = new ItemStack(ModItems.INCOMPLETE_CRYSTAL.get());
        if (completion >= 0.75) {
            setDisplayStack(displayStack);
        } else if (completion >= 0.5) {
            displayStack.getOrCreateTag().putFloat("ChargeLevel", 1);
            setDisplayStack(displayStack);
        } else if (completion >= 0.25) {
            displayStack.getOrCreateTag().putInt("ChargeLevel", 2);
            setDisplayStack(displayStack);
        } else if (completion > 0.0) {
            displayStack.getOrCreateTag().putFloat("ChargeLevel", 3);
            setDisplayStack(displayStack);
        } else if (completion == 0.0) {
            setDisplayStack(new ItemStack(iskallia.vault.init.ModItems.VAULT_CRYSTAL));
        }
    }

    public void setDisplayStack(ItemStack newStack) {
        displayedStack = newStack;
        if(!getWorld().isRemote) {
            getWorld().notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), 3);
            markDirty();
        }
    }

    public ItemStack getDisplayStack() {
        return displayedStack;
    }
    public int getCooldownTime() {
        return cooldownTime;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getCooldown() {
        return cooldown;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        // Write the current block entity state to NBT to be sent to the client
        CompoundNBT nbtTag = new CompoundNBT();
        this.write(nbtTag);  // Assuming 'write' serializes your block entity data to NBT
        return new SUpdateTileEntityPacket(this.pos, 1, nbtTag); // Creates the packet
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        // Read the updated state from NBT when receiving the packet client-side
        CompoundNBT nbtTag = pkt.getNbtCompound();
        this.read(this.getBlockState(), nbtTag);  // Assuming 'read' deserializes the block entity data from NBT
        getWorld().markBlockRangeForRenderUpdate(this.pos, this.getBlockState(), this.getBlockState());
    }
}
