package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.skill.ability.config.SummonEternalConfig;
import iskallia.vault.skill.ability.effect.SummonEternalAbility;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.archetype.CommanderTalent;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = SummonEternalAbility.class, remap = false)
public class MixinSummonEternalAbility<C extends SummonEternalConfig> {
    @ModifyVariable(method = "onAction(Liskallia/vault/skill/ability/config/SummonEternalConfig;Lnet/minecraft/entity/player/ServerPlayerEntity;Z)Z", at = @At("STORE"), ordinal = 1)
    private int injected(int eternalCap, C config, ServerPlayerEntity player, boolean active) {
        ServerWorld sWorld = (ServerWorld)player.getEntityWorld();
        TalentTree talents = PlayerTalentsData.get(sWorld).getTalents(player);

        //get the cost of the currently selected talent
        int talentCost = talents.getLearnedNodes(CommanderTalent.class).stream().mapToInt((node)
                -> node.getTalent().getCost()).sum();

        switch(talentCost) {
            case 5:
                return eternalCap+3;
            case 10:
                return eternalCap+4;
            case 20:
                return eternalCap+5;
        }
        return eternalCap;
    }
}
