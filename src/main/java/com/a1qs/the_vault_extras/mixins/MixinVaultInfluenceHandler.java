package com.a1qs.the_vault_extras.mixins;

import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.data.PlayerFavourData;
import iskallia.vault.world.data.VaultPartyData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.influence.*;
import iskallia.vault.world.vault.logic.VaultInfluenceHandler;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Mixin(value = VaultInfluenceHandler.class, remap = false)
public class MixinVaultInfluenceHandler{
    @Shadow
    private static final Random rand = new Random();

    /**
     * @author a1qs
     * @reason Make Influences work in Coop Vaults
     */
    @Overwrite
    public static void initializeInfluences(VaultRaid vault, ServerWorld world) {
        int vaultLvl = vault.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
        if (vaultLvl >= 50) {
            CrystalData data = vault.getProperties().getBase(VaultRaid.CRYSTAL_DATA).orElse(null);
            if (data != null && data.canTriggerInfluences() && data.getType().canTriggerInfluences()) {
                if (vault.getAllObjectives().stream().noneMatch(VaultObjective::preventsInfluences)) {
                    VaultInfluences influences = vault.getInfluences();
                    PlayerFavourData favourData = PlayerFavourData.get(world);
                    Map<PlayerFavourData.VaultGodType, Integer> positives = new HashMap<>();
                    Map<PlayerFavourData.VaultGodType, Integer> negatives = new HashMap<>();
                    PlayerFavourData.VaultGodType[] godTypes = PlayerFavourData.VaultGodType.values();

                    for (PlayerFavourData.VaultGodType type : godTypes) {
                        for (VaultPlayer vPlayer : vault.getPlayers()) {
                            Optional<VaultPartyData.Party> partyOpt = VaultPartyData.get(world).getParty(vPlayer.getPlayerId());

                            if (partyOpt.isPresent() && partyOpt.get().getMembers().size() > 1) {
                                VaultPartyData.Party party = partyOpt.get();
                                UUID leader = party.getLeader() != null ? party.getLeader() : MiscUtils.getRandomEntry(party.getMembers(), world.getRandom());
                                int favour = favourData.getFavour(leader, type);
                                if (Math.abs(favour) >= 4 && !(rand.nextFloat() >= 0.66F)) {
                                    if (favour < 0) {
                                        negatives.put(type, favour);
                                    } else {
                                        positives.put(type, favour);
                                    }
                                    break;
                                }
                            } else {
                                int favour = favourData.getFavour(vPlayer.getPlayerId(), type);
                                if (Math.abs(favour) >= 4 && !(rand.nextFloat() >= 0.66F)) {
                                    if (favour < 0) {
                                        negatives.put(type, favour);
                                    } else {
                                        positives.put(type, favour);
                                    }
                                    break;
                                }
                            }
                        }
                    }

                    try {
                        Method getPositiveInfluenceMethod = VaultInfluenceHandler.class.getDeclaredMethod("getPositiveInfluence", PlayerFavourData.VaultGodType.class, int.class);
                        Method getNegativeInfluenceMethod = VaultInfluenceHandler.class.getDeclaredMethod("getNegativeInfluence", PlayerFavourData.VaultGodType.class, int.class);

                        getNegativeInfluenceMethod.setAccessible(true);
                        getNegativeInfluenceMethod.setAccessible(true);

                        Field messagesField = VaultInfluenceHandler.class.getDeclaredField("messages");
                        messagesField.setAccessible(true);
                        Map<?, ?> messages = (Map<?, ?>) messagesField.get(null);

                        positives.forEach((typex, favourx) -> {
                            try {
                                Tuple<VaultInfluence, String> influenceResult = (Tuple<VaultInfluence, String>) getPositiveInfluenceMethod.invoke(vault, typex, Math.abs(favourx));
                                influences.addInfluence(influenceResult.getA(), vault, world);

                                Object influeceMessages = messages.get(typex);
                                Method getPositiveMessageMethod = influeceMessages.getClass().getDeclaredMethod("getPositiveMessage");
                                getPositiveMessageMethod.setAccessible(true);
                                String message = (String) getPositiveMessageMethod.invoke(influeceMessages);

                                IFormattableTextComponent vgName = (new StringTextComponent(typex.getName())).mergeStyle(typex.getChatColor());
                                vgName.modifyStyle((style) -> style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, typex.getHoverChatComponent())));
                                IFormattableTextComponent txt = new StringTextComponent("");
                                txt.appendSibling((new StringTextComponent("[VG] ")).mergeStyle(TextFormatting.DARK_PURPLE)).appendSibling(vgName).appendSibling((new StringTextComponent(": ")).mergeStyle(TextFormatting.WHITE)).appendSibling(new StringTextComponent(message));
                                IFormattableTextComponent info = (new StringTextComponent(influenceResult.getB())).mergeStyle(TextFormatting.DARK_GRAY);
                                vault.getPlayers().forEach((vPlayer) -> vPlayer.runIfPresent(world.getServer(), (sPlayer) -> {
                                    sPlayer.sendMessage(txt, Util.DUMMY_UUID);
                                    sPlayer.sendMessage(info, Util.DUMMY_UUID);
                                }));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        });

                        negatives.forEach((typex, favourx) -> {
                            try {
                                Tuple<VaultInfluence, String> influenceResult = (Tuple<VaultInfluence, String>) getNegativeInfluenceMethod.invoke(vault, typex, Math.abs(favourx));
                                influences.addInfluence(influenceResult.getA(), vault, world);

                                Object influeceMessages = messages.get(typex);
                                Method getNegativeMessageMethod = influeceMessages.getClass().getDeclaredMethod("getNegativeMessage");
                                getNegativeMessageMethod.setAccessible(true);
                                String message = (String) getNegativeMessageMethod.invoke(influeceMessages);

                                IFormattableTextComponent vgName = (new StringTextComponent(typex.getName())).mergeStyle(typex.getChatColor());
                                vgName.modifyStyle((style) -> style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, typex.getHoverChatComponent())));
                                IFormattableTextComponent txt = new StringTextComponent("");
                                txt.appendSibling((new StringTextComponent("[VG] ")).mergeStyle(TextFormatting.DARK_PURPLE)).appendSibling(vgName).appendSibling((new StringTextComponent(": ")).mergeStyle(TextFormatting.WHITE)).appendSibling(new StringTextComponent(message));
                                IFormattableTextComponent info = (new StringTextComponent(influenceResult.getB())).mergeStyle(TextFormatting.DARK_GRAY);
                                vault.getPlayers().forEach((vPlayer) -> vPlayer.runIfPresent(world.getServer(), (sPlayer) -> {
                                    sPlayer.sendMessage(txt, Util.DUMMY_UUID);
                                    sPlayer.sendMessage(info, Util.DUMMY_UUID);
                                }));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
