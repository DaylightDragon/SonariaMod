package org.daylight.sonariaworld.mixin.client;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.mixinrelated.MorphRenderState;
import org.daylight.sonariaworld.morph.ClientMorphManager;
import org.daylight.sonariaworld.morph.MorphStateService;
import org.daylight.sonariaworld.morph.MorphState;
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
        if (!(avatar instanceof Player player)) {
            return;
        }

        MorphState state = MorphStateService.get(player);
        if(!state.isMorphed()) return;

        LivingEntity morph = ClientMorphManager.getRenderEntity(player);
//        System.out.println("Morph in AvatarRendererMixin: " + morph); // c1
        if (morph == null) return;

//        sonaria$syncEntity(clientPlayer, morph); // c2

        if(avatarRenderState instanceof MorphRenderState morphRenderState) {
            morphRenderState.sonaria$setRealPlayerEntity(player);
        }
        state.setMorphEntity(morph);

//        ClientState.setPartialTick(f);
    }

    @Unique
    private void sonaria$syncEntity(AbstractClientPlayer player, LivingEntity morph) {
        morph.setOnGround(player.onGround());
        morph.setPose(player.getPose());
        morph.setSprinting(player.isSprinting());
        morph.setSwimming(player.isSwimming());
    }
}
