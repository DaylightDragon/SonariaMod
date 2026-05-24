package org.daylight.sonariaworld.entity.hitboxes.species;

import org.daylight.sonariaworld.data.coordinatesystems.CoordinateSystemComponent;
import org.daylight.sonariaworld.data.coordinatesystems.Hitbox;
import org.daylight.sonariaworld.entity.CreaturePose;
import org.daylight.sonariaworld.entity.hitboxes.HitboxPresets;

import java.util.ArrayList;
import java.util.List;

public class OlatuaHitboxes implements HitboxPresets {
    private boolean initialized = false;
    private List<CoordinateSystemComponent> hitboxes = new ArrayList<>();
    private Hitbox hb1;

    @Override
    public List<CoordinateSystemComponent> getHitboxes() {
        return hitboxes;
    }

    public void init(CoordinateSystemComponent parentSystem) {
        if(initialized) return;

        hb1 = new Hitbox(parentSystem, Hitbox.HitboxType.DAMAGE_ABSORPTION);
        hb1.setLocalPosition(0, 0, 0); // here because is static
        hb1.setLocalRotation(0, 0, 0); // here because is static

        hitboxes.add(hb1);

        updateForPose(CreaturePose.STANDING);

        initialized = true;
    }

    public void updateForPose(CreaturePose pose) {
        switch (pose) {
            case STANDING, WALKING -> {
                hb1.setSize(1, 1, 3);
            }
            case LYING -> {
                hb1.setSize(1, 0.8f, 3);
            }
            case SITTING -> {
                hb1.setSize(1, 0.6f, 3);
            }
            default -> {
                hb1.setSize(1, 1, 3);
            }
        }
    }
}
