package org.daylight.sonariaworld.neoforge.network.handler.server;

import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.daylight.sonariaworld.data.GhostCreatureManager;
import org.daylight.sonariaworld.data.ServerPlayerManager;
import org.daylight.sonariaworld.data.ServerPlayerState;
import org.daylight.sonariaworld.network.payload.MorphRotationDistributionPayload;
import org.daylight.sonariaworld.network.payload.MorphRotationRequestPayload;

public final class ServerMorphRotationHandler {
    private ServerMorphRotationHandler() {}

    public static void handle(
            MorphRotationRequestPayload payload,
            IPayloadContext context
    ) {
        ServerPlayer player = (ServerPlayer) context.player();
        // player.level().getServer()

        context.enqueueWork(() -> {
            ServerPlayerState serverState = ServerPlayerManager.get(player);
            ServerPlayerState.CreatureGhostInfo ghostInfo = serverState.getGhostInfo();

            ghostInfo.setYaw(payload.yaw());
            ghostInfo.setHeadYaw(payload.headYaw());
            ghostInfo.setPitch(payload.pitch());
            ghostInfo.setRotationInitialized(true);
            ghostInfo.setDirty(true);
            ghostInfo.updateHitboxes();

            GhostCreatureManager.syncGhostRotation(
                    GhostCreatureManager.get(player),
                    player
            );

//            System.out.println("Distributing rotation");
            player.level().getChunkSource().sendToTrackingPlayers(
                    player,
                    new ClientboundCustomPayloadPacket(new MorphRotationDistributionPayload(
                            payload.playerId(),
                            payload.yaw(),
                            payload.pitch(),
                            payload.headYaw()
                    ))
            );
        });
    }
}
