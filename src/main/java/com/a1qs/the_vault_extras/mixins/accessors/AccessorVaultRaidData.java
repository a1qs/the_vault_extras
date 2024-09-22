package com.a1qs.the_vault_extras.mixins.accessors;

import iskallia.vault.nbt.VListNBT;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = VaultRaidData.class, remap = false)
public interface AccessorVaultRaidData {
    @Accessor
    VListNBT<VaultRaid, CompoundNBT> getVaults();
}
