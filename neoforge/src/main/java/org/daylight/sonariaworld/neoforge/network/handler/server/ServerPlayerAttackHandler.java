package org.daylight.sonariaworld.neoforge.network.handler.server;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.daylight.sonariaworld.data.InteractionManager;
import org.daylight.sonariaworld.network.payload.PlayerAttackPayload;

public final class ServerPlayerAttackHandler {
    private ServerPlayerAttackHandler() {}

    public static void handle(
            PlayerAttackPayload payload,
            IPayloadContext context
    ) {
        ServerPlayer player = (ServerPlayer) context.player();

        System.out.println("Received attack from client");
        context.enqueueWork(() -> {
            InteractionManager.onPlayerAttack(player.level().getServer(), player);
        });
    }
}
