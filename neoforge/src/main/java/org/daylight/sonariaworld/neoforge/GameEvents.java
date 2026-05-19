package org.daylight.sonariaworld.neoforge;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.daylight.sonariaworld.SonariaWorld;
import org.daylight.sonariaworld.morph.PlayerMorph;
import org.daylight.sonariaworld.morph.PlayerMorphState;
import org.daylight.sonariaworld.neoforge.network.ModNet;

public class GameEvents {
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PlayerMorphState state = PlayerMorph.get(player);
            // Синхронизируем состояние с новым игроком
            ModNet.sendMorphSyncToPlayer(player, player.getUUID(), state.isMorphed());
            // И синхронизируем с окружающими
            ModNet.sendMorphSyncToNearbyPlayers(player, player.getUUID(), state.isMorphed());
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // При смене измерения нужно ресинхронизировать
            PlayerMorphState state = PlayerMorph.get(player);
            ModNet.sendMorphSyncToNearbyPlayers(player, player.getUUID(), state.isMorphed());
        }
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        // Когда один игрок начинает видеть другого
        if (event.getTarget() instanceof ServerPlayer trackedPlayer &&
                event.getEntity() instanceof ServerPlayer trackingPlayer) {
            PlayerMorphState state = PlayerMorph.get(trackedPlayer);
            ModNet.sendMorphSyncToPlayer(trackingPlayer, trackedPlayer.getUUID(), state.isMorphed());
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        // Очистка данных при выходе
        if (event.getEntity() instanceof ServerPlayer player) {
            PlayerMorph.remove(player);
        }
    }
}
