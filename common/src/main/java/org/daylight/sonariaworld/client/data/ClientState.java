package org.daylight.sonariaworld.client.data;

import lombok.Getter;

public class ClientState {
    public enum MovementMode {
        VANILLA,
        PHYSICAL
    }

    @Getter
    private static MovementMode movementMode = MovementMode.PHYSICAL;
    @Getter
    private static boolean clientSmoothAnimationActive = false;

    public static void setMovementMode(MovementMode movementMode) {
        ClientState.movementMode = movementMode;
    }

    public static void setClientSmoothAnimationActive(boolean clientSmoothAnimationActive) {
        ClientState.clientSmoothAnimationActive = clientSmoothAnimationActive;
    }
}
