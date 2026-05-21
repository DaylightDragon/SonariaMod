package org.daylight.sonariaworld.morph;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.daylight.sonariaworld.client.data.ClientState;
import org.joml.Vector3fc;

public class MorphMovementController {
    private static final double WALK_SPEED = 0.18;
    private static final double SPRINT_SPEED = 0.28;

    private static final float ROTATION_SPEED = 0.12f; // lerp factor

    public static void tick(Player player) {
        ClientState.setClientSmoothAnimationActive(false);

        if(ClientState.getMovementMode() == ClientState.MovementMode.VANILLA) return;
        if (player == null) return;
        if (player.level().isClientSide() == false) return;

        MorphState data = MorphData.get(player);
        if (data == null || !data.isMorphed()) return;

        LivingEntity morph = ClientMorphManager.getRenderEntity(player);
        if (morph == null) return;

        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();

        // -----------------------------
        // 1. INPUT (WASD → camera space)
        // -----------------------------
        Vector3fc camF = camera.forwardVector();
        Vector3fc camL = camera.leftVector();

        boolean w = mc.options.keyUp.isDown();
        boolean s = mc.options.keyDown.isDown();
        boolean a = mc.options.keyLeft.isDown();
        boolean d = mc.options.keyRight.isDown();

        Vec3 input = Vec3.ZERO;

        if (w) input = input.add(new Vec3(camF.x(), camF.y(), camF.z()));
        if (s) input = input.subtract(new Vec3(camF.x(), camF.y(), camF.z()));
        if (a) input = input.add(new Vec3(camL.x(), camL.y(), camL.z()));
        if (d) input = input.subtract(new Vec3(camL.x(), camL.y(), camL.z()));

        if (input.lengthSqr() > 1e-6) {
            input = input.normalize();
        }

        // -----------------------------
        // 2. MORPH BASIS
        // -----------------------------
        Vec3 forward = morph.getLookAngle().normalize();
        Vec3 up = new Vec3(0, 1, 0);

        Vec3 right = up.cross(forward).normalize();

        // -----------------------------
        // 3. INPUT → MORPH SPACE
        // -----------------------------
        double forwardComp = input.dot(forward);
        double rightComp = input.dot(right);

        Vec3 desiredDir = forward.scale(forwardComp)
                .add(right.scale(rightComp));

        if (desiredDir.lengthSqr() > 1e-6) {
            desiredDir = desiredDir.normalize();
        }

        // -----------------------------
        // 4. TARGET YAW
        // -----------------------------
        float targetYaw;
        if (desiredDir.lengthSqr() > 1e-6) {
            targetYaw = (float)(Math.atan2(desiredDir.z, desiredDir.x) * (180 / Math.PI)) - 90f;
        } else {
            targetYaw = morph.getYRot();
        }

        // -----------------------------
        // 5. SMOOTH ROTATION
        // -----------------------------
        float currentYaw = morph.getYRot();
        float newYaw = Mth.rotLerp(ROTATION_SPEED, currentYaw, targetYaw);

        ClientState.setClientSmoothAnimationTargetYaw(newYaw);
        ClientState.setClientSmoothAnimationActive(true);

        if(Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
            morph.setYRot(newYaw);
            morph.setYBodyRot(newYaw);
            morph.setYHeadRot(newYaw);
        }

        // -----------------------------
        // 6. MOVEMENT (stable forward motion)
        // -----------------------------
        boolean hasInput = input.lengthSqr() > 1e-6;

        if (!hasInput) {
            Vec3 vel = player.getDeltaMovement();
            player.setDeltaMovement(vel.multiply(0.8, 1.0, 0.8)); // затухание без движения вперёд
            return;
        }

        double speed = player.isSprinting() ? SPRINT_SPEED : WALK_SPEED;

        Vec3 moveDir = new Vec3(
                -Math.sin(Math.toRadians(newYaw)),
                0,
                Math.cos(Math.toRadians(newYaw))
        );

        Vec3 targetVel = moveDir.scale(speed);

        player.setDeltaMovement(player.getDeltaMovement().lerp(targetVel, 0.25));

        // optional: чтобы не было ванильного трения конфликта
        player.fallDistance = 0;
    }
}
