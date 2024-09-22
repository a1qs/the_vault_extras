package com.a1qs.the_vault_extras.events;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.vault.DeathMessages;
import iskallia.vault.Vault;
import iskallia.vault.world.data.PlayerFavourData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = VaultExtras.MOD_ID)
public class OnPlayerVaultDeathEvent {

    private static final Random random = new Random();

    @SubscribeEvent
    public static void onVaultDeath (LivingDeathEvent event){
        if(event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            World world = player.getEntityWorld();
            if (world.getDimensionKey() == Vault.VAULT_KEY) {
                switch(random.nextInt(4)) {
                    case 0:
                        player.sendMessage(DeathMessages.deathMessage(PlayerFavourData.VaultGodType.BENEVOLENT), Util.DUMMY_UUID);
                        break;
                    case 1:
                        player.sendMessage(DeathMessages.deathMessage(PlayerFavourData.VaultGodType.MALEVOLENCE), Util.DUMMY_UUID);
                        break;
                    case 2:
                        player.sendMessage(DeathMessages.deathMessage(PlayerFavourData.VaultGodType.OMNISCIENT), Util.DUMMY_UUID);
                        break;
                    case 3:
                        player.sendMessage(DeathMessages.deathMessage(PlayerFavourData.VaultGodType.TIMEKEEPER), Util.DUMMY_UUID);
                        break;
                 }
            }
        }
    }
}
