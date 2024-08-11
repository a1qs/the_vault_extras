package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.world.data.generated.ChallengeCrystalArchive;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = ChallengeCrystalArchive.class, remap = false)
public class MixinChallengeCrystalArchive {

    @Shadow
    @Final
    private static final List<ItemStack> generatedCrystals = new ArrayList<>();

    @Inject(method = "initialize", at = @At("RETURN"))
    private static void addAdditionalCrystals(CallbackInfo ci) {
        // By Josh
        CrystalData swarm = InvokerChallengeCrystalArchive.invokeBaseData();
        swarm.setType(CrystalData.Type.CLASSIC);
        swarm.setSelectedObjective((VaultRaid.SUMMON_AND_KILL_BOSS.get()).getId());
        swarm.setTargetObjectiveCount(6);
        swarm.addModifier("Antlike");
        swarm.addModifier("Frenzier");
        swarm.addModifier("Hunt");
        swarm.addModifier("Phoenix");
        swarm.addModifier("Free Hugs");
        swarm.addModifier("Indestructible");
        swarm.addModifier("Nihility");
        generatedCrystals.add(InvokerChallengeCrystalArchive.invokeMake(swarm));

        // By Zing
        CrystalData gravedigger = InvokerChallengeCrystalArchive.invokeBaseData();
        gravedigger.setType(CrystalData.Type.CLASSIC);
        gravedigger.setSelectedObjective((VaultRaid.SUMMON_AND_KILL_BOSS.get()).getId());
        gravedigger.setTargetObjectiveCount(10);
        gravedigger.addGuaranteedRoom("graves", 500);
        gravedigger.addModifier("Abundance");
        gravedigger.addModifier("Abundance");
        gravedigger.addModifier("Abundance");
        gravedigger.addModifier("Chaotic");
        gravedigger.addModifier("Forbidden Knowledge");
        generatedCrystals.add(InvokerChallengeCrystalArchive.invokeMake(gravedigger));
    }

    /**
     * @author a1qs
     * @reason make crystals not challenge
     */
    @Overwrite
    private static CrystalData baseData() {
        CrystalData data = new CrystalData();
        data.setModifiable(false);
        data.setCanTriggerInfluences(false);
        data.setCanGenerateTreasureRooms(false);
        data.setPreventsRandomModifiers(true);
        //data.setChallenge(true);
        return data;
    }

}
