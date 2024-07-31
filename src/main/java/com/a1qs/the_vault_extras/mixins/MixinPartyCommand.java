package com.a1qs.the_vault_extras.mixins;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.command.PartyCommand;
import iskallia.vault.world.data.VaultPartyData;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = PartyCommand.class, remap = false)
public class MixinPartyCommand {
    @Inject(method = "invite", at = @At("HEAD"))
    private void createPartyOnInvite(CommandContext<CommandSource> ctx, CallbackInfoReturnable<Integer> cir) throws CommandSyntaxException {
        VaultPartyData data = VaultPartyData.get((ctx.getSource()).getWorld());
        ServerPlayerEntity player = (ctx.getSource()).asPlayer();
        Optional<VaultPartyData.Party> party = data.getParty(player.getUniqueID());
        if (!party.isPresent()) {
            ((InvokerPartyCommand) this).invokeCreate(ctx);
            party = data.getParty(player.getUniqueID());
        }
    }
}
