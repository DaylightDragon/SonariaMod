package org.daylight.sonariaworld.morph;

import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.mixinrelated.MorphPlayerData;

public final class MorphData {
    public static MorphState get(Player player) {
        return provider(player).sonaria$getMorphState();
    }

    public static void set(Player player, MorphState state) {
        provider(player).sonaria$setMorphState(state);
    }

    private static MorphPlayerData provider(Player player) {
        return (MorphPlayerData) player;
    }
}
