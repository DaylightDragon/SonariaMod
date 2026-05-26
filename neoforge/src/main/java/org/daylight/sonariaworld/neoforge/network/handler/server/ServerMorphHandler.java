package org.daylight.sonariaworld.neoforge.network.handler.server;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.daylight.sonariaworld.data.systems.MorphRequestsManager;
import org.daylight.sonariaworld.network.payload.MorphRequestPayload;

public final class ServerMorphHandler {
    private ServerMorphHandler() {}

    public static void handle(
            MorphRequestPayload payload,
            IPayloadContext context
    ) {
        ServerPlayer player = (ServerPlayer) context.player();

        context.enqueueWork(() -> {
            MorphRequestsManager.morphPlayer(player, payload.entityId(), payload.variant());
        });
    }
}
