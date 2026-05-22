package org.daylight.sonariaworld.mixin.client;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.LivingEntity;
import org.daylight.sonariaworld.client.data.ClientState;
import org.daylight.sonariaworld.mixinrelated.MorphRenderState;
import org.daylight.sonariaworld.morph.ClientMorphManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {
    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
            at = @At("RETURN")
    )
    private void sonaria$extractState(
            Avatar avatar,
            AvatarRenderState avatarRenderState,
            float f,
            CallbackInfo ci
    ) {
        if (!(avatar instanceof AbstractClientPlayer clientPlayer)) {
            return;
        }

        LivingEntity morph = ClientMorphManager.getRenderEntity(clientPlayer);
//        System.out.println("Morph in AvatarRendererMixin: " + morph); // c1
        if (morph == null) return;

//        sonaria$syncEntity(clientPlayer, morph); // c2

        if(avatarRenderState instanceof MorphRenderState morphRenderState) {
            morphRenderState.sonaria$setRealPlayerEntity(clientPlayer);
            morphRenderState.sonaria$setMorphEntity(morph);
        }

//        ClientState.setPartialTick(f);
    }

    @Unique
    private void sonaria$syncEntity(AbstractClientPlayer player, LivingEntity morph) {
        // position interpolation baseline
        morph.xo = morph.getX();
        morph.yo = morph.getY();
        morph.zo = morph.getZ();

        morph.setPos(player.getX(), player.getY(), player.getZ());

        // rotation interpolation baseline
        morph.yRotO = morph.getYRot();
        morph.xRotO = morph.getXRot();

        morph.setYRot(player.getYRot());
        morph.setXRot(player.getXRot());

        morph.setYBodyRot(player.yBodyRot);
        morph.yBodyRotO = player.yBodyRotO;

        morph.setYHeadRot(player.getYHeadRot());
        morph.yHeadRotO = player.yHeadRotO;

        morph.setOnGround(player.onGround());

        morph.setDeltaMovement(player.getDeltaMovement());

        morph.setPose(player.getPose());

        morph.setSprinting(player.isSprinting());

        morph.setSwimming(player.isSwimming());
    }
}
