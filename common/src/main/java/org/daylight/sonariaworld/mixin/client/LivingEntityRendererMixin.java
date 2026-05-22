package org.daylight.sonariaworld.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.client.data.ClientState;
import org.daylight.sonariaworld.mixinrelated.MorphRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"rawtypes", "unchecked"})
@Mixin(value = LivingEntityRenderer.class, priority = 1100)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> {
    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sonaria$renderMorph(
            S livingEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci
    ) {
        Minecraft minecraft = Minecraft.getInstance();
//        System.out.println((livingEntityRenderState instanceof AvatarRenderState) + " " + (livingEntityRenderState instanceof MorphRenderState));
        if (!(livingEntityRenderState instanceof AvatarRenderState avatarRenderState && livingEntityRenderState instanceof MorphRenderState morphRenderState)) return;
        Player player = (Player) morphRenderState.sonaria$getRealPlayerEntity();
//        System.out.println("Player: " + player); // c1
        if(player == null) return;

        LivingEntity morph = morphRenderState.sonaria$getMorphEntity(); // ClientMorphManager.getRenderEntity(player);
//        System.out.println("Morph: " + morph); // c1

        if (morph == null) {
            return;
        }

        EntityRenderer<Entity, EntityRenderState> morphRenderer = (EntityRenderer<Entity, EntityRenderState>) entityRenderDispatcher.getRenderer(morph);

        EntityRenderState morphActualRenderState = morphRenderState.sonaria$getOrCreateCachedState(morph);

//        sonaria$syncEntity(player, morph);
//        sonaria$syncRenderState(avatarRenderState, morphActualRenderState);

//        System.out.println("Rendering with state: " + morphActualRenderState); // c1

        float partialTick = Minecraft.getInstance()
                .getDeltaTracker()
                .getGameTimeDeltaPartialTick(false);

        float yaw = Mth.lerp(
                partialTick,
                ClientState.getClientSmoothAnimationCurrentYaw(),
                ClientState.getClientSmoothAnimationTargetYaw()
        );
        morph.setYRot(yaw);
        morph.setYBodyRot(yaw);
        morph.setYHeadRot(yaw);

        ClientState.setClientSmoothAnimationCurrentYaw(yaw);

        morphRenderer.submit(
                morphActualRenderState,
                poseStack,
                submitNodeCollector,
                cameraRenderState
        );

        ci.cancel();
    }

    @Unique
    private void sonaria$syncEntity(
            Player player,
            LivingEntity morph
    ) {

        morph.setPos(
                player.getX(),
                player.getY(),
                player.getZ()
        );

        morph.xo = player.xo;
        morph.yo = player.yo;
        morph.zo = player.zo;

        morph.setYRot(player.getYRot());
        morph.setXRot(player.getXRot());

        morph.yRotO = player.yRotO;
        morph.xRotO = player.xRotO;

        morph.setYBodyRot(player.yBodyRot);
        morph.yBodyRotO = player.yBodyRotO;

        morph.setYHeadRot(player.getYHeadRot());
        morph.yHeadRotO = player.yHeadRotO;

        morph.tickCount = player.tickCount;

        morph.setPose(player.getPose());

        morph.setSprinting(player.isSprinting());

        morph.setShiftKeyDown(player.isShiftKeyDown());

        morph.setSwimming(player.isSwimming());

        morph.setOnGround(player.onGround());

        morph.walkAnimation.setSpeed(player.walkAnimation.speed());
        morph.walkAnimation.position(player.walkAnimation.position());

        morph.setDeltaMovement(player.getDeltaMovement());
    }

    @Unique
    private void sonaria$syncRenderState(
            AvatarRenderState playerState,
            EntityRenderState morphState
    ) {

        morphState.x = playerState.x;
        morphState.y = playerState.y;
        morphState.z = playerState.z;

        morphState.ageInTicks = playerState.ageInTicks;

        morphState.shadowRadius = playerState.shadowRadius;

        morphState.lightCoords = playerState.lightCoords;

        if (morphState instanceof LivingEntityRenderState livingState) {

            livingState.bodyRot = playerState.bodyRot;

            livingState.yRot = playerState.yRot;

            livingState.xRot = playerState.xRot;

            livingState.walkAnimationPos =
                    playerState.walkAnimationPos;

            livingState.walkAnimationSpeed =
                    playerState.walkAnimationSpeed;

            livingState.pose = playerState.pose;

            livingState.isInWater =
                    playerState.isInWater;

            livingState.deathTime =
                    playerState.deathTime;

            livingState.scale =
                    playerState.scale;
        }
    }
}
