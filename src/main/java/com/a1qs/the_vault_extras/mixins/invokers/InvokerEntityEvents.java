package com.a1qs.the_vault_extras.mixins.invokers;

import iskallia.vault.event.EntityEvents;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;

@Mixin(value = EntityEvents.class, remap = false)
public interface InvokerEntityEvents {
    @Invoker("addScavengerDrops")
    static boolean addScavengerDrops(World world, Entity killed, VaultRaid vault, Collection<ItemEntity> drops) {
        throw new AssertionError();
    }
    @Invoker("shouldDropDefaultInVault")
    static boolean shouldDropDefaultInVault(Entity entity) {
        throw new AssertionError();
    }

    @Invoker("addSubFighterDrops")
    static boolean addSubFighterDrops(World world, Entity killed, VaultRaid vault, Collection<ItemEntity> drops) {
        throw new AssertionError();
    }
}
