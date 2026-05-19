package org.daylight.sonariaworld.network;

import net.minecraft.server.level.ServerPlayer;
import org.daylight.sonariaworld.network.payload.MorphSyncPayload;

public interface ServerNetworkBridge {
    void syncMorph(ServerPlayer player, MorphSyncPayload payload);
}
