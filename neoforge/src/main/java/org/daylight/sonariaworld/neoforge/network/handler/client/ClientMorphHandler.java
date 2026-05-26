package org.daylight.sonariaworld.neoforge.network.handler.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.daylight.sonariaworld.data.ClientMorphVisualsInfo;
import org.daylight.sonariaworld.entity.hitboxes.SpeciesHitboxes;
import org.daylight.sonariaworld.morph.MorphStateService;
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

            MorphState state = MorphStateService.get(player);
            state.setMorphed(payload.morphed());
            state.setDirty(true);

            if(payload.morphed()) {
                Identifier entityId = payload.entityId();
                if (!BuiltInRegistries.ENTITY_TYPE.containsKey(entityId)) return;
                EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.getValue(entityId);

                state.setEntityIdentifier(payload.entityId());
                state.setVariant(payload.variant());

                player.refreshDimensions();
                player.setBoundingBox(player.getBoundingBox());

                ClientMorphVisualsInfo visualsInfo = state.getClientMorphVisualsInfo();
                visualsInfo.setHitboxHolder(SpeciesHitboxes.create(type, visualsInfo));
                visualsInfo.setDirty(true);
                visualsInfo.updateHitboxes();
            } else {
                player.refreshDimensions();
                player.setBoundingBox(player.getBoundingBox());
            }
        });
    }
}
