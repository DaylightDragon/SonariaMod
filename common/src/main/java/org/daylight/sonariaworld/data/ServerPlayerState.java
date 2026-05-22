package org.daylight.sonariaworld.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ServerPlayerState {
    private CreatureMirrorInfo mirrorInfo = new CreatureMirrorInfo();
    private CreatureSurvivalStats survivalStats = new CreatureSurvivalStats();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public class CreatureMirrorInfo {
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

    public CreatureMirrorInfo getMirrorInfo() {
        return mirrorInfo;
    }

    public void setMirrorInfo(CreatureMirrorInfo mirrorInfo) {
        this.mirrorInfo = mirrorInfo;
    }

    public CreatureSurvivalStats getSurvivalStats() {
        return survivalStats;
    }

    public void setSurvivalStats(CreatureSurvivalStats survivalStats) {
        this.survivalStats = survivalStats;
    }
}
