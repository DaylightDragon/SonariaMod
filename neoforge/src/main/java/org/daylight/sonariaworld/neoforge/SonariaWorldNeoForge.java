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
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.daylight.sonariaworld.Services;
import org.daylight.sonariaworld.SonariaWorld;
import net.neoforged.fml.common.Mod;
import org.daylight.sonariaworld.neoforge.events.ClientEvents;
import org.daylight.sonariaworld.neoforge.events.GhostPositionSyncListener;
import org.daylight.sonariaworld.neoforge.events.LifecycleEvents;
import org.daylight.sonariaworld.neoforge.keybinds.KeyHandler;
import org.daylight.sonariaworld.neoforge.network.NeoForgeClientNetworkBridge;
import org.daylight.sonariaworld.neoforge.network.NeoForgeNetwork;
import org.daylight.sonariaworld.neoforge.network.NeoForgeServerNetworkBridge;
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
        modEventBus.addListener(NeoForgeNetwork::register);
        modEventBus.addListener(KeyHandler::registerKeyMappings);
        NeoForge.EVENT_BUS.register(ClientEvents.class);
        NeoForge.EVENT_BUS.register(GhostPositionSyncListener.class);
        // gameplay events
        NeoForge.EVENT_BUS.register(KeyHandler.class);
        NeoForge.EVENT_BUS.register(LifecycleEvents.class);

        SOUND_EVENTS.register(modEventBus);
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        ENTITIES.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
        ITEMS.register(modEventBus);
        modEventBus.<EntityAttributeCreationEvent>addListener(event -> EntityRegistry.registerEntityAttributes(event::put));

        Services.CLIENT_NETWORK = new NeoForgeClientNetworkBridge();
        Services.SERVER_NETWORK = new NeoForgeServerNetworkBridge();

        SonariaWorld.doRegistrations();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        EntityRegistry.registerHitboxPresets();
        SonariaWorld.initPostRegistering();
    }
}
