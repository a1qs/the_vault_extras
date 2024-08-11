package com.a1qs.the_vault_extras.item.paxel;

import com.a1qs.the_vault_extras.init.ModEffect;
import com.a1qs.the_vault_extras.mixins.InvokerPaxelEnhancements;
import iskallia.vault.item.paxel.enhancement.EffectEnhancement;
import iskallia.vault.item.paxel.enhancement.FortuneEnhancement;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancement;
import net.minecraft.potion.Effects;

public class PaxelRegistry {
    public static PaxelEnhancement ADVANCED_HAMMER;
    public static PaxelEnhancement FORTUNATE;
    public static PaxelEnhancement LUCKY;
    public static PaxelEnhancement LUCKIER;
    public static PaxelEnhancement OPULENT;
    public static PaxelEnhancement EXPLOSIVE;
    public static PaxelEnhancement REACHING;
    public static PaxelEnhancement ADVANCED_REACHING;


    public static void registerEnhancements() {
        ADVANCED_HAMMER = InvokerPaxelEnhancements.invokeRegister("advanced_hammer", new AdvancedHammerEnhancement());
        FORTUNATE = InvokerPaxelEnhancements.invokeRegister("advanced_fortune", new FortuneEnhancement(1));
        LUCKY = InvokerPaxelEnhancements.invokeRegister("lucky", new EffectEnhancement(Effects.LUCK, 1));
        LUCKIER = InvokerPaxelEnhancements.invokeRegister("luckier", new EffectEnhancement(Effects.LUCK, 2));
        OPULENT = InvokerPaxelEnhancements.invokeRegister("opulent", new EffectEnhancement(ModEffect.EXTRA_CHEST_RARITY, 1));
        EXPLOSIVE = InvokerPaxelEnhancements.invokeRegister("explosive", new ExplodingEnhancement());
        REACHING = InvokerPaxelEnhancements.invokeRegister("reaching", new ReachingEnhancement(2.5F));
        ADVANCED_REACHING = InvokerPaxelEnhancements.invokeRegister("advanced_reaching", new ReachingEnhancement(5.0F));
    }

}
