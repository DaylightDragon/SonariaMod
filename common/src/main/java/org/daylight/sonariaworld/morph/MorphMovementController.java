package org.daylight.sonariaworld.morph;

import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.daylight.sonariaworld.client.data.ClientState;
import org.daylight.sonariaworld.data.ClientMorphVisualsInfo;
import org.joml.Vector3fc;

public class MorphMovementController {

    private static final double WALK_SPEED = 0.18;
    private static final double SPRINT_SPEED = 0.28;

    private static final float ROTATION_SPEED = 0.15f;

    public static void tick(Player player) {
        if (player == null) return;
        if (player.level() == null || !player.level().isClientSide()) return;

        MorphState data = MorphStateService.get(player); // MorphData.get(player);
        if (data == null || !data.isMorphed()) return;

        LivingEntity morph = ClientMorphManager.getRenderEntity(player);
        if (morph == null) return;

        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();

        // =========================================================
        // INPUT
        // =========================================================

        Vector3fc camForwardRaw = camera.forwardVector();
        Vector3fc camLeftRaw = camera.leftVector();

        Vec3 camForward = new Vec3(
                camForwardRaw.x(),
                0,
                camForwardRaw.z()
        );

        Vec3 camLeft = new Vec3(
                camLeftRaw.x(),
                0,
                camLeftRaw.z()
        );

        if (camForward.lengthSqr() > 1e-6) {
            camForward = camForward.normalize();
        }

        if (camLeft.lengthSqr() > 1e-6) {
            camLeft = camLeft.normalize();
        }

        // third person front invert
        if (mc.options.getCameraType() == CameraType.THIRD_PERSON_FRONT) {
            camForward = camForward.scale(-1);
            camLeft = camLeft.scale(-1);
        }

        boolean w = mc.options.keyUp.isDown();
        boolean s = mc.options.keyDown.isDown();
        boolean a = mc.options.keyLeft.isDown();
        boolean d = mc.options.keyRight.isDown();

        Vec3 input = Vec3.ZERO;

        if (w) input = input.add(camForward);
        if (s) input = input.subtract(camForward);
        if (a) input = input.add(camLeft);
        if (d) input = input.subtract(camLeft);

        boolean hasInput = input.lengthSqr() > 1e-6;

        if (hasInput) {
            input = input.normalize();
        }

        // =========================================================
        // TARGET ROTATION
        // =========================================================

        ClientMorphVisualsInfo clientMorphVisualsInfo = MorphStateService.get(player).getClientMorphVisualsInfo();

        float targetYaw;

        if (hasInput) {
            targetYaw = (float) (
                    Math.toDegrees(Math.atan2(input.z, input.x))
            ) - 90f;
        } else {
            targetYaw = morph.getYRot();
        }

        float currentYaw = clientMorphVisualsInfo.getMorphYaw(); // ClientState.getClientVisualYaw();

        float newYaw = Mth.rotLerp(
                ROTATION_SPEED,
                currentYaw,
                targetYaw
        );


        clientMorphVisualsInfo.setMorphYaw0(currentYaw);
        clientMorphVisualsInfo.setMorphYaw(newYaw);

        // pitch in progress. later use that in rendering

//        ClientState.setClientVisualPrevYaw(currentYaw);
//        ClientState.setClientVisualYaw(newYaw);

//        ClientState.setClientSmoothAnimationTargetYaw(newYaw);

        MorphState state = MorphStateService.get(player);
        if (state == null) return;
        ClientMorphVisualsInfo visualsInfo = state.getClientMorphVisualsInfo();

        ClientState.setClientSmoothAnimationActive(true);

        // =========================================================
        // VANILLA MODE
        // Только визуал, без движения
        // =========================================================

        if (ClientState.getMovementMode() == ClientState.MovementMode.VANILLA) {
            return;
        }

        // =========================================================
        // MOVEMENT
        // =========================================================

        if (!hasInput) {

            Vec3 vel = player.getDeltaMovement();

            player.setDeltaMovement(
                    vel.x * 0.8,
                    vel.y,
                    vel.z * 0.8
            );

            return;
        }

        double speed = player.isSprinting()
                ? SPRINT_SPEED
                : WALK_SPEED;

        Vec3 moveDir = new Vec3(
                -Math.sin(Math.toRadians(newYaw)),
                0,
                Math.cos(Math.toRadians(newYaw))
        );

        Vec3 targetVel = moveDir.scale(speed);

        Vec3 vel = player.getDeltaMovement();

        // =========================================================
        // GROUND / AIR CONTROL
        // =========================================================

        double steering = player.onGround()
                ? 0.25
                : 0.05;

        double lerpedX = Mth.lerp(
                steering,
                vel.x,
                targetVel.x
        );

        double lerpedZ = Mth.lerp(
                steering,
                vel.z,
                targetVel.z
        );

        // НЕ трогаем Y velocity
        // иначе ломаются прыжки и падение

        player.setDeltaMovement(
                lerpedX,
                vel.y,
                lerpedZ
        );
    }
}
