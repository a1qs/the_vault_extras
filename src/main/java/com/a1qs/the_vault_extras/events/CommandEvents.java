package com.a1qs.the_vault_extras.events;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.commands.GetPlayerLevel;
import com.a1qs.the_vault_extras.commands.ModifyFavourDataCommand;
import com.a1qs.the_vault_extras.commands.SetGlobalCrystalDifficultyCommand;
import com.a1qs.the_vault_extras.commands.SetGlobalDifficultyCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = VaultExtras.MOD_ID)
public class CommandEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new SetGlobalDifficultyCommand(event.getDispatcher());
        new SetGlobalCrystalDifficultyCommand(event.getDispatcher());
        new GetPlayerLevel(event.getDispatcher());
        new ModifyFavourDataCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }
}
