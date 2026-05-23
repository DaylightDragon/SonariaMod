package org.daylight.sonariaworld.data;

import lombok.*;
import lombok.experimental.Accessors;
import net.minecraft.world.entity.LivingEntity;
import org.daylight.sonariaworld.data.coordinatesystems.CoordinateSystemComponent;
import org.daylight.sonariaworld.data.coordinatesystems.Hitbox;
import org.daylight.sonariaworld.entity.hitboxes.HitboxPresets;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ServerPlayerState {
    private LivingEntity ghostEntity;
    private CreatureGhostInfo ghostInfo = new CreatureGhostInfo();
    private CreatureSurvivalStats survivalStats = new CreatureSurvivalStats();

    @Getter
    @Setter
    @NoArgsConstructor
    @Accessors(chain = true)
    public class CreatureGhostInfo extends CoordinateSystemComponent {
        private double x;
        private double y;
        private double z;

        private float yaw;
        private float pitch;
        private float roll;

        private float headYaw;

        private boolean rotationInitialized = false;
        private HitboxPresets hitboxPresets = null;

        @Override
        protected List<CoordinateSystemComponent> getChildren() {
            if(hitboxPresets == null) return List.of();
            return hitboxPresets.getHitboxes();
        }

        @Override
        public CoordinateSystemComponent getParentCoordinateSystem() {
            return null;
        }

        @Override
        public void updateGlobal() {
            localTransform.position().set(x, y, z);
            System.out.println("Set ghost position: " + localTransform.position() + " from x: " + x + " y: " + y + " z: " + z);

            localTransform.rotation()
                    .identity()
                    .rotateY((float)Math.toRadians(-yaw))
                    .rotateX((float)Math.toRadians(pitch))
                    .rotateZ((float)Math.toRadians(roll));

            super.updateGlobal();
        }

        public void updateHitboxes() {
            for(CoordinateSystemComponent child : getChildren()) {
                child.setDirty(true);
                child.updateGlobal();
            }
        }
    }

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    public class CreatureSurvivalStats {
        private float hp;
        private float stamina;
        private float hunger;
        private float thurs;
    }
}
