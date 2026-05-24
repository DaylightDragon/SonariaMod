package org.daylight.sonariaworld.data;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.daylight.sonariaworld.data.coordinatesystems.SpatialHash3D;
import org.daylight.sonariaworld.mixinrelated.IdHolder;

import java.util.HashMap;
import java.util.Map;

public class ServerSpatialSystems {
    private static final Map<Level, SpatialHash3D> WORLD_SPATIAL_HASHES = new HashMap<>();
    private static final double cellSize = 4;

    private ServerSpatialSystems() {}

    public static SpatialHash3D getSpatialHash(Level level) {
        return WORLD_SPATIAL_HASHES.computeIfAbsent(level, lvl -> new SpatialHash3D(cellSize));
    }
}
