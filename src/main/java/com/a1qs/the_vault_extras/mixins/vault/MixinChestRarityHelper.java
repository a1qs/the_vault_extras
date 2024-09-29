package com.a1qs.the_vault_extras.mixins.vault;

import com.a1qs.the_vault_extras.init.ModEffect;
import iskallia.vault.attribute.VAttribute;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.item.gear.VaultGearHelper;
import iskallia.vault.skill.set.DreamSet;
import iskallia.vault.skill.set.SetNode;
import iskallia.vault.skill.set.SetTree;
import iskallia.vault.skill.set.TreasureSet;
import iskallia.vault.util.calc.ChestRarityHelper;
import iskallia.vault.world.data.PlayerSetsData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.influence.VaultAttributeInfluence;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Iterator;

@Mixin(value = ChestRarityHelper.class, remap = false)
public class MixinChestRarityHelper {

    /**
     * @author a1qs
     * @reason add chest rarity calculation with the modifier
     */
    @Overwrite
    public static float getIncreasedChestRarity(ServerPlayerEntity sPlayer) {
        float increasedRarity = 0.0F;
        increasedRarity += VaultGearHelper.getAttributeValueOnGearSumFloat(sPlayer, new VAttribute[]{ModAttributes.CHEST_RARITY});
        SetTree sets = PlayerSetsData.get(sPlayer.getServerWorld()).getSets(sPlayer);
        Iterator var3 = sets.getNodes().iterator();

        while(var3.hasNext()) {
            SetNode<?> node = (SetNode)var3.next();
            if (node.getSet() instanceof TreasureSet) {
                TreasureSet set = (TreasureSet)node.getSet();
                increasedRarity += set.getIncreasedChestRarity();
            }

            if (node.getSet() instanceof DreamSet) {
                DreamSet set = (DreamSet)node.getSet();
                increasedRarity += set.getIncreasedChestRarity();
            }
        }

        if(sPlayer.getActivePotionEffect(ModEffect.EXTRA_CHEST_RARITY) != null) {
            int amplifier = sPlayer.getActivePotionEffect(ModEffect.EXTRA_CHEST_RARITY).getAmplifier();
            increasedRarity += (1+amplifier) * 0.50F;
        }

        VaultRaid vault = VaultRaidData.get(sPlayer.getServerWorld()).getActiveFor(sPlayer);
        if (vault != null) {
            Iterator var7 = vault.getInfluences().getInfluences(VaultAttributeInfluence.class).iterator();

            VaultAttributeInfluence influence;
            while(var7.hasNext()) {
                influence = (VaultAttributeInfluence)var7.next();
                if (influence.getType() == VaultAttributeInfluence.Type.CHEST_RARITY && !influence.isMultiplicative()) {
                    increasedRarity += influence.getValue();
                }
            }

            var7 = vault.getInfluences().getInfluences(VaultAttributeInfluence.class).iterator();

            while(var7.hasNext()) {
                influence = (VaultAttributeInfluence)var7.next();
                if (influence.getType() == VaultAttributeInfluence.Type.CHEST_RARITY && influence.isMultiplicative()) {
                    increasedRarity *= influence.getValue();
                }
            }
        }

        return increasedRarity;
    }
}
