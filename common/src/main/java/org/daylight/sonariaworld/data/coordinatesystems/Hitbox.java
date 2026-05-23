package org.daylight.sonariaworld.data.coordinatesystems;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain=true)
public class Hitbox extends CoordinateSystemComponent {
    public enum HitboxType {
        DAMAGE_ABSORPTION,
        ATTACK
    }

    private CoordinateSystemComponent parentCoordinateSystem;
    private float xSize;
    private float ySize;
    private float zSize;

    private List<CoordinateSystemComponent> children = List.of();
    private HitboxType hitboxType = HitboxType.DAMAGE_ABSORPTION;

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

    public void setSize(float xSize, float ySize, float zSize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
    }
}
