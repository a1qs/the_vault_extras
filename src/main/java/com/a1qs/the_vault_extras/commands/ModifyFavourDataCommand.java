package com.a1qs.the_vault_extras.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.world.data.PlayerFavourData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.server.command.EnumArgument;

public class ModifyFavourDataCommand {
    public ModifyFavourDataCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("the_vault")
                .requires(sender -> sender.hasPermissionLevel(this.getRequiredPermissionLevel()))
                .then(Commands.literal("debug")
                        .then(Commands.literal("addFavour")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .then(Commands.argument("godType", EnumArgument.enumArgument(PlayerFavourData.VaultGodType.class))
                                                .then(Commands.argument("amount", IntegerArgumentType.integer(-16, 16))
                                                        .executes(this::modifyFavour)
                                                )
                                        )
                                )
                        )
                )
        );
    }
    public int modifyFavour(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = EntityArgument.getPlayer(context, "target");
        int amount = IntegerArgumentType.getInteger(context, "amount");
        PlayerFavourData.VaultGodType vaultGod = context.getArgument("godType", PlayerFavourData.VaultGodType.class);
        PlayerFavourData data = PlayerFavourData.get(target.getServerWorld());
        data.addFavour(target, vaultGod, amount);
        context.getSource().sendFeedback(new TranslationTextComponent("command.the_vault_extras.setFavour.success", amount, target.getDisplayName()), true);
        return 0;
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }
}
