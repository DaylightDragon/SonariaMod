package org.daylight.sonariaworld.morph;

import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.SonariaWorld;
import org.daylight.sonariaworld.mixinrelated.IdHolder;
import org.daylight.sonariaworld.network.payload.MorphSyncPayload;

import java.util.HashMap;
import java.util.Map;

/**
 * Client & Server
 */
public final class MorphStateService {
    private static final Map<String, MorphState> STATES = new HashMap<>();

    private MorphStateService() {
    }

    public static MorphState get(Player player) {
        return STATES.computeIfAbsent(((IdHolder)player).sonaria$getId(), id ->
                new MorphState()
                        .setPlayerId(((IdHolder) player).sonaria$getId())
        );
    }

    public static void setMorph(
            ServerPlayer player,
            EntityType<?> entityType,
            Identifier entityIdentifier,
            int variant
    ) {
        MorphState state = get(player);

        state.setMorphed(true);
        state.setEntityIdentifier(entityIdentifier);
        state.setVariant(variant);

        player.refreshDimensions();
    }

    public static void clearMorph(ServerPlayer player) {
        MorphState state = get(player);

        state.setMorphed(false);
        state.setEntityIdentifier(null);

        player.refreshDimensions();
    }

    public static void fullCleanup() {
        STATES.clear();
    }
}
