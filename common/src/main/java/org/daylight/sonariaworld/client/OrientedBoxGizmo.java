package org.daylight.sonariaworld.client;

import net.minecraft.gizmos.Gizmo;
import net.minecraft.gizmos.GizmoPrimitives;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.daylight.sonariaworld.data.coordinatesystems.Hitbox;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public record OrientedBoxGizmo(
        Vec3 center,
        Vector3f halfSize,
        Quaternionf rotation,
        GizmoStyle style
) implements Gizmo {
    @Override
    public void emit(GizmoPrimitives p, float tickDelta) {

        Vector3f[] corners = new Vector3f[] {
                new Vector3f(-halfSize.x, -halfSize.y, -halfSize.z),
                new Vector3f( halfSize.x, -halfSize.y, -halfSize.z),
                new Vector3f( halfSize.x,  halfSize.y, -halfSize.z),
                new Vector3f(-halfSize.x,  halfSize.y, -halfSize.z),

                new Vector3f(-halfSize.x, -halfSize.y,  halfSize.z),
                new Vector3f( halfSize.x, -halfSize.y,  halfSize.z),
                new Vector3f( halfSize.x,  halfSize.y,  halfSize.z),
                new Vector3f(-halfSize.x,  halfSize.y,  halfSize.z),
        };

        Vector3f[] world = new Vector3f[8];

        for (int i = 0; i < 8; i++) {
            Vector3f v = new Vector3f(corners[i]);
            v.rotate(rotation);
            v.add((float) center.x, (float) center.y, (float) center.z);
            world[i] = v;
        }

        edge(p, world, 0, 1);
        edge(p, world, 1, 2);
        edge(p, world, 2, 3);
        edge(p, world, 3, 0);

        edge(p, world, 4, 5);
        edge(p, world, 5, 6);
        edge(p, world, 6, 7);
        edge(p, world, 7, 4);

        edge(p, world, 0, 4);
        edge(p, world, 1, 5);
        edge(p, world, 2, 6);
        edge(p, world, 3, 7);
    }

    private void edge(GizmoPrimitives p, Vector3f[] w, int a, int b) {
        p.addLine(
                new Vec3(w[a]),
                new Vec3(w[b]),
                style.multipliedStroke(1.0f),
                style.strokeWidth()
        );
    }

    public static Gizmo fromHitbox(Player player, Hitbox hitbox) {
        int color;
        float width;

        switch (hitbox.getHitboxType()) {
            case DAMAGE_ABSORPTION -> {
                color = ARGB.colorFromFloat(1.0F, 0.0F, 1.0F, 0.0F);
                width = 1.5f;
            }
            case ATTACK -> {
                color = ARGB.colorFromFloat(1.0F, 1.0F, 0.55F, 0.0F);
                width = 1.5f;
            }
            default -> {
                color = ARGB.colorFromFloat(1.0F, 1.0F, 1.0F, 1.0F);
                width = 1.5f;
            }
        }

//        System.out.println(hitbox.getHitboxType() + " " + color + " " + width);

//        Vector3f center = new Vector3f(
//                (float) (hitbox.world().position().x + player.getBoundingBox().getXsize() / 2),
//                (float) (hitbox.world().position().y + player.getBoundingBox().getYsize() / 2),
//                (float) (hitbox.world().position().z + player.getBoundingBox().getZsize() / 2)
//        );

        Vector3f center = new Vector3f(
                (float) hitbox.world().position().x,
                (float) hitbox.world().position().y,
                (float) hitbox.world().position().z
        );

        Quaternionf rotation = new Quaternionf(hitbox.world().rotation());

        Vector3f halfSize = new Vector3f(
                hitbox.getXSize() * 0.5f,
                hitbox.getYSize() * 0.5f,
                hitbox.getZSize() * 0.5f
        );

        return new OrientedBoxGizmo(
                new Vec3(center),
                halfSize,
                rotation,
                GizmoStyle.stroke(color)
        );
    }
}
