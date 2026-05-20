package org.daylight.sonariaworld.neoforge.network.handler.server;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.daylight.sonariaworld.morph.MorphService;
import org.daylight.sonariaworld.network.payload.MorphRequestPayload;

public final class ServerMorphHandler {
    private ServerMorphHandler() {

    }

    public static void handle(
            MorphRequestPayload payload,
            IPayloadContext context
    ) {
        ServerPlayer player = (ServerPlayer) context.player();

        context.enqueueWork(() -> {
            Identifier entityId = payload.entityId();

            if (!BuiltInRegistries.ENTITY_TYPE.containsKey(entityId)) {
                return;
            }

            System.out.println("Making " + player.getName().getString() + " into " + entityId.toString());

            MorphService.setMorph(
                    player,
                    entityId,
                    payload.variant()
            );
        });
    }
}
