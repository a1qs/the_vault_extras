package com.a1qs.the_vault_extras.mixins.vault;

import com.google.common.collect.Lists;
import iskallia.vault.util.NameProviderPublic;
import net.minecraftforge.common.UsernameCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = NameProviderPublic.class, remap = false)
public class MixinNameProviderPublic {

    @Unique
    private static final List<String> DEV_NAMES = Lists.newArrayList("KaptainWutax", "iGoodie", "jmilthedude", "Scalda", "Kumara22", "Goktwo", "Aolsen96", "Winter_Grave", "kimandjax", "Monni_21", "Starmute", "MukiTanuki", "RowanArtifex", "HellFirePvP", "Pau1_", "Douwsky", "pomodoko", "Damnsecci");
    @Unique
    private static final List<String> SMP_S2 = Lists.newArrayList("CaptainSparklez", "Stressmonster101", "CaptainPuffy", "AntonioAsh", "ItsFundy", "iskall85", "Tubbo_", "HBomb94", "5uppps", "X33N", "PeteZahHutt", "Seapeekay");
    @Unique
    private static final List<String> DEV_NAMES_VH2POINT5 = Lists.newArrayList("a1qs", "JoshWannaPaas", "monkyman640", "xD0R0x", "faewild_", "MELANCHOLIC88", "RMZing");

    /**
     * @author a1qs
     * @reason add additional dev names for vh2.5
     */
    @Overwrite
    public static List<String> getAllAvailableNames() {
        List<String> names = new ArrayList<>();
        names.addAll(DEV_NAMES_VH2POINT5);
        names.addAll(DEV_NAMES);
        names.addAll(SMP_S2);
        names.addAll(getKnownUsernames());
        return names;
    }

    @Unique
    private static List<String> getKnownUsernames() {
        return new ArrayList<>(UsernameCache.getMap().values());
    }

}
