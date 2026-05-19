package org.daylight.sonariaworld.neoforge.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import org.daylight.sonariaworld.network.ClientNetworkBridge;
import org.daylight.sonariaworld.network.ServerNetworkBridge;
import org.daylight.sonariaworld.network.payload.MorphRequestPayload;
import org.daylight.sonariaworld.network.payload.MorphSyncPayload;

public final class NeoForgeServerNetworkBridge implements ServerNetworkBridge {
    @Override
    public void syncMorph(ServerPlayer player, MorphSyncPayload payload) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, payload);
    }
}
