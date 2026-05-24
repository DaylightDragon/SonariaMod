package org.daylight.sonariaworld.data.coordinatesystems;

import lombok.*;
import lombok.experimental.Accessors;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.daylight.sonariaworld.data.MathUtils;
import org.daylight.sonariaworld.data.ServerSpatialSystems;
import org.joml.Vector3f;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Accessors(chain=true)
public class Hitbox extends CoordinateSystemComponent {
    public enum HitboxType {
        DAMAGE_ABSORPTION,
        ATTACK
    }

    @Required
    private final CoordinateSystemComponent parentCoordinateSystem;
    private float xSize;
    private float ySize;
    private float zSize;

    private List<CoordinateSystemComponent> children = List.of();
    @Required
    private final HitboxType hitboxType;

    @Override
    public CoordinateSystemComponent getParentCoordinateSystem() {
        return parentCoordinateSystem;
    }

    @Override
    protected List<CoordinateSystemComponent> getChildren() {
        return children;
    }

    public void setLocalPosition(float x, float y, float z) {
        local().position().set(x, y, z);
        markDirty();
    }

    public void setLocalRotation(float yaw, float pitch, float roll) {
        local().rotation()
                .identity()
                .rotateY((float)Math.toRadians(yaw))
                .rotateX((float)Math.toRadians(pitch))
                .rotateZ((float)Math.toRadians(roll));

        markDirty();
    }

    public Vector3f getSize() {
        return new Vector3f(xSize, ySize, zSize);
    }

    public void setSize(float xSize, float ySize, float zSize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
    }

    @Override
    public Level getWorld() {
        if(getParentCoordinateSystem() != null) return getParentCoordinateSystem().getWorld();
        return null;
    }

    @Override
    public CoordinateSystemComponent getRoot() {
        if(getParentCoordinateSystem() != null) return getParentCoordinateSystem().getRoot();
        return null;
    }

    @Override
    public void updateGlobal() {
        Level world = getWorld();
        if(world == null) return;

        boolean wasDirty = isDirty();
        super.updateGlobal();

        SpatialHash3D spatialHash = ServerSpatialSystems.getSpatialHash(world);
        if(wasDirty) spatialHash.update(this);
    }

    public boolean intersects(Hitbox hitbox) {
        return MathUtils.intersects(this, hitbox);
    }
}
