package com.a1qs.the_vault_extras;

import com.a1qs.the_vault_extras.config.VaultExtrasConfig;
import com.a1qs.the_vault_extras.events.ModSounds;
import com.a1qs.the_vault_extras.events.PlayerEvents;
import com.a1qs.the_vault_extras.events.PlayerLogOutEvent;
import com.a1qs.the_vault_extras.events.PlayerTabNameEvent;
import com.a1qs.the_vault_extras.init.*;
import com.a1qs.the_vault_extras.item.paxel.AdvancedHammerEnhancement;
import com.a1qs.the_vault_extras.item.paxel.PaxelRegistry;
import com.a1qs.the_vault_extras.network.VaultExtrasNetwork;
import com.a1qs.the_vault_extras.screen.VaultRecyclerScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod(VaultExtras.MOD_ID)
public class VaultExtras {
    public static final String MOD_ID = "the_vault_extras";
    public static final Logger LOGGER = LogManager.getLogger();

    public VaultExtras() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModConfigs.register();
        ModConfigs.registerCompressionConfigs();
        ModSounds.register(eventBus);
        ModRecipeTypes.register(eventBus);
        ModTileEntities.register(eventBus);
        ModContainers.register(eventBus);
        ModParticles.register(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::enqueueIMC);
        eventBus.addListener(this::processIMC);
        eventBus.addListener(this::clientSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, VaultExtrasConfig.SPEC, "vaultextras-common.toml");

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(PlayerTabNameEvent::onTabListNameFormat);
        MinecraftForge.EVENT_BUS.addListener(PlayerTabNameEvent::onTick);
        MinecraftForge.EVENT_BUS.addListener(PlayerLogOutEvent::removeFromPartyUponLogout);
        MinecraftForge.EVENT_BUS.addListener(PlayerEvents::onPlayerTick);
        MinecraftForge.EVENT_BUS.addListener(AdvancedHammerEnhancement::onBlockMined);
    }

    private void setup(final FMLCommonSetupEvent event) {
        VaultExtrasNetwork.init();
        ModGameRules.initialize();
        PaxelRegistry.registerEnhancements();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() ->
                ScreenManager.registerFactory(ModContainers.VAULT_RECYCLER_CONTAINER.get(), VaultRecyclerScreen::new));
        ModKeyBinds.register(event);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // Communicate to Curios API to add a slot type of: CURIO
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
                () -> SlotTypePreset.CURIO.getMessageBuilder().build());
    }

    private void processIMC(final InterModProcessEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
    }


    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onEffectRegister(RegistryEvent.Register<Effect> event) {
            ModEffect.register(event);
        }
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation("the_vault_extras", name);
    }
}
