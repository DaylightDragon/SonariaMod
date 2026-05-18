package org.daylight.sonaria_world.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import org.daylight.sonaria_world.SonariaWorld;

@EventBusSubscriber(
        modid = SonariaWorld.MOD_ID,
        value = Dist.CLIENT
)
public class ClientEvents {
    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre<?> event) {
        System.out.println("Render");
    }
}
