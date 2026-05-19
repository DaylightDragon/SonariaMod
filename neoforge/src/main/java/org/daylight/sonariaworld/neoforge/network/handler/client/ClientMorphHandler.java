package org.daylight.sonariaworld.neoforge.network.handler.client;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.daylight.sonariaworld.network.payload.MorphSyncPayload;

public final class ClientMorphHandler {
    private ClientMorphHandler() {

    }

    public static void handle(
            MorphSyncPayload payload,
            IPayloadContext context
    ) {
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();

            if (minecraft.level == null) {
                return;
            }

            System.out.println("Received morph sync:");
            System.out.println("Player: " + payload.playerId());
            System.out.println("Entity: " + payload.entityId());
            System.out.println("Variant: " + payload.variant());
            System.out.println("Morphed: " + payload.morphed());

            // Тут уже:
            // - обновление client cache
            // - refresh renderer
            // - refresh animations
            // - refresh hitbox visuals
        });
    }
}
