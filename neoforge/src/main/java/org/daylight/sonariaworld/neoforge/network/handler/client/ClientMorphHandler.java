package org.daylight.sonariaworld.neoforge.network.handler.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.daylight.sonariaworld.entity.hitboxes.SpeciesHitboxes;
import org.daylight.sonariaworld.morph.MorphService;
import org.daylight.sonariaworld.morph.MorphState;
import org.daylight.sonariaworld.network.payload.MorphSyncPayload;
import org.daylight.sonariaworld.util.PlayerLookup;

public final class ClientMorphHandler {
    private ClientMorphHandler() {}

    public static void handle(
            MorphSyncPayload payload,
            IPayloadContext context
    ) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;

            Player player = PlayerLookup.client(payload.playerId());
            if (player == null) return;

            Identifier entityId = payload.entityId();
            if (!BuiltInRegistries.ENTITY_TYPE.containsKey(entityId)) return;
            EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.getValue(entityId);

            System.out.println("Received morph sync:");
            System.out.println("Player: " + payload.playerId());
            System.out.println("Entity: " + payload.entityId());
            System.out.println("Variant: " + payload.variant());
            System.out.println("Morphed: " + payload.morphed());

//            MorphState state = MorphData.get(player);
            MorphState state = MorphService.get(player);

            state.setMorphed(payload.morphed());
            state.setEntityIdentifier(payload.entityId());
            state.setVariant(payload.variant());

            state.setDirty(true);

            player.refreshDimensions();
            player.setBoundingBox(player.getBoundingBox());

            MorphState.MorphVisualsInfo visualsInfo = state.getMorphVisualsInfo();
            visualsInfo.setHitboxPresets(SpeciesHitboxes.create(type, visualsInfo));
            visualsInfo.setDirty(true);
            visualsInfo.updateHitboxes();

            // Тут уже:
            // - обновление client cache
            // - refresh renderer
            // - refresh animations
            // - refresh hitbox visuals
        });
    }
}
