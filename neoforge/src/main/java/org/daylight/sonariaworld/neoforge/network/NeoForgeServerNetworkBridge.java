package org.daylight.sonariaworld.neoforge.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import org.daylight.sonariaworld.network.ClientNetworkBridge;
import org.daylight.sonariaworld.network.ServerNetworkBridge;
import org.daylight.sonariaworld.network.payload.MorphRequestPayload;
import org.daylight.sonariaworld.network.payload.MorphSyncPayload;

public final class NeoForgeServerNetworkBridge implements ServerNetworkBridge {
    @Override
    public void syncMorph(Player player, MorphSyncPayload payload) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, payload);
    }

    @Override
    public void sendToPlayersTrackingEntityAndSelf(Player player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, payload);
    }
}
