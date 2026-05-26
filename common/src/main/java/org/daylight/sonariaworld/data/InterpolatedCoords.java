package org.daylight.sonariaworld.data;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.world.level.Level;

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
    private Level world;
}
