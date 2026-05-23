package org.daylight.sonariaworld.neoforge;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.daylight.sonariaworld.data.GhostCreatureManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class GhostPositionSyncListener {
    private static final double MOVEMENT_THRESHOLD_SQR = 1.0E-6;

    private static final Map<UUID, Vec3> LAST_APPLIED_POSITIONS =
            new HashMap<>();

    private GhostPositionSyncListener() {}

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        Vec3 currentPosition = player.position();

        Vec3 lastApplied =
                LAST_APPLIED_POSITIONS.get(player.getUUID());

        if (lastApplied != null
                && currentPosition.distanceToSqr(lastApplied)
                < MOVEMENT_THRESHOLD_SQR) {
            return;
        }

        LAST_APPLIED_POSITIONS.put(
                player.getUUID(),
                currentPosition
        );

        GhostCreatureManager.syncGhostPosition(
                GhostCreatureManager.get(player),
                player
        );
    }
}
