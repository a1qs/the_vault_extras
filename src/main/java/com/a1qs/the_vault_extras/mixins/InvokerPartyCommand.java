package com.a1qs.the_vault_extras.mixins;

import com.mojang.brigadier.context.CommandContext;
import iskallia.vault.command.PartyCommand;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = PartyCommand.class, remap = false)
public interface InvokerPartyCommand {
    @Invoker("create")
    int invokeCreate(CommandContext<CommandSource> ctx);
}
