package com.a1qs.the_vault_extras.init;

import com.a1qs.the_vault_extras.VaultExtras;
import iskallia.vault.effect.BasicEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

import java.awt.*;


public class ModEffect {
    public static final Effect EXTRA_CHEST_RARITY;


    public ModEffect() {
    }

    public static void register(RegistryEvent.Register<Effect> event) {
        event.getRegistry().registerAll(new Effect[]{EXTRA_CHEST_RARITY});
    }

    static {
        EXTRA_CHEST_RARITY = new BasicEffect(EffectType.BENEFICIAL, Color.green.getRGB(), new ResourceLocation(VaultExtras.MOD_ID, "extra_chest_rarity"));
    }
}
