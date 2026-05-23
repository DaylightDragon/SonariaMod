package org.daylight.sonariaworld.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.world.entity.LivingEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ServerPlayerState {
    private LivingEntity ghostEntity;
    private CreatureMirrorInfo mirrorInfo = new CreatureMirrorInfo();
    private CreatureSurvivalStats survivalStats = new CreatureSurvivalStats();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public class CreatureMirrorInfo {
        boolean initialized = false;
        float yaw;
        float pitch;
        float headYaw;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public class CreatureSurvivalStats {
        float hp;
        float stamina;
        float hunger;
        float thurs;
    }
}
