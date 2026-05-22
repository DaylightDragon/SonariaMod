package org.daylight.sonariaworld.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.daylight.sonariaworld.client.data.LateInitializations;
import org.daylight.sonariaworld.mixinrelated.ICreatureRotationHolder;
import org.daylight.sonariaworld.morph.MorphData;
import org.daylight.sonariaworld.morph.MorphService;
import org.daylight.sonariaworld.morph.MorphState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
//    @Inject(method = "moveRelative", at = @At("HEAD"), cancellable = true)
//    private void sonaria$moveRelative(float speed, Vec3 input, CallbackInfo ci) {
//        Entity self = (Entity)(Object)this;
//
//        if (!(self instanceof LocalPlayer player)) return;
//        if (!MorphData.get(player).isMorphed()) return;
//
//        float yaw = ((ICreatureRotationHolder) player).sonaria$getCreatureYaw();
//
//        float rad = yaw * Mth.DEG_TO_RAD;
//
//        float sin = Mth.sin(rad);
//        float cos = Mth.cos(rad);
//
//        double x = input.z * sin + input.x * cos;
//        double z = input.z * cos - input.x * sin;
//
//        Vec3 motion = new Vec3(x * speed, 0.0, z * speed);
//
//        player.setDeltaMovement(player.getDeltaMovement().add(motion));
//
//        ci.cancel();
//    }
}
