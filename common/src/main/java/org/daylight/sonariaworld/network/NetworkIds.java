package org.daylight.sonariaworld.network;

import net.minecraft.resources.Identifier;
import org.daylight.sonariaworld.SonariaWorld;

public class NetworkIds {
    public static final String MOD_ID = SonariaWorld.MOD_ID;

    public static final Identifier MORPH_REQUEST =
            Identifier.fromNamespaceAndPath(MOD_ID, "morph_request");

    public static final Identifier MORPH_SYNC =
            Identifier.fromNamespaceAndPath(MOD_ID, "morph_sync");

    public static final Identifier MORPH_ROTATION_UPDATE_REQUEST =
            Identifier.fromNamespaceAndPath(MOD_ID, "morph_rotation_update_request"); // formal

    public static final Identifier MORPH_ROTATION_UPDATE_DISTRIBUTION =
            Identifier.fromNamespaceAndPath(MOD_ID, "morph_rotation_update_distribution");

    public static final Identifier PLAYER_ATTACK =
            Identifier.fromNamespaceAndPath(MOD_ID, "player_attack");

    private NetworkIds() {

    }
}
