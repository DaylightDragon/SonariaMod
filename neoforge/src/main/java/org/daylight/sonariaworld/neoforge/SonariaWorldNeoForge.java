package org.daylight.sonariaworld.neoforge;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.daylight.sonariaworld.SonariaWorld;
import net.neoforged.fml.common.Mod;
import org.daylight.sonariaworld.neoforge.client.keybind.KeyHandler;
import org.daylight.sonariaworld.neoforge.network.ModNet;
import org.daylight.sonariaworld.neoforge.network.ToggleMorphPayload;
import org.daylight.sonariaworld.registry.EntityRegistry;
import org.slf4j.Logger;

@Mod(SonariaWorld.MOD_ID)
public final class SonariaWorldNeoForge {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, SonariaWorld.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, SonariaWorld.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SonariaWorld.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, SonariaWorld.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SonariaWorld.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, SonariaWorld.MOD_ID);

    public SonariaWorldNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        SonariaWorld.init();

        // lifecycle/mod events
        modEventBus.addListener(this::commonSetup);
        // gameplay events
//        modEventBus.addListener(this::registerPayloads);
        modEventBus.addListener(ModNet::register);
        NeoForge.EVENT_BUS.register(GameEvents.class);
        NeoForge.EVENT_BUS.register(KeyHandler.class);

        SOUND_EVENTS.register(modEventBus);
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        ENTITIES.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
        ITEMS.register(modEventBus);
        modEventBus.<EntityAttributeCreationEvent>addListener(event -> EntityRegistry.registerEntityAttributes(event::put));

        NeoForge.EVENT_BUS.addListener(GameEvents::onPlayerLoggedOut);
        SonariaWorld.doRegistrations();
    }

//    @SubscribeEvent
//    public void registerPayloads(RegisterPayloadHandlersEvent event) {
//        PayloadRegistrar registrar =
//                event.registrar("1");
//
//        registrar.playToServer(
//                ToggleMorphPayload.TYPE,
//                ToggleMorphPayload.STREAM_CODEC,
//                ModNet::handleToggleMorph
//        );
//    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }
}
