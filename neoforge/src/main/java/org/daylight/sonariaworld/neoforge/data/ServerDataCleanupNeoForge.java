package org.daylight.sonariaworld.neoforge.data;

import org.daylight.sonariaworld.data.systems.ServerDataCleanup;
import org.daylight.sonariaworld.data.systems.ServerSpatialSystems;
import org.daylight.sonariaworld.neoforge.events.GhostPositionSyncListener;
import org.daylight.sonariaworld.util.PlayerLookup;

public class ServerDataCleanupNeoForge {
    public static void cleanup() {
        ServerDataCleanup.cleanup();
        GhostPositionSyncListener.fullCleanup();
        ServerSpatialSystems.fullCleanup();
        PlayerLookup.clear();
    }
}
