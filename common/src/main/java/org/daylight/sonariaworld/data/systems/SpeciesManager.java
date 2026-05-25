package org.daylight.sonariaworld.data.systems;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SpeciesManager {
    private SpeciesManager() {}

    private static final List<Supplier<? extends EntityType<?>>> SPECIES =
            new ArrayList<>();

    public static void registerSpecies(
            Supplier<? extends EntityType<?>> supplier
    ) {
        SPECIES.add(supplier);
    }

    public static List<EntityType<?>> getAllSpecies() {
        List<EntityType<?>> result = new ArrayList<>();

        for (Supplier<? extends EntityType<?>> supplier : SPECIES) {
            result.add(supplier.get());
        }

        return List.copyOf(result);
    }

    public static List<EntityType<?>> getPlayerOwnedSpecies(String playerId) {
        return getAllSpecies();
    }
}
