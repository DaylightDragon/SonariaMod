package org.daylight.sonariaworld.network;

import net.minecraft.server.level.ServerPlayer;
import org.daylight.sonariaworld.network.payload.MorphRequestPayload;
import org.daylight.sonariaworld.network.payload.MorphSyncPayload;

public interface ClientNetworkBridge {
    void sendMorphRequest(MorphRequestPayload payload);
}
