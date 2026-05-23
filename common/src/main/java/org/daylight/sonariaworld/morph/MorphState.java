package org.daylight.sonariaworld.morph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MorphState {
    private boolean morphed = false;
    private Identifier entityIdentifier = null;
    private int variant = 0;
    private boolean dirty = false;
    private LivingEntity morphEntity;
    private MorphVisualsInfo morphVisualsInfo = new MorphVisualsInfo();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    @ToString
    public class MorphVisualsInfo {
        private float morphYaw0;
        private float morphYaw;
        private float morphHeadYaw0;
        private float morphHeadYaw;
        private float morphPitch0;
        private float morphPitch;
    }
}
