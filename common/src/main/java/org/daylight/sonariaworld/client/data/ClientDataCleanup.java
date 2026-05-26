package org.daylight.sonariaworld.client.data;

import org.daylight.sonariaworld.morph.ClientMorphManager;
import org.daylight.sonariaworld.morph.MorphStateService;
import org.daylight.sonariaworld.util.PlayerLookup;

public class ClientDataCleanup {
    public static void cleanup() {
        ClientState.setMovementMode(ClientState.MovementMode.PHYSICAL);
        ClientState.setClientSmoothAnimationActive(false);

        MorphStateService.fullCleanup();
        ClientMorphManager.fullCleanup();
        PlayerLookup.clear();
    }
}
