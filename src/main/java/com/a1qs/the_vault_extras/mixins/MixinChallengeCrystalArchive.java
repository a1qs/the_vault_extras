package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.world.data.generated.ChallengeCrystalArchive;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = ChallengeCrystalArchive.class, remap = false)
public class MixinChallengeCrystalArchive {

    @Shadow
    @Final
    private static final List<ItemStack> generatedCrystals = new ArrayList<>();

    @Inject(method = "initialize", at = @At("RETURN"))
    private static void addAdditionalCrystals(CallbackInfo ci) {
        CrystalData custom = InvokerChallengeCrystalArchive.invokeBaseData();
        custom.setType(CrystalData.Type.CLASSIC);
        custom.setSelectedObjective((VaultRaid.SUMMON_AND_KILL_BOSS.get()).getId());
        custom.setTargetObjectiveCount(6);
        custom.addModifier("Treasure");
        custom.addModifier("Treasure");
        custom.addModifier("Treasure");
        custom.addModifier("Safe Zone");
        custom.addModifier("Locked");
        custom.addModifier("Rush");
        custom.addModifier("Rush");
        generatedCrystals.add(InvokerChallengeCrystalArchive.invokeMake(custom));
    }
}
