package org.daylight.sonariaworld.morph;

import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerMorph {
    private static final Map<UUID, PlayerMorphState> CLIENT_STATES = new HashMap<>();
    private static final Map<UUID, PlayerMorphState> SERVER_STATES = new HashMap<>();

    public static PlayerMorphState get(Player player) {
        if (player.level().isClientSide()) {
            return CLIENT_STATES.computeIfAbsent(player.getUUID(), id -> new PlayerMorphState());
        } else {
            return SERVER_STATES.computeIfAbsent(player.getUUID(), id -> new PlayerMorphState());
        }
    }

    // Для очистки при выходе игрока
    public static void remove(Player player) {
        if (player.level().isClientSide()) {
            CLIENT_STATES.remove(player.getUUID());
        } else {
            SERVER_STATES.remove(player.getUUID());
        }
    }
}
