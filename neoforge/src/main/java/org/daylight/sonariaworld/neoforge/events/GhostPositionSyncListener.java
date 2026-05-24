package org.daylight.sonariaworld.neoforge.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.daylight.sonariaworld.data.CreatureGhostInfo;
import org.daylight.sonariaworld.data.systems.GhostCreatureManager;
import org.daylight.sonariaworld.data.systems.ServerPlayerManager;
import org.daylight.sonariaworld.data.ServerPlayerState;
import org.daylight.sonariaworld.data.coordinatesystems.CoordinateSystemComponent;
import org.daylight.sonariaworld.data.coordinatesystems.Hitbox;
import org.daylight.sonariaworld.data.coordinatesystems.Transform;
import org.daylight.sonariaworld.entity.hitboxes.HitboxHolder;
import org.daylight.sonariaworld.mixinrelated.IdHolder;
import org.joml.Vector3fc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GhostPositionSyncListener {
    private static final double MOVEMENT_THRESHOLD_SQR = 1.0E-6;

    private static final Map<String, Vec3> LAST_APPLIED_POSITIONS =
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
                LAST_APPLIED_POSITIONS.get(((IdHolder) player).sonaria$getId());

        if (lastApplied != null
                && currentPosition.distanceToSqr(lastApplied)
                < MOVEMENT_THRESHOLD_SQR) {
            return;
        }

        LAST_APPLIED_POSITIONS.put(
                ((IdHolder) player).sonaria$getId(),
                currentPosition
        );

        GhostCreatureManager.syncGhostPosition(
                GhostCreatureManager.get(player),
                player
        );

        CreatureGhostInfo ghostInfo = ServerPlayerManager.get(player).getGhostInfo();
        ghostInfo.setPlayerWorld(player.level());
        ghostInfo.setX(player.getX());
        ghostInfo.setY(player.getY());
        ghostInfo.setZ(player.getZ());
        ghostInfo.setDirty(true);
        ghostInfo.updateHitboxes();

        HitboxHolder hitboxHolder = ghostInfo.getHitboxHolder();
        if(hitboxHolder != null) {
            List<CoordinateSystemComponent> hitboxes = hitboxHolder.getHitboxes();
            if(!hitboxes.isEmpty()) {
                Hitbox hitbox = (Hitbox) hitboxes.getFirst();
                Transform transform = hitbox.getWorldTransform();
//                System.out.println("Pos: " + vec(transform.position()));
//                System.out.println("Rotation: " + transform.rotation());
//                System.out.println("Size: " + hitbox.getSize());
            } else {
//                System.out.println(ghostInfo);
            }
        }
    }

    public static String vec(Vector3fc v) {
        return String.format(
                "(%.3f, %.3f, %.3f)",
                v.x(),
                v.y(),
                v.z()
        );
    }

    public static void fullCleanup() {
        LAST_APPLIED_POSITIONS.clear();
    }
}
