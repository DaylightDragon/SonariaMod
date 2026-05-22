package org.daylight.sonariaworld.client.data;

public class ClientState {
    public enum MovementMode {
        VANILLA,
        PHYSICAL
    }

    private static MovementMode movementMode = MovementMode.PHYSICAL;
    private static boolean clientSmoothAnimationActive = false;
//    private static float clientSmoothAnimationCurrentYaw = 0;
//    private static float clientSmoothAnimationTargetYaw = 0;
//    private static float clientVisualYaw = 0;
//    private static float clientVisualPrevYaw = 0;

//    public static float getClientVisualYaw() {
//        return clientVisualYaw;
//    }
//
//    public static void setClientVisualYaw(float clientVisualYaw) {
//        ClientState.clientVisualYaw = clientVisualYaw;
//    }
//
//    public static float getClientVisualPrevYaw() {
//        return clientVisualPrevYaw;
//    }
//
//    public static void setClientVisualPrevYaw(float clientVisualPrevYaw) {
//        ClientState.clientVisualPrevYaw = clientVisualPrevYaw;
//    }

    public static MovementMode getMovementMode() {
        return movementMode;
    }

    public static void setMovementMode(MovementMode movementMode) {
        ClientState.movementMode = movementMode;
    }

//    public static float getClientSmoothAnimationTargetYaw() {
//        return clientSmoothAnimationTargetYaw;
//    }
//
//    public static void setClientSmoothAnimationTargetYaw(float clientSmoothAnimationTargetYaw) {
//        ClientState.clientSmoothAnimationTargetYaw = clientSmoothAnimationTargetYaw;
//    }

    public static boolean isClientSmoothAnimationActive() {
        return clientSmoothAnimationActive;
    }

    public static void setClientSmoothAnimationActive(boolean clientSmoothAnimationActive) {
        ClientState.clientSmoothAnimationActive = clientSmoothAnimationActive;
    }

//    public static float getClientSmoothAnimationCurrentYaw() {
//        return clientSmoothAnimationCurrentYaw;
//    }
//
//    public static void setClientSmoothAnimationCurrentYaw(float clientSmoothAnimationCurrentYaw) {
//        ClientState.clientSmoothAnimationCurrentYaw = clientSmoothAnimationCurrentYaw;
//    }
}
