package org.daylight.sonariaworld.neoforge.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.Minecart;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.daylight.sonariaworld.SonariaWorld;
import org.daylight.sonariaworld.morph.PlayerMorph;
import org.daylight.sonariaworld.morph.PlayerMorphState;

import java.io.IOException;
import java.util.UUID;

public class ModNet {
    // Регистрация всех payloads
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1")
                .versioned("1.0.0");

        // C2S
        registrar.playToServer(
                ToggleMorphPayload.TYPE,
                ToggleMorphPayload.STREAM_CODEC,
                ModNet::handleToggleMorph
        );

        // S2C
        registrar.playToClient(
                PlayerMorphSyncPayload.TYPE,
                PlayerMorphSyncPayload.STREAM_CODEC,
                ModNet::handleMorphSync
        );
    }

    // Обработка запроса от клиента
    public static void handleToggleMorph(ToggleMorphPayload payload, IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        System.out.println("Handling toggle morph");

        context.enqueueWork(() -> {
            PlayerMorphState state = PlayerMorph.get(player);
            state.setMorphed(payload.enabled());

            // Синхронизация с ВСЕМИ игроками в радиусе видимости
            sendMorphSyncToNearbyPlayers(player, player.getUUID(), payload.enabled());

            // Обновление размеров
            player.refreshDimensions();

            player.calculateEntityAnimation(true);

            // Отправить подтверждение обратно этому игроку (опционально)
            sendMorphSyncToPlayer(player, player.getUUID(), payload.enabled());
        });
    }

    // Синхронизация для всех игроков поблизости
    public static void sendMorphSyncToNearbyPlayers(ServerPlayer source, UUID targetId, boolean morphed) {
        PlayerMorphSyncPayload payload = new PlayerMorphSyncPayload(targetId, morphed);

        source.level().getServer().getPlayerList().getPlayers().forEach(player -> {
            // Отправляем всем, кто в радиусе 64 блоков от target
            ServerPlayer targetPlayer = source.level().getServer().getPlayerList().getPlayer(targetId);
            if (targetPlayer != null && player.distanceTo(targetPlayer) <= 64) {
                PacketDistributor.sendToPlayer(player, payload);
            }
        });
    }

    // Синхронизация конкретному игроку
    public static void sendMorphSyncToPlayer(ServerPlayer player, UUID targetId, boolean morphed) {
        PacketDistributor.sendToPlayer(player, new PlayerMorphSyncPayload(targetId, morphed));
    }

    // Обработка S2C на клиенте
    public static void handleMorphSync(PlayerMorphSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            // Обновляем локальное состояние
            if (context.player().getUUID().equals(payload.playerId())) {
                // Это наш игрок
                PlayerMorph.get(context.player()).setMorphed(payload.morphed());
            } else {
                // Другой игрок - нужно обновить их модель/размер
                Player otherPlayer = context.player().level().getPlayerByUUID(payload.playerId());
                if (otherPlayer != null) {
                    PlayerMorph.get(otherPlayer).setMorphed(payload.morphed());
                    // Принудительно обновить размеры на клиенте
                    otherPlayer.refreshDimensions();
                }
            }
        });
    }

    public static void sendToggleMorph(boolean enabled) {
        ClientPacketDistributor.sendToServer(new ToggleMorphPayload(enabled));
    }
}
