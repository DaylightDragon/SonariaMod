package org.daylight.sonariaworld.data.systems;

import net.minecraft.world.level.Level;
import org.daylight.sonariaworld.data.coordinatesystems.SpatialHash3D;

import java.util.HashMap;
import java.util.Map;

public class ServerSpatialSystems {
    private static final Map<Level, SpatialHash3D> WORLD_SPATIAL_HASHES = new HashMap<>();
    private static final double cellSize = 4;

    private ServerSpatialSystems() {}

    public static SpatialHash3D getSpatialHash(Level level) {
        return WORLD_SPATIAL_HASHES.computeIfAbsent(level, lvl -> new SpatialHash3D(cellSize));
    }

    public static void fullCleanup() {
        WORLD_SPATIAL_HASHES.clear();
    }
}
