package org.daylight.sonariaworld.data.systems;

import org.daylight.sonariaworld.util.PlayerLookup;

public class ServerDataCleanup {
    public static void cleanup() {
        GhostCreatureManager.fullCleanup();
        ServerPlayerManager.fullCleanup();
        PlayerLookup.clear();
    }
}
