package org.daylight.sonariaworld.mixin.client;

import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.morph.*;
import org.daylight.sonariaworld.network.client.ClientMorphRotationSender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Inject(method = "aiStep", at = @At("TAIL"))
    private void morph$movementTick(CallbackInfo ci) {
        if((Object) this instanceof LocalPlayer) {
            MorphMovementController.tick((Player) (Object)this);
            ClientMorphRotationSender.tick();
        }
    }
}
