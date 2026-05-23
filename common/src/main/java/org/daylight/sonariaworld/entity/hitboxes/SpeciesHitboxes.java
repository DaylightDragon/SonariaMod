package org.daylight.sonariaworld.entity.hitboxes;

import net.minecraft.world.entity.EntityType;
import org.daylight.sonariaworld.data.coordinatesystems.CoordinateSystemComponent;
import org.daylight.sonariaworld.data.coordinatesystems.Hitbox;
import org.daylight.sonariaworld.entity.CreaturePose;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SpeciesHitboxes {
    private static final Map<EntityType<?>, HitboxPresets> REGISTRY = new HashMap<>();

    public static void register(EntityType<?> type, HitboxPresets presets) {
        REGISTRY.put(type, presets);
    }

    public static HitboxPresets get(EntityType<?> type) {
        return REGISTRY.get(type);
    }
}
