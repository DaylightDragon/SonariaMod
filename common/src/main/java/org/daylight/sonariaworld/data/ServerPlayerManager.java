package org.daylight.sonariaworld.data;

import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.mixinrelated.IdHolder;
import org.daylight.sonariaworld.morph.MorphState;

import java.util.HashMap;
import java.util.Map;

public class ServerPlayerManager {
    private static final Map<String, ServerPlayerState> STATES = new HashMap<>();

    private ServerPlayerManager() {}

    public static ServerPlayerState get(Player player) {
        return STATES.computeIfAbsent(((IdHolder)player).sonaria$getId(), id -> {
            ServerPlayerState state = new ServerPlayerState()
                    .setPlayerId(((IdHolder) player).sonaria$getId());
            state.getGhostInfo().setPlayerId(((IdHolder) player).sonaria$getId());

            return state;
        });
    }

    public static ServerPlayerState get(String playerId) {
        return STATES.computeIfAbsent(playerId, id -> {
            ServerPlayerState state = new ServerPlayerState()
                    .setPlayerId(playerId);
            state.getGhostInfo().setPlayerId(playerId);

            return state;
        });
    }
}
