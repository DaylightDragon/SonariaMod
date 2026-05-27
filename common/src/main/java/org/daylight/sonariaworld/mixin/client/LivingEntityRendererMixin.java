package org.daylight.sonariaworld.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.daylight.sonariaworld.client.OrientedBoxGizmo;
import org.daylight.sonariaworld.data.ClientMorphVisualsInfo;
import org.daylight.sonariaworld.data.InterpolatedCoords;
import org.daylight.sonariaworld.data.coordinatesystems.CoordinateSystemComponent;
import org.daylight.sonariaworld.data.coordinatesystems.Hitbox;
import org.daylight.sonariaworld.mixinrelated.MorphRenderState;
import org.daylight.sonariaworld.morph.MorphStateService;
import org.daylight.sonariaworld.morph.MorphState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
        if (!(livingEntityRenderState instanceof AvatarRenderState avatarRenderState && livingEntityRenderState instanceof MorphRenderState morphRenderState)) return;

        Player player = (Player) morphRenderState.sonaria$getRealPlayerEntity();
        if(player == null) return;

        MorphState state = MorphStateService.get(player);
        if(!state.isMorphed()) return;

        LivingEntity morph = state.getMorphEntity(); // ClientMorphManager.getRenderEntity(player);
        if (morph == null) return;

        EntityRenderer<Entity, EntityRenderState> morphRenderer = (EntityRenderer<Entity, EntityRenderState>) entityRenderDispatcher.getRenderer(morph);

        EntityRenderState morphActualRenderState = morphRenderState.sonaria$getOrCreateCachedState(morph);

//        sonaria$syncEntity(player, morph);
//        sonaria$syncRenderState(avatarRenderState, morphActualRenderState);

//        System.out.println("Rendering with state: " + morphActualRenderState); // c1

        float partialTick = minecraft
                .getDeltaTracker()
                .getGameTimeDeltaPartialTick(false);

        float yaw;
        float headYaw;
//        System.out.println(morphRenderState + " " + player);
        ClientMorphVisualsInfo clientMorphVisualsInfo = state.getClientMorphVisualsInfo();

        yaw = Mth.rotLerp(
                partialTick,
                clientMorphVisualsInfo.getMorphYaw0(),
                clientMorphVisualsInfo.getMorphYaw()
        );
        headYaw = yaw;

        clientMorphVisualsInfo.setMorphYaw0(yaw);

        morph.setYRot(yaw);
        morph.setYHeadRot(headYaw);
        morph.setYBodyRot(yaw);

        morph.yRotO = yaw;
        morph.yHeadRotO = headYaw;
        morph.yBodyRotO = yaw;

//        System.out.println("player: " + player.getName() + ", yaw: " + yaw);

        morphActualRenderState.lightCoords = livingEntityRenderState.lightCoords;

        morphRenderer.submit(
                morphActualRenderState,
                poseStack,
                submitNodeCollector,
                cameraRenderState
        );

        Vec3 currentPlayerPosition = player.getPosition(partialTick);
        Vec3 currentPlayerRotation = new Vec3(player.getXRot(partialTick), player.getYRot(partialTick), 0);

        InterpolatedCoords realPlayerCoords = state.getRealPlayerCoords();
        realPlayerCoords.setWorld(player.level());

        realPlayerCoords.setX0(realPlayerCoords.getX());
        realPlayerCoords.setY0(realPlayerCoords.getY());
        realPlayerCoords.setZ0(realPlayerCoords.getZ());
        realPlayerCoords.setYaw0(realPlayerCoords.getYaw());
        realPlayerCoords.setPitch0(realPlayerCoords.getPitch());

        realPlayerCoords.setX(currentPlayerPosition.x());
        realPlayerCoords.setY(currentPlayerPosition.y());
        realPlayerCoords.setZ(currentPlayerPosition.z());
        realPlayerCoords.setYaw(realPlayerCoords.getYaw());
        realPlayerCoords.setPitch(realPlayerCoords.getPitch());

        if(isOverMovementShreshold(realPlayerCoords)) {
            ClientMorphVisualsInfo visualsInfo = state.getClientMorphVisualsInfo();
            if(visualsInfo.getHitboxHolder() != null) {
                visualsInfo.setDirty(true);
                visualsInfo.updateHitboxes();
            }
        }

        if(minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.ENTITY_HITBOXES)) {
            renderHitboxes(player, state, morphActualRenderState, poseStack, submitNodeCollector, cameraRenderState);
        }

        ci.cancel();
    }

    @Unique
    private boolean isOverMovementShreshold(InterpolatedCoords coords) {
        double movementThreshold = 0.0004d;
        float rotationDegreesThreshold = 0.01f;
        if(Math.abs(coords.getX() - coords.getX0()) >= movementThreshold) return true;
        if(Math.abs(coords.getY() - coords.getY0()) >= movementThreshold) return true;
        if(Math.abs(coords.getZ() - coords.getZ0()) >= movementThreshold) return true;
        if(Math.abs(coords.getYaw() - coords.getYaw0()) >= rotationDegreesThreshold) return true;
        if(Math.abs(coords.getPitch() - coords.getPitch0()) >= rotationDegreesThreshold) return true;
        return false;
    }

    private void renderHitboxes(Player player, MorphState state, EntityRenderState morphActualRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        ClientMorphVisualsInfo visualsInfo = state.getClientMorphVisualsInfo();
        if(!state.isMorphed()) return;
        if(visualsInfo.getHitboxHolder().isEmpty()) return;

        for(CoordinateSystemComponent component : visualsInfo.getHitboxHolder().get().getHitboxes()) {
            if(component instanceof Hitbox hitbox) {
                Gizmos.addGizmo(OrientedBoxGizmo.fromHitbox(player, hitbox));
            }
        }
    }
}
