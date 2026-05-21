package org.daylight.sonariaworld.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.daylight.sonariaworld.mixinrelated.ICreatureRotationHolder;
import org.daylight.sonariaworld.morph.*;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin implements ICreatureRotationHolder {
    @Unique
    private float sonaria$creatureYaw;

    @Override
    public float sonaria$getCreatureYaw() {
        return sonaria$creatureYaw;
    }

    @Override
    public void sonaria$setCreatureYaw(float yaw) {
        this.sonaria$creatureYaw = yaw;
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void morph$movementTick(CallbackInfo ci) {
        MorphMovementController.tick((Player) (Object)this);
    }

//
//    @Inject(method = "applyInput", at = @At("TAIL"))
//    private void sonaria$modifyInput(CallbackInfo ci) {
//        LocalPlayer player = (LocalPlayer)(Object)this;
//
//        if (!MorphData.get(player).isMorphed()) return;
//
//        float strafe = player.xxa;
//        float forward = player.zza;
//
//        Vec2 input = new Vec2(strafe, forward);
//
//        if (input.lengthSquared() < 1.0E-6F) return;
//
//        input = input.normalized();
//
//        float cameraYaw = player.getYRot();
//
//        float inputAngle = (float)Math.toDegrees(Math.atan2(input.x, input.y));
//
//        float targetYaw = cameraYaw + inputAngle;
//
//        ICreatureRotationHolder holder = (ICreatureRotationHolder) player;
//
//        float currentYaw = holder.sonaria$getCreatureYaw();
//
//        float rotateSpeed = 0.18F;
//
//        float newYaw = Mth.rotLerp(rotateSpeed, currentYaw, targetYaw);
//
//        holder.sonaria$setCreatureYaw(newYaw);
//
//        // force forward movement in creature direction
//        player.zza = 1.0F;
//        player.xxa = 0.0F;
//    }
//
//    @Inject(method = "aiStep", at = @At("TAIL"))
//    private void sonaria$syncYawToMovement(CallbackInfo ci) {
//        LocalPlayer player = (LocalPlayer)(Object)this;
//
//        if (!MorphData.get(player).isMorphed()) return;
//
//        Vec3 vel = player.getDeltaMovement();
//
//        if (vel.lengthSqr() < 1.0E-6) return;
//
//        float targetYaw = (float)(Math.atan2(-vel.x, vel.z) * Mth.RAD_TO_DEG);
//
//        ICreatureRotationHolder holder = (ICreatureRotationHolder) player;
//
//        float current = holder.sonaria$getCreatureYaw();
//
//        float smooth = 0.25F;
//
//        holder.sonaria$setCreatureYaw(
//                Mth.rotLerp(smooth, current, targetYaw)
//        );
//    }
}
