package com.a1qs.the_vault_extras.mixins;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.command.PartyCommand;
import iskallia.vault.world.data.VaultPartyData;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = PartyCommand.class, remap = false)
public class MixinPartyCommand {
    @Inject(method = "invite", at = @At("HEAD"), cancellable = true)
    private void createPartyOnInvite(CommandContext<CommandSource> ctx, CallbackInfoReturnable<Integer> cir) throws CommandSyntaxException {
        VaultPartyData data = VaultPartyData.get(((CommandSource)ctx.getSource()).getWorld());
        ServerPlayerEntity player = ((CommandSource)ctx.getSource()).asPlayer();
        Optional<VaultPartyData.Party> party = data.getParty(player.getUniqueID());
        if (!party.isPresent()) {
            this.create(ctx);
            party = data.getParty(player.getUniqueID());

        }
    }

        private int create(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
            VaultPartyData data = VaultPartyData.get(((CommandSource)ctx.getSource()).getWorld());
            ServerPlayerEntity player = ((CommandSource)ctx.getSource()).asPlayer();
            if (data.createParty(player.getUniqueID())) {
                player.sendMessage(new StringTextComponent("Successfully created a party.").mergeStyle(TextFormatting.GREEN), player.getUniqueID());
                VaultPartyData.broadcastPartyData(player.getServerWorld());
            } else {
                player.sendMessage(
                        new StringTextComponent("You are already in a party! Please leave or disband it first.").mergeStyle(TextFormatting.RED), player.getUniqueID()
                );
            }

            return 0;
        }
}
