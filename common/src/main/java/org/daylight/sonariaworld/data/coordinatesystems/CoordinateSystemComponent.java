package org.daylight.sonariaworld.data.coordinatesystems;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public abstract class CoordinateSystemComponent {
    // local
    protected final Transform localTransform = new Transform();

    // cached world
    protected final Transform worldTransform = new Transform();

    protected boolean dirty = true;

    public abstract Optional<CoordinateSystemComponent> getParentCoordinateSystem();

    protected abstract List<CoordinateSystemComponent> getChildren();

    public void markDirty() {
        dirty = true;

        for (CoordinateSystemComponent child : getChildren()) {
            child.markDirty();
        }
    }

    public Transform world() {
        return worldTransform;
    }

    public Transform local() {
        return localTransform;
    }

    public void debugPrint() {
        if(!getClass().equals(Hitbox.class)) return;

        System.out.println("=== " + getClass().getSimpleName() + " ===");

        System.out.println("Dirty: " + dirty);

        System.out.println("Local Pos: " + local().position());
        System.out.println("Local Rot: " + local().rotation());

        System.out.println("World Pos: " + world().position());
        System.out.println("World Rot: " + world().rotation());

        System.out.println("Parent: " + getParentCoordinateSystem());
    }

    public void updateGlobal() {
        if (!dirty) {
            return;
        }

//        debugPrint();

//        System.out.println("Updating: " + this);

        Optional<CoordinateSystemComponent> parent = getParentCoordinateSystem();

        if (parent.isEmpty()) {
            worldTransform.position().set(localTransform.position());
            worldTransform.rotation().set(localTransform.rotation());
            dirty = false;

//            System.out.println("(Early) Result world pos: " + world().position());
//            System.out.println("(Early) Result world rot: " + world().rotation());
            return;
        }

        parent.get().updateGlobal();

        worldTransform.rotation()
                .set(parent.get().world().rotation())
                .mul(localTransform.rotation());

        Vector3f rotated = new Vector3f(localTransform.position());

        rotated.rotate(parent.get().world().rotation());

        worldTransform.position()
                .set(parent.get().world().position())
                .add(rotated);

//        System.out.println("Result world pos: " + world().position());
//        System.out.println("Result world rot: " + world().rotation());

        dirty = false;
    }

//    public void forceUpdateChildren() {
//        for (CoordinateSystemComponent child : getChildren()) {
//            child.setDirty(true);
//            child.updateGlobal();
//            child.forceUpdateChildren();
//        }
//    }

    public abstract Level getWorld();
    public abstract CoordinateSystemComponent getRoot();
}
