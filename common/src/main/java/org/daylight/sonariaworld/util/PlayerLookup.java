package org.daylight.sonariaworld.util;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.mixinrelated.IdHolder;

public final class PlayerLookup {
    public static Player client(String id) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return null;

        return mc.level.players().stream()
                .filter(p -> ((IdHolder)p).sonaria$getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static ServerPlayer server(MinecraftServer server, String id) {
        return server.getPlayerList().getPlayers().stream()
                .filter(p -> ((IdHolder)p).sonaria$getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
