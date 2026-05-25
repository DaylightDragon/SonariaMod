package org.daylight.sonariaworld.data.systems;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

import java.util.List;

public class SpeciesManager {
    private SpeciesManager() {}

    private static List<EntityType<?>> SPECIES;

    public static <T extends Mob> void registerSpecies(EntityType<T> newEntityType) {
        SPECIES.add(newEntityType);
    }

    public List<EntityType<?>> getAllSpecies() {
        return ImmutableList.copyOf(SPECIES);
    }

    public List<EntityType<?>> getPlayerOwnedSpecies(String playerId) {
        return getAllSpecies();
    }
}
