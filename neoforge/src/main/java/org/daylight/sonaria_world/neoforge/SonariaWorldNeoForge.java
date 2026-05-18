package org.daylight.sonaria_world.neoforge;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.daylight.sonaria_world.SonariaWorld;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(SonariaWorld.MOD_ID)
public final class SonariaWorldNeoForge {
    private static final Logger LOGGER = LogUtils.getLogger();

    public SonariaWorldNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        SonariaWorld.init();
        // lifecycle/mod events
        modEventBus.addListener(this::commonSetup);
        // gameplay events
        NeoForge.EVENT_BUS.register(GameEvents.class);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }
}
