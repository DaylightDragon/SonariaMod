package org.daylight.sonariaworld.entity.hitboxes;

import org.daylight.sonariaworld.data.coordinatesystems.CoordinateSystemComponent;
import org.daylight.sonariaworld.data.coordinatesystems.Hitbox;
import org.daylight.sonariaworld.entity.CreaturePose;

import java.util.List;

public interface HitboxPresets {
    List<CoordinateSystemComponent> getHitboxes();
    void init();
    void updateForPose(CreaturePose pose);
}
