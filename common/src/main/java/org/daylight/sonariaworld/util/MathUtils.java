package org.daylight.sonariaworld.util;

import org.daylight.sonariaworld.data.coordinatesystems.Hitbox;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MathUtils {
    private static final float EPSILON = 1e-6f;

    public static boolean intersects(Hitbox a, Hitbox b) {

        Vector3f aCenter = a.world().position();
        Vector3f bCenter = b.world().position();

        Quaternionf aRot = a.world().rotation();
        Quaternionf bRot = b.world().rotation();

        Vector3f aHalf = new Vector3f(
                a.getXSize() * 0.5f,
                a.getYSize() * 0.5f,
                a.getZSize() * 0.5f
        );

        Vector3f bHalf = new Vector3f(
                b.getXSize() * 0.5f,
                b.getYSize() * 0.5f,
                b.getZSize() * 0.5f
        );

        Matrix3f aBasis = new Matrix3f().set(aRot);
        Matrix3f bBasis = new Matrix3f().set(bRot);

        Vector3f[] A = new Vector3f[3];
        Vector3f[] B = new Vector3f[3];

        A[0] = new Vector3f(aBasis.m00(), aBasis.m10(), aBasis.m20());
        A[1] = new Vector3f(aBasis.m01(), aBasis.m11(), aBasis.m21());
        A[2] = new Vector3f(aBasis.m02(), aBasis.m12(), aBasis.m22());

        B[0] = new Vector3f(bBasis.m00(), bBasis.m10(), bBasis.m20());
        B[1] = new Vector3f(bBasis.m01(), bBasis.m11(), bBasis.m21());
        B[2] = new Vector3f(bBasis.m02(), bBasis.m12(), bBasis.m22());

        float[][] R = new float[3][3];
        float[][] AbsR = new float[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                R[i][j] = A[i].dot(B[j]);
                AbsR[i][j] = Math.abs(R[i][j]) + EPSILON;
            }
        }

        Vector3f tVec = new Vector3f(bCenter).sub(aCenter);

        float[] t = new float[] {
                tVec.dot(A[0]),
                tVec.dot(A[1]),
                tVec.dot(A[2])
        };

        float ra, rb;

        // =========================
        // Axes A0 A1 A2
        // =========================

        for (int i = 0; i < 3; i++) {

            ra = aHalf.get(i);

            rb =
                    bHalf.x * AbsR[i][0] +
                            bHalf.y * AbsR[i][1] +
                            bHalf.z * AbsR[i][2];

            if (Math.abs(t[i]) > ra + rb) {
                return false;
            }
        }

        // =========================
        // Axes B0 B1 B2
        // =========================

        for (int i = 0; i < 3; i++) {

            ra =
                    aHalf.x * AbsR[0][i] +
                            aHalf.y * AbsR[1][i] +
                            aHalf.z * AbsR[2][i];

            rb = bHalf.get(i);

            float proj =
                    Math.abs(
                            t[0] * R[0][i] +
                                    t[1] * R[1][i] +
                                    t[2] * R[2][i]
                    );

            if (proj > ra + rb) {
                return false;
            }
        }

        // =========================
        // Cross products
        // =========================

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                ra =
                        aHalf.get((i + 1) % 3) * AbsR[(i + 2) % 3][j] +
                                aHalf.get((i + 2) % 3) * AbsR[(i + 1) % 3][j];

                rb =
                        bHalf.get((j + 1) % 3) * AbsR[i][(j + 2) % 3] +
                                bHalf.get((j + 2) % 3) * AbsR[i][(j + 1) % 3];

                float proj =
                        Math.abs(
                                t[(i + 2) % 3] * R[(i + 1) % 3][j]
                                        - t[(i + 1) % 3] * R[(i + 2) % 3][j]
                        );

                if (proj > ra + rb) {
                    return false;
                }
            }
        }

        return true;
    }
}
