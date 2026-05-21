package org.daylight.sonariaworld.morph;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.Services;
import org.daylight.sonariaworld.network.payload.MorphSyncPayload;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MorphService {
    private static final Map<UUID, MorphState> STATES = new HashMap<>();

    private MorphService() {
    }

    public static MorphState get(Player player) {
        return STATES.computeIfAbsent(player.getUUID(), uuid -> new MorphState());
    }

    public static void setMorph(
            ServerPlayer player,
            Identifier entityId,
            int variant
    ) {
        MorphState state = get(player);

        state.setMorphed(true);
        state.setEntityId(entityId);
        state.setVariant(variant);

        player.refreshDimensions();

        sync(player);
    }

    public static void clearMorph(ServerPlayer player) {
        MorphState state = get(player);

        state.setMorphed(false);

        player.refreshDimensions();

        sync(player);
    }

    public static void sync(ServerPlayer player) {
        MorphState state = get(player);

        MorphSyncPayload payload = new MorphSyncPayload(
                player.getUUID(),
                state.getEntityId(),
                state.getVariant(),
                state.isMorphed()
        );

        Services.SERVER_NETWORK.syncMorph(player, payload);
    }
}
