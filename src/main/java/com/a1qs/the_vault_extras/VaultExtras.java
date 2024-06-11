package com.a1qs.the_vault_extras;

import com.a1qs.the_vault_extras.events.PlayerLogOutEvent;
import com.a1qs.the_vault_extras.events.PlayerTabNameEvent;
import com.a1qs.the_vault_extras.init.*;
import com.a1qs.the_vault_extras.network.VaultExtrasNetwork;
import com.a1qs.the_vault_extras.events.ModSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
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

// The value here should match an entry in the META-INF/mods.toml file
@Mod(VaultExtras.MOD_ID)
public class VaultExtras
{
    public static final String MOD_ID = "the_vault_extras";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public VaultExtras() {
        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModSoundEvents.register(eventBus);
        ModRecipeTypes.register(eventBus);


        eventBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        eventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        eventBus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        eventBus.addListener(this::clientSetup);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(PlayerTabNameEvent::onTabListNameFormat);
        MinecraftForge.EVENT_BUS.addListener(PlayerTabNameEvent::onTick);
        MinecraftForge.EVENT_BUS.addListener(PlayerLogOutEvent::removeFromPartyUponLogout);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        VaultExtrasNetwork.init();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ModKeyBinds.register(event);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        //communicate to curios to register a slot
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
                () -> SlotTypePreset.CURIO.getMessageBuilder().build());
    }

    private void processIMC(final InterModProcessEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
        }

        @SubscribeEvent
        public static void onEffectRegister(RegistryEvent.Register<Effect> event) {
            ModEffect.register(event);
        }
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation("the_vault_extras", name);
    }
}
