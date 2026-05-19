package org.daylight.sonariaworld.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.daylight.sonariaworld.SonariaWorld;
import org.daylight.sonariaworld.client.SonariaWorldClient;

@EventBusSubscriber(modid = SonariaWorld.MOD_ID, value = Dist.CLIENT)
public final class SonariaWorldNeoForgeClient {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        SonariaWorldClient.registerRenderers(event::registerEntityRenderer, event::registerBlockEntityRenderer);
    }
}
