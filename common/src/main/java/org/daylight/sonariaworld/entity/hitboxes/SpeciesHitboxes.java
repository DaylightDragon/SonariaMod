package org.daylight.sonariaworld.entity.hitboxes;

import net.minecraft.world.entity.EntityType;
import org.daylight.sonariaworld.data.coordinatesystems.CoordinateSystemComponent;
import org.daylight.sonariaworld.data.coordinatesystems.Hitbox;
import org.daylight.sonariaworld.entity.CreaturePose;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class SpeciesHitboxes {
    private static final Map<EntityType<?>, Supplier<HitboxPresets>> REGISTRY =
            new HashMap<>();

    public static void register(
            EntityType<?> type,
            Supplier<HitboxPresets> factory
    ) {
        REGISTRY.put(type, factory);
    }

    public static HitboxPresets create(EntityType<?> type, CoordinateSystemComponent parentSystem) {
        Supplier<HitboxPresets> supplier = REGISTRY.get(type);
        if (supplier == null) return null;

        HitboxPresets presets = supplier.get();
        presets.init(parentSystem);

        return presets;
    }
}
