package org.daylight.sonariaworld.data.systems;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.daylight.sonariaworld.Services;
import org.daylight.sonariaworld.SonariaWorld;
import org.daylight.sonariaworld.mixinrelated.IdHolder;
import org.daylight.sonariaworld.morph.MorphState;
import org.daylight.sonariaworld.morph.MorphStateService;
import org.daylight.sonariaworld.network.payload.MorphSyncPayload;

/***
 * Server-only
 */
public class MorphRequestsManager {
    public static void morphPlayer(ServerPlayer player, Identifier entityId, int variant) {
        if (!BuiltInRegistries.ENTITY_TYPE.containsKey(entityId)) {
            return;
        }

        MorphStateService.setMorph(
                player,
                BuiltInRegistries.ENTITY_TYPE.getValue(entityId),
                entityId,
                variant
        );

        GhostCreatureManager.get(player);

        sync(player);
    }

    public static void unmorphPlayer(ServerPlayer player) {
        MorphStateService.clearMorph(player);
        GhostCreatureManager.clearGhost(player);
        sync(player);
    }

    public static void sync(ServerPlayer player) {
        MorphState state = MorphStateService.get(player);

        MorphSyncPayload syncPayload = new MorphSyncPayload(
                ((IdHolder)player).sonaria$getId(),
                state.isMorphed() ? state.getEntityIdentifier() : Identifier.fromNamespaceAndPath(SonariaWorld.MOD_ID, "none"),
                state.getVariant(),
                state.isMorphed()
        );

        Services.SERVER_NETWORK.syncMorph(player, syncPayload);

        player.refreshDimensions();
        player.setBoundingBox(player.getBoundingBox());
    }
}
