package org.daylight.sonariaworld.data.coordinatesystems;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.joml.Vector3f;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public abstract class CoordinateSystemComponent {
    // local
    protected final Transform localTransform = new Transform();

    // cached world
    protected final Transform worldTransform = new Transform();

    protected boolean dirty = true;

    public abstract CoordinateSystemComponent getParentCoordinateSystem();

    protected abstract List<CoordinateSystemComponent> getChildren();

    public void markDirty() {
        dirty = true;

        for (CoordinateSystemComponent child : getChildren()) {
            child.markDirty();
        }
    }

    public Transform world() {
        if(dirty) updateGlobal();
        return worldTransform;
    }

    public Transform local() {
        return localTransform;
    }

    public void updateGlobal() {
        if (!dirty) {
            return;
        }

        CoordinateSystemComponent parent = getParentCoordinateSystem();

        if (parent == null) {
            worldTransform.position().set(localTransform.position());
            worldTransform.rotation().set(localTransform.rotation());
            dirty = false;
            return;
        }

        parent.updateGlobal();

        worldTransform.rotation()
                .set(parent.world().rotation())
                .mul(localTransform.rotation());

        Vector3f rotated = new Vector3f(localTransform.position());

        rotated.rotate(parent.world().rotation());

        worldTransform.position()
                .set(parent.world().position())
                .add(rotated);

        dirty = false;
    }

    public void forceUpdateChildren() {
        for (CoordinateSystemComponent child : getChildren()) {
            child.setDirty(true);
            child.updateGlobal();
            child.forceUpdateChildren();
        }
    }
}
