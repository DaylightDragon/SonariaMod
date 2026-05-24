package org.daylight.sonariaworld.util;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.mixinrelated.IdHolder;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public final class PlayerLookup {
    private static final Map<String, Player> CLIENT_CACHE = new HashMap<>();
    private static final Map<String, ServerPlayer> SERVER_CACHE = new HashMap<>();

    private PlayerLookup() {}

    // CLIENT

    public static Player client(String id) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null) {
            CLIENT_CACHE.clear();
            return null;
        }

        Player cached = CLIENT_CACHE.get(id);

        if (isValidClient(cached, id)) {
            return cached;
        }

        Player found = mc.level.players().stream()
                .filter(p -> id.equals(((IdHolder) p).sonaria$getId()))
                .findFirst()
                .orElse(null);

        if (found != null) {
            CLIENT_CACHE.put(id, found);
        } else {
            CLIENT_CACHE.remove(id);
        }

        return found;
    }

    // SERVER

    public static ServerPlayer server(MinecraftServer server, String id) {

        ServerPlayer cached = SERVER_CACHE.get(id);

        if (isValidServer(cached, id)) {
            return cached;
        }

        ServerPlayer found = server.getPlayerList()
                .getPlayers()
                .stream()
                .filter(p -> id.equals(((IdHolder) p).sonaria$getId()))
                .findFirst()
                .orElse(null);

        if (found != null) {
            SERVER_CACHE.put(id, found);
        } else {
            SERVER_CACHE.remove(id);
        }

        return found;
    }

    // VALIDATION

    private static boolean isValidClient(Player p, String id) {
        return p != null
                && !p.isRemoved()
                && p.level() != null
                && id.equals(((IdHolder) p).sonaria$getId());
    }

    private static boolean isValidServer(ServerPlayer p, String id) {
        return p != null
                && !p.isRemoved()
                && p.connection != null
                && p.level() != null
                && id.equals(((IdHolder) p).sonaria$getId());
    }

    // MANUAL INVALIDATION

    public static void invalidate(String id) {
        CLIENT_CACHE.remove(id);
        SERVER_CACHE.remove(id);
    }

    public static void clear() {
        CLIENT_CACHE.clear();
        SERVER_CACHE.clear();
    }
}