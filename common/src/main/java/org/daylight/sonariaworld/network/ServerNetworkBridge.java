package org.daylight.sonariaworld.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.network.payload.MorphSyncPayload;

public interface ServerNetworkBridge {
    void syncMorph(Player player, MorphSyncPayload payload);
    void sendToPlayersTrackingEntityAndSelf(Player player, CustomPacketPayload payload);
}
