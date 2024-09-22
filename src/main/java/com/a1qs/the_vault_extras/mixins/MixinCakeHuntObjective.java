package com.a1qs.the_vault_extras.mixins;

import com.a1qs.the_vault_extras.init.ModItems;
import iskallia.vault.config.VaultModifiersConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.CakeHuntObjective;
import iskallia.vault.world.vault.modifier.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

import static iskallia.vault.world.vault.gen.layout.JigsawPoolProvider.rand;
import static iskallia.vault.world.vault.logic.objective.CakeHuntObjective.PENALTY;

@Mixin(value = CakeHuntObjective.class, remap = false)
public class MixinCakeHuntObjective {


    @Shadow private float healthPenalty;
    @Shadow private int cakeCount;
    @Shadow private int maxCakeCount;
    @Shadow private float modifierChance;
    @Shadow private VaultModifiersConfig.ModifierPoolType poolType;

    @Inject(method = "<init>", at = @At(value= "TAIL"))
    private void init(ResourceLocation id, CallbackInfo ci) {
        this.maxCakeCount = 15 + rand.nextInt(32);
        this.modifierChance = 1.0F;
    }

    @Inject(method = "addSpecialLoot", at = @At(value="TAIL"))
    protected void addCakeSealToCakeLoot(ServerWorld world, VaultRaid vault, LootContext context, NonNullList<ItemStack> stacks, CallbackInfo ci) {
        stacks.add(new ItemStack(ModItems.CAKE_SEAL.get()));
    }

    /**
     * @author JoshWannaPaas
     * @reason Remove Catalyst Modifiers from Cake Adds (Experimental)
     */
    @Overwrite
    private void addRandomModifier(VaultRaid vault, ServerWorld sWorld, ServerPlayerEntity player) {
        if (this.healthPenalty != 0.0F) {
            vault.getPlayers().stream().map(p -> p.getServerPlayer(sWorld.getServer())).filter(Optional::isPresent).map(Optional::get).forEach(other -> {
                ModifiableAttributeInstance attribute = other.getAttribute(Attributes.MAX_HEALTH);
                if (attribute != null) {
                    attribute.removeModifier(PENALTY);
                    double amount = ((float)this.cakeCount * this.healthPenalty);
                    amount = Math.min(amount, attribute.getValue() - 2.0);
                    AttributeModifier modifierx = new AttributeModifier(PENALTY, "Cake Health Penalty", -amount, AttributeModifier.Operation.ADDITION);
                    attribute.applyNonPersistentModifier(modifierx);
                }
            });
        }

        if (!(sWorld.getRandom().nextFloat() >= this.modifierChance)) {
            int level = vault.getProperties().getValue(VaultRaid.LEVEL);
            Set<VaultModifier> modifiers = ModConfigs.VAULT_MODIFIERS.getRandom(rand, level, this.poolType, null);
            modifiers.removeIf(mod -> mod instanceof NoExitModifier);
            modifiers.removeIf(mod -> mod instanceof TimerModifier);
            modifiers.removeIf(mod -> mod instanceof InventoryRestoreModifier);
            modifiers.removeIf(mod -> mod instanceof CatalystChanceModifier);
            if (sWorld.getRandom().nextFloat() < 0.65F) {
                modifiers.removeIf(mod -> mod instanceof ArtifactChanceModifier);
            }

            List<VaultModifier> modifierList = new ArrayList<>(modifiers);
            Collections.shuffle(modifierList);
            VaultModifier modifier = MiscUtils.getRandomEntry(modifierList, rand);
            if (modifier != null) {
                ITextComponent c0 = player.getDisplayName().deepCopy().mergeStyle(TextFormatting.LIGHT_PURPLE);
                ITextComponent c1 = new StringTextComponent(" found a ").mergeStyle(TextFormatting.GRAY);
                ITextComponent c2 = new StringTextComponent("cake").mergeStyle(TextFormatting.GREEN);
                ITextComponent c3 = new StringTextComponent(" and added ").mergeStyle(TextFormatting.GRAY);
                ITextComponent c4 = modifier.getNameComponent();
                ITextComponent c5 = new StringTextComponent(".").mergeStyle(TextFormatting.GRAY);
                ITextComponent ct = new StringTextComponent("")
                        .appendSibling(c0)
                        .appendSibling(c1)
                        .appendSibling(c2)
                        .appendSibling(c3)
                        .appendSibling(c4)
                        .appendSibling(c5);
                vault.getModifiers().addPermanentModifier(modifier);
                vault.getPlayers().forEach(vPlayer -> {
                    modifier.apply(vault, vPlayer, sWorld, sWorld.getRandom());
                    vPlayer.runIfPresent(sWorld.getServer(), sPlayer -> {
                        sPlayer.sendMessage(ct, Util.DUMMY_UUID);
                        sPlayer.getEntityWorld().playSound(null, sPlayer.getPosX(), sPlayer.getPosY(), sPlayer.getPosZ(), SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 0.6F, 1.0F);
                    });
                });
            }
        }
    }
}
