package org.daylight.sonariaworld;

public class ModOptions {
    private static PlayerIdType playerIdType = PlayerIdType.UUID;

    public static PlayerIdType getPlayerIdType() {
        return playerIdType;
    }

    public static void setPlayerIdType(PlayerIdType playerIdType) {
        ModOptions.playerIdType = playerIdType;
    }

    public enum PlayerIdType {
        UUID,
        USERNAME
    }
}
