package org.daylight.sonariaworld.data.coordinatesystems;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;

public class SpatialHash3D {
    private final double cellSize;

    private final Map<Long, Set<Hitbox>> cells = new HashMap<>();
    private final Map<Hitbox, Set<Long>> objectCells = new HashMap<>();

    public SpatialHash3D(double cellSize) {
        this.cellSize = cellSize;
    }

    private long hash(int x, int y, int z) {
        long lx = ((long)x & 0x1FFFFF);
        long ly = ((long)y & 0x1FFFFF);
        long lz = ((long)z & 0x1FFFFF);

        return (lx << 42) | (ly << 21) | lz;
    }

    private int cell(double v) {
        return (int) Math.floor(v / cellSize);
    }

    private Set<Long> computeCells(AABB aabb) {
        int minX = cell(aabb.minX);
        int minY = cell(aabb.minY);
        int minZ = cell(aabb.minZ);

        int maxX = cell(aabb.maxX);
        int maxY = cell(aabb.maxY);
        int maxZ = cell(aabb.maxZ);

        Set<Long> result = new HashSet<>();

        for (int x = minX; x <= maxX; x++)
            for (int y = minY; y <= maxY; y++)
                for (int z = minZ; z <= maxZ; z++) {
                    result.add(hash(x, y, z));
                }

        return result;
    }

    public void update(Hitbox hb) {
        AABB box = computeWorldAABB(hb);

        Set<Long> newCells = computeCells(box);
        Set<Long> oldCells = objectCells.get(hb);

        if (oldCells != null) {
            for (long c : oldCells) {
                Set<Hitbox> set = cells.get(c);
                if (set != null) {
                    set.remove(hb);
                }
            }
        }

//        System.out.println("Updating hitbox: " + hb);
        for (long c : newCells) {
//            System.out.println(" -> add cell " + c);
            cells.computeIfAbsent(c, k -> new HashSet<>()).add(hb);
        }

        objectCells.put(hb, newCells);
    }

    public List<Hitbox> query(AABB range) {
        Set<Long> cells = computeCells(range);

//        System.out.println("Querying cells:");

//        for (long c : cells) {
//            System.out.println(" -> query " + c);
//        }

        List<Hitbox> result = new ArrayList<>();
        Set<Hitbox> seen = new HashSet<>();

        for (long c : cells) {
            Set<Hitbox> set = this.cells.get(c);
//            System.out.println("Checking hitboxes: " + set);
            if (set == null) continue;
//            System.out.println(set.size());

            for (Hitbox hb : set) {
                if (seen.add(hb)) {
                    result.add(hb);
                }
            }
        }

//        System.out.println("Found " + result.size() + " hitboxes: " + result);
        return result;
    }

    public static AABB computeWorldAABB(Hitbox hb) {
        Vector3f center = hb.world().position();
        Quaternionf q = hb.world().rotation();

        float hx = hb.getXSize() * 0.5f;
        float hy = hb.getYSize() * 0.5f;
        float hz = hb.getZSize() * 0.5f;

        Matrix3f m = new Matrix3f().set(q);

        float ex =
                Math.abs(m.m00()) * hx +
                        Math.abs(m.m01()) * hy +
                        Math.abs(m.m02()) * hz;

        float ey =
                Math.abs(m.m10()) * hx +
                        Math.abs(m.m11()) * hy +
                        Math.abs(m.m12()) * hz;

        float ez =
                Math.abs(m.m20()) * hx +
                        Math.abs(m.m21()) * hy +
                        Math.abs(m.m22()) * hz;

        return new AABB(
                center.x - ex,
                center.y - ey,
                center.z - ez,

                center.x + ex,
                center.y + ey,
                center.z + ez
        );
    }
}
