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
        return STATES.computeIfAbsent(((IdHolder)player).sonaria$getId(), id -> new ServerPlayerState());
    }
}
