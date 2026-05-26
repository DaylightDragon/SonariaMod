package org.daylight.sonariaworld.morph;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.data.ClientMorphVisualsInfo;
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

        onEntityCreation(entity, player);

//        System.out.println("Put cache: " + ((IdHolder)player).sonaria$getId() + " " + entity);
        CACHE.put(((IdHolder)player).sonaria$getId(), entity);

        return entity;
    }

    private static void onEntityCreation(LivingEntity entity, Player player) {
        entity.setYRot(player.getYRot());
        entity.yRotO = player.yRotO;
        entity.getXRot(player.getXRot());
        entity.xRotO = player.xRotO;
        entity.setYBodyRot(player.yBodyRot);
        entity.yBodyRotO = player.yBodyRotO;
        entity.setYHeadRot(player.getYHeadRot());
        entity.yHeadRotO = player.yHeadRotO;

        MorphState state = MorphStateService.get(player);
        ClientMorphVisualsInfo visualsInfo = state.getClientMorphVisualsInfo();

        visualsInfo.setMorphYaw(player.getYRot());
        visualsInfo.setMorphYaw0(player.yRotO);
        visualsInfo.setMorphHeadYaw(player.getYHeadRot());
        visualsInfo.setMorphHeadYaw0(player.yHeadRotO);
        visualsInfo.setMorphPitch(0);
        visualsInfo.setMorphPitch0(0);
//        visualsInfo.setMorphHeadPitch(player.getXRot()); // TODO
//        visualsInfo.setMorphHeadPitch0(player.xRotO);
        visualsInfo.setMorphRoll(0);
        visualsInfo.setMorphRoll0(0);
    }

    public static void fullCleanup() {
        CACHE.clear();
    }
}
