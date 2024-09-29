package com.a1qs.the_vault_extras.mixins.vault;

import iskallia.vault.block.base.FillableAltarTileEntity;
import iskallia.vault.block.entity.SoulAltarTileEntity;
import iskallia.vault.util.CodecUtils;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = SoulAltarTileEntity.class, remap = false)
public abstract class MixinSoulAltarTileEntity extends FillableAltarTileEntity {

    @Shadow private int ticksExisted;

    @Unique
    private static final AxisAlignedBB SEARCH_BOX = new AxisAlignedBB(-24.0, -24.0, -24.0, 24.0, 24.0, 24.0);

    public MixinSoulAltarTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    /**
     * @author a1qs
     * @reason higher radius for soul altars...
     */
    @Overwrite
    public void tick() {
        SoulAltarTileEntity thisInstance = ((SoulAltarTileEntity) (Object) this);
        super.tick();
        if (!thisInstance.getWorld().isRemote()) {
            ++this.ticksExisted;
            if (this.ticksExisted % 10 != 0) {
                return;
            }
            thisInstance.getWorld().getLoadedEntitiesWithinAABB(LivingEntity.class, SEARCH_BOX.offset(thisInstance.getPos()), (entity) -> {
                return entity.isAlive() && !entity.isSpectator() && !entity.isInvulnerable() && entity.getType().getClassification() == EntityClassification.MONSTER;
            }).forEach((entity) -> {
                if (entity.addTag("the_vault_SoulAltar")) {
                    CodecUtils.writeNBT(BlockPos.CODEC, thisInstance.getPos(), entity.getPersistentData(), "the_vault_SoulAltarPos");
                }
            });
        }
    }
}
