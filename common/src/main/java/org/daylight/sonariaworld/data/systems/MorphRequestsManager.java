package org.daylight.sonariaworld.data.systems;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.daylight.sonariaworld.SonariaWorld;
import org.daylight.sonariaworld.mixinrelated.IdHolder;
import org.daylight.sonariaworld.morph.MorphService;
import org.daylight.sonariaworld.network.payload.MorphSyncPayload;

public class MorphRequestsManager {
    public static void morphPlayer(ServerPlayer player, Identifier entityId, int variant) {
        if (!BuiltInRegistries.ENTITY_TYPE.containsKey(entityId)) {
            return;
        }

        MorphService.setMorph(
                player,
                BuiltInRegistries.ENTITY_TYPE.getValue(entityId),
                entityId,
                variant
        );

        GhostCreatureManager.get(player);

        player.refreshDimensions();
    }

    public static void unmorphPlayer(ServerPlayer player) {
        MorphService.clearMorph(player);
        GhostCreatureManager.clearGhost(player);
    }
}
