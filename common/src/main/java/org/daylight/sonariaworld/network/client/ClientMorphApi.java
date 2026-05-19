package org.daylight.sonariaworld.network.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.daylight.sonariaworld.Services;
import org.daylight.sonariaworld.network.payload.MorphRequestPayload;

public final class ClientMorphApi {
    private ClientMorphApi() {

    }

    public static void requestMorph(
            Identifier entityId,
            int variant
    ) {
        if (Minecraft.getInstance().player == null) {
            return;
        }

        Services.CLIENT_NETWORK.sendMorphRequest(
                new MorphRequestPayload(entityId, variant)
        );
    }
}
