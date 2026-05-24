package org.daylight.sonariaworld.neoforge.network.handler.server;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.daylight.sonariaworld.data.systems.GhostCreatureManager;
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
            Identifier entityId = payload.entityId();

            if (!BuiltInRegistries.ENTITY_TYPE.containsKey(entityId)) {
                return;
            }

//            System.out.println("Making " + player.getName().getString() + " into " + entityId.toString());

            MorphService.setMorph(
                    player,
                    BuiltInRegistries.ENTITY_TYPE.getValue(entityId),
                    entityId,
                    payload.variant()
            );

            GhostCreatureManager.get(player);

            MorphSyncPayload syncPayload = new MorphSyncPayload(
                    ((IdHolder)player).sonaria$getId(),
                    entityId,
                    payload.variant(),
                    true
            );

            // Отправляем самому игроку
            ((ServerPlayer) context.player()).connection.send(new ClientboundCustomPayloadPacket(syncPayload));

            // Отправляем всем tracking players
            player.level().getChunkSource().sendToTrackingPlayers(
                    player,
                    new ClientboundCustomPayloadPacket(syncPayload)
            );

            player.refreshDimensions();

            System.out.println("Set " + player.getName().getString() + " bounding box to " + player.getBoundingBox().toString());
        });
    }
}
