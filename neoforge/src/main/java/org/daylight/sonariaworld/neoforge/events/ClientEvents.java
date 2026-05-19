package org.daylight.sonariaworld.neoforge.events;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import org.daylight.sonariaworld.SonariaWorld;
import org.daylight.sonariaworld.neoforge.client.keybind.KeyHandler;

@EventBusSubscriber(modid = SonariaWorld.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre<?> event) {
//        System.out.println("Render");
    }

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(KeyHandler.TOGGLE_MORPH);
    }
}
