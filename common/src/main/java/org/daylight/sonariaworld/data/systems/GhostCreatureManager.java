package org.daylight.sonariaworld.data.systems;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.data.CreatureGhostInfo;
import org.daylight.sonariaworld.data.ServerPlayerState;
import org.daylight.sonariaworld.entity.hitboxes.SpeciesHitboxes;
import org.daylight.sonariaworld.mixinrelated.IdHolder;
import org.daylight.sonariaworld.mixinrelated.PossibleGhostEntity;
import org.daylight.sonariaworld.morph.MorphService;
import org.daylight.sonariaworld.morph.MorphState;

import java.util.HashMap;
import java.util.Map;

public final class GhostCreatureManager {
    private static final Map<String, LivingEntity> ENTITIES = new HashMap<>();

    public static LivingEntity get(Player player) {
        String id = ((IdHolder) player).sonaria$getId();

        MorphState state = MorphService.get(player);
        if (state == null || !state.isMorphed()) {
            return null;
        }

        EntityType<?> wantedType =
                BuiltInRegistries.ENTITY_TYPE.getValue(
                        state.getEntityIdentifier()
                );

        if (wantedType == null) {
            return null;
        }

        LivingEntity current = ENTITIES.get(id);

        // reuse existing
        if (current != null
                && !current.isRemoved()
                && current.isAlive()
                && current.level() == player.level()
                && current.getType() == wantedType) {

            syncGhostPosition(current, player);
            syncGhostRotation(current, player);

            return current;
        }

        // remove old
        if (current != null) {
            current.discard();
        }

        LivingEntity created = createGhost(player, wantedType);

        if (created != null) {
            ENTITIES.put(id, created);
        }

        return created;
    }

    private static LivingEntity createGhost(
            Player player,
            EntityType<?> type
    ) {
        if (!(player.level() instanceof ServerLevel level)) {
            return null;
        }

        Entity entity = type.create(level, EntitySpawnReason.COMMAND);
        System.out.println("Created a ghost: " + entity);

        if (!(entity instanceof LivingEntity living)) {
            return null;
        }

        if(entity instanceof PossibleGhostEntity ghostEntity) {
            ghostEntity.sonariaworld$setGhostEntity(true);
        }

        configureGhost(living, player);

        level.addFreshEntity(living);

        syncGhostPosition(living, player);

        level.getChunkSource().removeEntity(living);

        CreatureGhostInfo ghostInfo = ServerPlayerManager.get(player).getGhostInfo();
        ghostInfo.setPlayerWorld(player.level());
        ghostInfo.setX(player.getX());
        ghostInfo.setY(player.getY());
        ghostInfo.setZ(player.getZ());
        if(!ghostInfo.isRotationInitialized()) {
            ghostInfo.setYaw(player.getYRot());
            ghostInfo.setPitch(player.getXRot());
            ghostInfo.setHeadYaw(player.getYRot());
        }
        ghostInfo.setHitboxHolder(SpeciesHitboxes.create(type, ghostInfo));
        ghostInfo.setDirty(true);
        ghostInfo.updateHitboxes();
//        ghostInfo.forceUpdateChildren();

        return living;
    }

    public static void syncGhostPosition(LivingEntity ghost, Player player) {
        if (ghost == null) return;
        ghost.setPos(player.position());
    }

    public static void syncGhostRotation(
            LivingEntity ghost,
            Player player
    ) {
        if (ghost == null) return;

        ServerPlayerState serverState = ServerPlayerManager.get(player);
        CreatureGhostInfo ghostInfo = serverState.getGhostInfo();

        if(ghostInfo.isRotationInitialized()) {
            ghost.setYRot(ghostInfo.getYaw());
            ghost.yRotO = ghostInfo.getYaw();

            ghost.setXRot(ghostInfo.getPitch());
            ghost.xRotO = ghostInfo.getPitch();

            ghost.setYHeadRot(ghostInfo.getHeadYaw());
            ghost.yHeadRotO = ghostInfo.getHeadYaw();
        } else {
            ghost.setYRot(player.getYRot());
            ghost.setXRot(player.getXRot());

            ghost.yRotO = ghost.getYRot();
            ghost.xRotO = ghost.getXRot();
        }
    }

    private static void configureGhost(
            LivingEntity living,
            Player player
    ) {
        if(living instanceof Mob mob) mob.setNoAi(true);
        living.setSilent(true);
        living.setInvulnerable(true);
        living.noPhysics = false;
        living.setInvisible(true);
        syncGhostPosition(living, player);
        syncGhostRotation(living, player);
    }

    public static void fullCleanup() {
        ENTITIES.clear();
    }

    public static void clearGhost(ServerPlayer player) {
        String id = ((IdHolder) player).sonaria$getId();
        LivingEntity ghost = ENTITIES.get(id);
        if(ghost != null) ghost.remove(Entity.RemovalReason.DISCARDED);
        ENTITIES.remove(id);
    }
}
