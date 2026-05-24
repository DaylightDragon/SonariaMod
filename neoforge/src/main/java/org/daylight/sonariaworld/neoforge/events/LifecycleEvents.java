package org.daylight.sonariaworld.neoforge.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.daylight.sonariaworld.neoforge.data.ClientDataCleanupNeoForge;
import org.daylight.sonariaworld.neoforge.data.ServerDataCleanupNeoForge;

public class LifecycleEvents {
    @SubscribeEvent
    public static void onDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientDataCleanupNeoForge.cleanup();
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        ServerDataCleanupNeoForge.cleanup();
    }
}
