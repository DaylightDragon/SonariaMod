package org.daylight.sonariaworld.network.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.Services;
import org.daylight.sonariaworld.mixinrelated.IdHolder;
import org.daylight.sonariaworld.morph.MorphStateService;
import org.daylight.sonariaworld.morph.MorphState;
import org.daylight.sonariaworld.network.payload.MorphRotationRequestPayload;

public class ClientMorphRotationSender {
    private static final float YAW_THRESHOLD = 1.5F;
    private static final float PITCH_THRESHOLD = 1.5F;
    private static final float HEAD_YAW_THRESHOLD = 1.5F;

    private static float lastSentYaw;
    private static float lastSentPitch;
    private static float lastSentHeadYaw;

    private static boolean initialized = false;

    private ClientMorphRotationSender() {}

    public static void tick() {
        Minecraft mc = Minecraft.getInstance();

        Player player = mc.player;

        if (player == null) {
            return;
        }

        MorphState state = MorphStateService.get(player);
        if(!state.isMorphed()) return;

        LivingEntity morph = state.getMorphEntity();
        if(morph == null) return;

        float yaw = normalize(morph.getYRot());
        float pitch = normalize(morph.getXRot());
        float headYaw = normalize(morph.getYHeadRot());

        yaw = Mth.wrapDegrees(yaw);
        pitch = Mth.wrapDegrees(pitch);
        headYaw = Mth.wrapDegrees(headYaw);

        if (!initialized) {
            forceSend(yaw, pitch, headYaw);
            initialized = true;
            return;
        }

        boolean changed =
                angleDistance(yaw, lastSentYaw) >= YAW_THRESHOLD
                        || angleDistance(pitch, lastSentPitch) >= PITCH_THRESHOLD
                        || angleDistance(headYaw, lastSentHeadYaw) >= HEAD_YAW_THRESHOLD;

        if (!changed) {
            return;
        }

        forceSend(yaw, pitch, headYaw);
    }

    private static void forceSend(
            float yaw,
            float pitch,
            float headYaw
    ) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        lastSentYaw = yaw;
        lastSentPitch = pitch;
        lastSentHeadYaw = headYaw;

        Services.CLIENT_NETWORK.sendMorphRotation(
                new MorphRotationRequestPayload(
                        ((IdHolder)player).sonaria$getId(),
                        yaw,
                        pitch,
                        headYaw
                )
        );
    }

    private static float angleDistance(float a, float b) {
        float diff = Math.abs(a - b) % 360.0F;

        return diff > 180.0F
                ? 360.0F - diff
                : diff;
    }

    private static float normalize(float angle) {
        angle %= 360.0F;

        if (angle < 0.0F) {
            angle += 360.0F;
        }

        return angle;
    }

    public static void reset() {
        initialized = false;
    }
}
