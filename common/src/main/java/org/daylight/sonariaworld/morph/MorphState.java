package org.daylight.sonariaworld.morph;

import lombok.*;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.data.coordinatesystems.CoordinateSystemComponent;
import org.daylight.sonariaworld.entity.hitboxes.HitboxPresets;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class MorphState {
    private boolean morphed = false;
    private Identifier entityIdentifier = null;
    private int variant = 0;
    private boolean dirty = false;
    private LivingEntity morphEntity;
    private MorphVisualsInfo morphVisualsInfo = new MorphVisualsInfo();
    private InterpolatedCoords realPlayerCoords = new InterpolatedCoords();
    private EntityDimensions realPlayerDimensions = EntityDimensions.fixed(0.5f, 0.5f); // temp initialization

    @Getter
    @Setter
    @Accessors(chain = true)
    public class InterpolatedCoords {
        private double x;
        private double y;
        private double z;
        private double x0;
        private double y0;
        private double z0;
        private float yaw;
        private float pitch;
        private float roll;
        private float yaw0;
        private float pitch0;
        private float roll0;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ToString
    public class MorphVisualsInfo extends CoordinateSystemComponent {
        private float morphYaw0;
        private float morphYaw;
        private float morphHeadYaw0;
        private float morphHeadYaw;
        private float morphPitch0;
        private float morphPitch;
        private float morphRoll0;
        private float morphRoll;

        private float x0;
        private float y0;
        private float z0;

        private HitboxPresets hitboxPresets = null;

        public MorphVisualsInfo() {
            localTransform.position().set(0, 0, 0);
            localTransform.rotation()
                    .identity()
                    .rotateY((float)Math.toRadians(-getMorphYaw()))
                    .rotateX((float)Math.toRadians(getMorphPitch()))
                    .rotateZ((float)Math.toRadians(getMorphRoll()));
        }

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
            Minecraft minecraft = Minecraft.getInstance();
            if(minecraft == null) return;

            InterpolatedCoords realPlayerCoords = getRealPlayerCoords();

            LivingEntity morphEntity = getMorphEntity();
            if(morphEntity == null) return;

            float partialTick = minecraft
                    .getDeltaTracker()
                    .getGameTimeDeltaPartialTick(false);

            localTransform.position().set(
                    Mth.lerp(partialTick, realPlayerCoords.getX0(), realPlayerCoords.getX()),
                    Mth.lerp(partialTick, realPlayerCoords.getY0(), realPlayerCoords.getY()) + getRealPlayerDimensions().height() / 2,
                    Mth.lerp(partialTick, realPlayerCoords.getZ0(), realPlayerCoords.getZ()));

            float yaw = Mth.rotLerp(
                    partialTick,
                    morphVisualsInfo.getMorphYaw0(),
                    morphVisualsInfo.getMorphYaw()
            );

            float pitch = Mth.rotLerp(
                    partialTick,
                    morphVisualsInfo.getMorphPitch0(),
                    morphVisualsInfo.getMorphPitch()
            );

            float roll = Mth.rotLerp(
                    partialTick,
                    morphVisualsInfo.getMorphRoll0(),
                    morphVisualsInfo.getMorphRoll()
            );

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
}
