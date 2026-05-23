package org.daylight.sonariaworld.data.coordinatesystems;

import org.joml.Quaternionf;
import org.joml.Vector3f;


public final class Transform {
    private final Vector3f position = new Vector3f();
    private final Quaternionf rotation = new Quaternionf();

    public Vector3f position() {
        return position;
    }

    public Quaternionf rotation() {
        return rotation;
    }
}
