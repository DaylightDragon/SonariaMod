package org.daylight.sonariaworld.neoforge.network.handler.server;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.daylight.sonariaworld.data.systems.GhostCreatureManager;
import org.daylight.sonariaworld.data.systems.MorphRequestsManager;
import org.daylight.sonariaworld.mixinrelated.IdHolder;
import org.daylight.sonariaworld.morph.MorphService;
import org.daylight.sonariaworld.network.payload.MorphRequestPayload;
import org.daylight.sonariaworld.network.payload.MorphSyncPayload;

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
