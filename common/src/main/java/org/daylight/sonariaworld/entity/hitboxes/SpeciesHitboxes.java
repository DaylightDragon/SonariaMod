package org.daylight.sonariaworld.entity.hitboxes;

import net.minecraft.world.entity.EntityType;
import org.daylight.sonariaworld.data.coordinatesystems.CoordinateSystemComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class SpeciesHitboxes {
    private static final Map<EntityType<?>, Supplier<HitboxHolder>> REGISTRY =
            new HashMap<>();

    public static void register(
            EntityType<?> type,
            Supplier<HitboxHolder> factory
    ) {
        REGISTRY.put(type, factory);
    }

    public static HitboxHolder create(EntityType<?> type, CoordinateSystemComponent parentSystem) {
        Supplier<HitboxHolder> supplier = REGISTRY.get(type);
        if (supplier == null) return null;

        HitboxHolder presets = supplier.get();
        presets.init(parentSystem);

        return presets;
    }
}
