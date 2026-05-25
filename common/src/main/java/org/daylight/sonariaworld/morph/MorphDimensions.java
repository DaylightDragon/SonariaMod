package org.daylight.sonariaworld.morph;

import net.minecraft.world.entity.*;
import org.daylight.sonariaworld.registry.EntityRegistry;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public final class MorphDimensions {
    private MorphDimensions() {}

    private static final Map<EntityType<?>, EnumMap<Pose, EntityDimensions>> DIMENSIONS = new HashMap<>();
    private static EntityDimensions normalPlayerDimensions = null;

    static {
        register(
                EntityRegistry.OLATUA.get(),
                0.9F,
                0.8F
        );
    }

    public static EntityDimensions get(
            EntityType<?> entityType,
            Pose pose
    ) {
        EnumMap<Pose, EntityDimensions> poseMap = DIMENSIONS.get(entityType);

        if (poseMap == null) {
            return defaultPlayer(pose);
        }

        return poseMap.getOrDefault(
                pose,
                poseMap.get(Pose.STANDING)
        );
    }

    private static void register(
            EntityType<?> entityType,
            float width,
            float height
    ) {
        EnumMap<Pose, EntityDimensions> poses = new EnumMap<>(Pose.class);

        poses.put(
                Pose.STANDING,
                EntityDimensions.scalable(width, height)
                        .withEyeHeight(height * 0.85F)
        );

        poses.put(
                Pose.CROUCHING,
                EntityDimensions.scalable(width, height * 0.85F)
                        .withEyeHeight(height * 0.85F * 0.85F)
        );

        poses.put(
                Pose.SWIMMING,
                EntityDimensions.scalable(width, Math.min(width, height * 0.5F))
                        .withEyeHeight(height * 0.35F)
        );

        poses.put(
                Pose.FALL_FLYING,
                EntityDimensions.scalable(width, Math.min(width, height * 0.5F))
                        .withEyeHeight(height * 0.35F)
        );

        poses.put(
                Pose.SPIN_ATTACK,
                EntityDimensions.scalable(width, Math.min(width, height * 0.5F))
                        .withEyeHeight(height * 0.35F)
        );

        poses.put(
                Pose.SLEEPING,
                EntityDimensions.fixed(0.2F, 0.2F)
        );

        DIMENSIONS.put(entityType, poses);
    }

    private static EntityDimensions defaultPlayer(Pose pose) {
        return switch (pose) {
            case CROUCHING ->
                    EntityDimensions.scalable(0.6F, 1.5F)
                            .withEyeHeight(1.27F);

            case SWIMMING, FALL_FLYING, SPIN_ATTACK ->
                    EntityDimensions.scalable(0.6F, 0.6F)
                            .withEyeHeight(0.4F);

            case SLEEPING ->
                    EntityDimensions.fixed(0.2F, 0.2F);

            default ->
                    EntityDimensions.scalable(0.6F, 1.8F)
                            .withEyeHeight(1.62F);
        };
    }

    public static EntityDimensions getNormalPlayerDimensions() {
        if(normalPlayerDimensions == null) {
            normalPlayerDimensions = EntityDimensions.scalable(0.6F, 1.8F)
                    .withEyeHeight(1.62F)
                    .withAttachments(EntityAttachments.builder().attach(EntityAttachment.VEHICLE, Avatar.DEFAULT_VEHICLE_ATTACHMENT));;
        }
        return normalPlayerDimensions;
    }
}
