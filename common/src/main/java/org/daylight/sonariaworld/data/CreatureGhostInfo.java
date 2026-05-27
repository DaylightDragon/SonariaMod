package org.daylight.sonariaworld.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.world.level.Level;
import org.daylight.sonariaworld.data.coordinatesystems.CoordinateSystemComponent;
import org.daylight.sonariaworld.entity.hitboxes.HitboxHolder;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class CreatureGhostInfo extends CoordinateSystemComponent {
    private String playerId;
    private Level playerWorld;
    private double x;
    private double y;
    private double z;

    private float yaw;
    private float pitch;
    private float roll;

    private float headYaw;
    private float headPitch;

    private HitboxHolder hitboxHolder = null;

    @Override
    protected List<CoordinateSystemComponent> getChildren() {
        if(hitboxHolder == null) return List.of();
        return hitboxHolder.getHitboxes();
    }

    @Override
    public Optional<CoordinateSystemComponent> getParentCoordinateSystem() {
        return Optional.empty();
    }

    @Override
    public void updateGlobal() {
        localTransform.position().set(x, y, z);
//            System.out.println("Set ghost position: " + localTransform.position() + " from x: " + x + " y: " + y + " z: " + z);

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
        return getPlayerWorld();
    }

    @Override
    public CoordinateSystemComponent getRoot() {
        if(getParentCoordinateSystem().isPresent()) return getParentCoordinateSystem().get().getRoot();
        return this;
    }

    public Optional<HitboxHolder> getHitboxHolder() {
        return Optional.ofNullable(hitboxHolder);
    }
}
