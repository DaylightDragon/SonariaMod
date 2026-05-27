package org.daylight.sonariaworld.data.systems;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.data.CreatureCharacter;
import org.daylight.sonariaworld.data.CreatureGhostInfo;
import org.daylight.sonariaworld.data.ServerPlayerState;
import org.daylight.sonariaworld.data.coordinatesystems.CoordinateSystemComponent;
import org.daylight.sonariaworld.data.coordinatesystems.Hitbox;
import org.daylight.sonariaworld.data.coordinatesystems.SpatialHash3D;
import org.daylight.sonariaworld.mixinrelated.IdHolder;
import org.daylight.sonariaworld.util.PlayerLookup;

import java.util.ArrayList;
import java.util.List;

public class InteractionManager {
    private InteractionManager() {}

    public static void onPlayerAttack(MinecraftServer server, Player attacker) {
        SpatialHash3D spatialHash = ServerSpatialSystems.getSpatialHash(attacker.level());
        List<Hitbox> possiblyAffected = new ArrayList<>();

        ServerPlayerState state = ServerPlayerManager.get(attacker);
        if(state.getGhostInfo().getHitboxHolder().isEmpty()) return;
        a: for(CoordinateSystemComponent component : state.getGhostInfo().getHitboxHolder().get().getHitboxes()) {
            if(component instanceof Hitbox hitbox) {
                if(hitbox.getHitboxType() == Hitbox.HitboxType.ATTACK) {
                    for(Hitbox possiblyAffectedHitbox : spatialHash.query(SpatialHash3D.computeWorldAABB(hitbox))) {
                        if(possiblyAffectedHitbox.intersects(hitbox)) {
                            possiblyAffected.add(possiblyAffectedHitbox);
//                            break a;
                        }
                    }
                }
            }
        }

        String attackerPlayerId = ((IdHolder) attacker).sonaria$getId();

        for(Hitbox hitbox : possiblyAffected) {
            if(hitbox.getHitboxType() != Hitbox.HitboxType.DAMAGE_ABSORPTION) continue;
            CoordinateSystemComponent root = hitbox.getRoot();
            if(root instanceof CreatureGhostInfo creatureGhostInfo) {
                String victimPlayerId = creatureGhostInfo.getPlayerId();
                if(!attackerPlayerId.equals(victimPlayerId)) {
                    onEntityDamaged(server, creatureGhostInfo, 10);
                    break;
                }
            } else {
                // idk
            }
        }
    }

    public static void onEntityDamaged(MinecraftServer server, CreatureGhostInfo creatureGhostInfo, float damage) {
        ServerPlayerState state = ServerPlayerManager.get(creatureGhostInfo.getPlayerId());
        CreatureCharacter survivalStats = state.getCharacter();
        survivalStats.setHp(survivalStats.getHp() - damage);

        System.out.println("Damaged " + creatureGhostInfo.getPlayerId() + " by " + damage + " hp");

        Player player = PlayerLookup.server(server, creatureGhostInfo.getPlayerId());
        if(player == null) return;
    }
}
