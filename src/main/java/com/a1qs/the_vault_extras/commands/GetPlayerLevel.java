package com.a1qs.the_vault_extras.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class GetPlayerLevel {

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public GetPlayerLevel(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("the_vault")
            .requires(sender -> sender.hasPermissionLevel(this.getRequiredPermissionLevel()))
            .then(Commands.literal("debug")
                    .then(Commands.literal("getPlayerLevel")
                            .then(Commands.argument("target", EntityArgument.player())
                                    .executes(this::getVaultLevel)
                            )
                    )
            )
        );
    }

    private int getVaultLevel(CommandContext<CommandSource> context) throws CommandSyntaxException {
        CommandSource source = context.getSource();
        ServerPlayerEntity target = EntityArgument.getPlayer(context, "target");

        int vaultLevel = PlayerVaultStatsData.get(target.getServerWorld()).getVaultStats(target).getVaultLevel();
        source.sendFeedback(new TranslationTextComponent("command.the_vault_extras.getplayerlevel.success", target.getDisplayName(), vaultLevel), true);


        // source.sendFeedback(new TranslationTextComponent("commands.give.success.single", count, itemIn.createStack(count, false).getTextComponent(), targets.iterator().next().getDisplayName()), true);
        return 0;
    }
}
