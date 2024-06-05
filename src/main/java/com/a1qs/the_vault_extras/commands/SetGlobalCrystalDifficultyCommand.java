package com.a1qs.the_vault_extras.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import iskallia.vault.world.data.GlobalDifficultyData;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class SetGlobalCrystalDifficultyCommand {

    private static final String ERROR_MESSAGE_KEY = "command.the_vault_extras.difficulty.set";

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public SetGlobalCrystalDifficultyCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("the_vault")
                .requires(sender -> sender.hasPermissionLevel(this.getRequiredPermissionLevel()))
                .then(Commands.literal("debug")
                        .then(Commands.literal("setGlobalCrystalDifficulty")
                                .then(Commands.literal("TRIVIAL")
                                        .executes(this::setDifficultyTrivial))
                                .then(Commands.literal("CASUAL")
                                        .executes(this::setDifficultyCasual))
                                .then(Commands.literal("STANDARD")
                                        .executes(this::setDifficultyStandard))
                                .then(Commands.literal("HARD")
                                        .executes(this::setDifficultyHard))
                                .then(Commands.literal("HARD")
                                        .executes(this::setDifficultyHard))
                                .then(Commands.literal("EXTREME")
                                        .executes(this::setDifficultyExtreme))
                        )
                )
        );
    }

    // yadda yadda "inefficient" yadda yadda
    // I don't care, it works
    // go make a PR if it bothers you that much
    private int setDifficultyTrivial(CommandContext<CommandSource> context) {
        CommandSource source = (CommandSource)context.getSource();
        if(GlobalDifficultyData.get(source.getWorld()).getCrystalCost() == GlobalDifficultyData.Difficulty.TRIVIAL) {
            throw new CommandException(new TranslationTextComponent(ERROR_MESSAGE_KEY));
        }
        GlobalDifficultyData.get(source.getWorld()).setCrystalCost(GlobalDifficultyData.Difficulty.TRIVIAL);
        source.sendFeedback(new TranslationTextComponent("command.the_vault_extras.difficulty.trivial"), true);
        return 0;
    }

    private int setDifficultyCasual(CommandContext<CommandSource> context) {
        CommandSource source = (CommandSource)context.getSource();
        if(GlobalDifficultyData.get(source.getWorld()).getCrystalCost() == GlobalDifficultyData.Difficulty.CASUAL) {
            throw new CommandException(new TranslationTextComponent(ERROR_MESSAGE_KEY));
        }
        GlobalDifficultyData.get(source.getWorld()).setCrystalCost(GlobalDifficultyData.Difficulty.CASUAL);
        source.sendFeedback(new TranslationTextComponent("command.the_vault_extras.difficulty.casual"), true);
        return 0;
    }

    private int setDifficultyStandard(CommandContext<CommandSource> context) {
        CommandSource source = (CommandSource)context.getSource();
        if(GlobalDifficultyData.get(source.getWorld()).getCrystalCost() == GlobalDifficultyData.Difficulty.STANDARD) {
            throw new CommandException(new TranslationTextComponent(ERROR_MESSAGE_KEY));
        }
        GlobalDifficultyData.get(source.getWorld()).setCrystalCost(GlobalDifficultyData.Difficulty.STANDARD);
        source.sendFeedback(new TranslationTextComponent("command.the_vault_extras.difficulty.standard"), true);
        return 0;
    }

    private int setDifficultyHard(CommandContext<CommandSource> context) {
        CommandSource source = (CommandSource)context.getSource();
        if(GlobalDifficultyData.get(source.getWorld()).getCrystalCost() == GlobalDifficultyData.Difficulty.HARD) {
            throw new CommandException(new TranslationTextComponent(ERROR_MESSAGE_KEY));
        }
        GlobalDifficultyData.get(source.getWorld()).setCrystalCost(GlobalDifficultyData.Difficulty.HARD);
        source.sendFeedback(new TranslationTextComponent("command.the_vault_extras.difficulty.hard"), true);
        return 0;
    }

    private int setDifficultyExtreme(CommandContext<CommandSource> context) {
        CommandSource source = (CommandSource)context.getSource();
        if(GlobalDifficultyData.get(source.getWorld()).getCrystalCost() == GlobalDifficultyData.Difficulty.EXTREME) {
            throw new CommandException(new TranslationTextComponent(ERROR_MESSAGE_KEY));
        }
        GlobalDifficultyData.get(source.getWorld()).setCrystalCost(GlobalDifficultyData.Difficulty.EXTREME);
        source.sendFeedback(new TranslationTextComponent("command.the_vault_extras.difficulty.extreme"), true);
        return 0;
    }
}
