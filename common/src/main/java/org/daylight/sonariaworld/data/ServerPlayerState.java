package org.daylight.sonariaworld.data;

import lombok.*;
import lombok.experimental.Accessors;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.daylight.sonariaworld.data.coordinatesystems.CoordinateSystemComponent;
import org.daylight.sonariaworld.entity.hitboxes.HitboxHolder;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ServerPlayerState {
    private String playerId;
    private LivingEntity ghostEntity;
    private CreatureGhostInfo ghostInfo = new CreatureGhostInfo();
    private CreatureSurvivalStats survivalStats = new CreatureSurvivalStats();
}
