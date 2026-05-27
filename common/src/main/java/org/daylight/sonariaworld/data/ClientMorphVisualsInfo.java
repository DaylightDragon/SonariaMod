package org.daylight.sonariaworld.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.daylight.sonariaworld.data.coordinatesystems.CoordinateSystemComponent;
import org.daylight.sonariaworld.entity.hitboxes.HitboxHolder;
import org.daylight.sonariaworld.morph.MorphState;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class ClientMorphVisualsInfo extends CoordinateSystemComponent {
    private MorphState state;
    private float morphYaw0;
    private float morphYaw;
    private float morphHeadYaw0;
    private float morphHeadYaw;
    private float morphPitch0;
    private float morphPitch;
    private float morphRoll0;
    private float morphRoll;

    private HitboxHolder hitboxHolder = null;

    public ClientMorphVisualsInfo() {
        localTransform.position().set(0, 0, 0);
        localTransform.rotation()
                .identity()
                .rotateY((float)Math.toRadians(-getMorphYaw()))
                .rotateX((float)Math.toRadians(getMorphPitch()))
                .rotateZ((float)Math.toRadians(getMorphRoll()));
    }

    @Override
    protected List<CoordinateSystemComponent> getChildren() {
        if(hitboxHolder == null) return List.of();
        return hitboxHolder.getHitboxes();
    }

    @Override
    public Optional<CoordinateSystemComponent> getParentCoordinateSystem() {
        return Optional.empty();
    }

    public Optional<HitboxHolder> getHitboxHolder() {
        return Optional.ofNullable(hitboxHolder);
    }

    @Override
    public void updateGlobal() {
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft == null) return;

        InterpolatedCoords realPlayerCoords = state.getRealPlayerCoords();

        LivingEntity morphEntity = state.getMorphEntity();
        if(morphEntity == null) return;

        float partialTick = minecraft
                .getDeltaTracker()
                .getGameTimeDeltaPartialTick(false);

        localTransform.position().set(
                Mth.lerp(partialTick, realPlayerCoords.getX0(), realPlayerCoords.getX()),
                Mth.lerp(partialTick, realPlayerCoords.getY0(), realPlayerCoords.getY()) + state.getRealPlayerDimensions().height() / 2,
                Mth.lerp(partialTick, realPlayerCoords.getZ0(), realPlayerCoords.getZ()));

        float yaw = Mth.rotLerp(
                partialTick,
                getMorphYaw0(),
                getMorphYaw()
        );

        float pitch = Mth.rotLerp(
                partialTick,
                getMorphPitch0(),
                getMorphPitch()
        );

        float roll = Mth.rotLerp(
                partialTick,
                getMorphRoll0(),
                getMorphRoll()
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

    @Override
    public Level getWorld() {
        if(getParentCoordinateSystem().isPresent()) return getParentCoordinateSystem().get().getWorld();
        return state.getRealPlayerCoords().getWorld();
    }

    @Override
    public CoordinateSystemComponent getRoot() {
        if(getParentCoordinateSystem().isPresent()) return getParentCoordinateSystem().get().getRoot();
        return null;
    }
}
