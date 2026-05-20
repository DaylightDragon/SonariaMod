package org.daylight.sonariaworld.morph;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.morph.MorphData;
import org.daylight.sonariaworld.morph.MorphState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ClientMorphManager {
    private static final Map<UUID, LivingEntity> CACHE =
            new HashMap<>();

    public static LivingEntity getRenderEntity(
            Player player
    ) {
        MorphState state = MorphData.get(player);

        System.out.println("Morph state: " + state);
        System.out.println(state.isMorphed() + " " + state.getEntityId());

        if (!state.isMorphed() || state.getEntityId() == null) {
            CACHE.remove(player.getUUID());
            return null;
        }

        LivingEntity existing =
                CACHE.get(player.getUUID());

        if (existing != null) {

            Identifier currentId =
                    BuiltInRegistries.ENTITY_TYPE
                            .getKey(existing.getType());

            if (currentId.equals(state.getEntityId())) {
                return existing;
            }
        }

        EntityType<?> type =
                BuiltInRegistries.ENTITY_TYPE
                        .getValue(state.getEntityId());

        if (!(type.create(
                player.level(),
                EntitySpawnReason.LOAD
        ) instanceof LivingEntity entity)) {
            return null;
        }

        System.out.println("Put cache: " + player.getUUID() + " " + entity);
        CACHE.put(player.getUUID(), entity);

        return entity;
    }
}
