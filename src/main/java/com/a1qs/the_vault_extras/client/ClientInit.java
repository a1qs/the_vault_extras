package com.a1qs.the_vault_extras.client;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.init.ModItems;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientInit {
    public static void registerItemProperites() {
        ItemModelsProperties.registerProperty(ModItems.INCOMPLETE_CRYSTAL.get(), new ResourceLocation(VaultExtras.MOD_ID, "charge"),
                (stack, clientWorld, entity) -> {
                    if (stack.hasTag() && stack.getTag().contains("ChargeLevel")) {
                        return stack.getTag().getInt("ChargeLevel");
                    }
                    return 0;
                });
    }
}
