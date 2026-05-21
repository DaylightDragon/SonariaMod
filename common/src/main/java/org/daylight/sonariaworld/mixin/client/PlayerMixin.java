package org.daylight.sonariaworld.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.daylight.sonariaworld.morph.MorphData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
//    @Inject(
//            method = "travel",
//            at = @At("HEAD"),
//            cancellable = true
//    )
//    private void sonaria$customTravel(
//            Vec3 input,
//            CallbackInfo ci
//    ) {
//        if(!((Object) this instanceof LocalPlayer)) return;
//
//        LocalPlayer player = (LocalPlayer)(Object)this;
//
//        if (!MorphData.get(player).isMorphed()) {
//            return;
//        }
//
//        // input.z = W/S
//        // input.x = A/D
//
//        float forward = (float) input.z;
//        float strafe = (float) input.x;
//
//        // пример:
//        // отключаем strafe полностью
//        // и делаем forward сильнее
//
//        Vec3 modified = new Vec3(
//                0.0,
//                input.y,
//                forward * 0.3F
//        );
//
//        player.setDeltaMovement(
//                player.getDeltaMovement().add(
//                        modified
//                )
//        );
//
//        ci.cancel();
//    }
}
