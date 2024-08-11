package com.a1qs.the_vault_extras.item.paxel;

import com.a1qs.the_vault_extras.mixins.InvokerPaxelEnhancements;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancement;

public class PaxelRegistry {
    public static PaxelEnhancement ADVANCED_HAMMER;
    public static void registerEnhancements() {
        ADVANCED_HAMMER = InvokerPaxelEnhancements.invokeRegister("advanced_hammer", new AdvancedHammerEnhancement());
    }

}
