package org.daylight.sonariaworld.network;

import net.minecraft.resources.Identifier;

public class NetworkIds {
    public static final String MOD_ID = "examplemod";

    public static final Identifier MORPH_REQUEST =
            Identifier.fromNamespaceAndPath(MOD_ID, "morph_request");

    public static final Identifier MORPH_SYNC =
            Identifier.fromNamespaceAndPath(MOD_ID, "morph_sync");

    private NetworkIds() {

    }
}
