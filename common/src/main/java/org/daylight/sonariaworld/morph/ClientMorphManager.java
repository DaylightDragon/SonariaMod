package org.daylight.sonariaworld.morph;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.mixinrelated.IdHolder;

import java.util.HashMap;
import java.util.Map;

public final class ClientMorphManager {
    private static final Map<String, LivingEntity> CACHE =
            new HashMap<>();

    public static LivingEntity getRenderEntity(
            Player player
    ) {
        MorphState state = MorphStateService.get(player); // MorphData.get(player);

//        System.out.println("Morph state: " + state); // c1
//        System.out.println(state.isMorphed() + " " + state.getEntityId()); // c1

        if (!state.isMorphed() || state.getEntityIdentifier() == null) {
            CACHE.remove(((IdHolder)player).sonaria$getId());
            return null;
        }

        LivingEntity existing =
                CACHE.get(((IdHolder)player).sonaria$getId());

        if (existing != null) {

            Identifier currentId =
                    BuiltInRegistries.ENTITY_TYPE
                            .getKey(existing.getType());

            if (currentId.equals(state.getEntityIdentifier())) {
                return existing;
            }
        }

        EntityType<?> type =
                BuiltInRegistries.ENTITY_TYPE
                        .getValue(state.getEntityIdentifier());

        if (!(type.create(
                player.level(),
                EntitySpawnReason.LOAD
        ) instanceof LivingEntity entity)) {
            return null;
        }

        System.out.println("Put cache: " + ((IdHolder)player).sonaria$getId() + " " + entity);
        CACHE.put(((IdHolder)player).sonaria$getId(), entity);

        return entity;
    }

    public static void fullCleanup() {
        CACHE.clear();
    }
}
