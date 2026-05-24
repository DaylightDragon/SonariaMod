package org.daylight.sonariaworld.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.daylight.sonariaworld.client.OrientedBoxGizmo;
import org.daylight.sonariaworld.data.coordinatesystems.CoordinateSystemComponent;
import org.daylight.sonariaworld.data.coordinatesystems.Hitbox;
import org.daylight.sonariaworld.mixinrelated.MorphRenderState;
import org.daylight.sonariaworld.morph.MorphService;
import org.daylight.sonariaworld.morph.MorphState;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
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
        if (!(livingEntityRenderState instanceof AvatarRenderState avatarRenderState && livingEntityRenderState instanceof MorphRenderState morphRenderState)) return;

        Player player = (Player) morphRenderState.sonaria$getRealPlayerEntity();
        if(player == null) return;

        MorphState state = MorphService.get(player);
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
        MorphState.MorphVisualsInfo morphVisualsInfo = state.getMorphVisualsInfo();

        yaw = Mth.rotLerp(
                partialTick,
                morphVisualsInfo.getMorphYaw0(),
                morphVisualsInfo.getMorphYaw()
        );
        headYaw = yaw;

        morphVisualsInfo.setMorphYaw0(yaw);

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

        MorphState.InterpolatedCoords realPlayerCoords = state.getRealPlayerCoords();
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
            MorphState.MorphVisualsInfo visualsInfo = state.getMorphVisualsInfo();
            if(visualsInfo.getHitboxHolder() != null) {
                visualsInfo.setDirty(true);
                visualsInfo.updateHitboxes();
            }
        }

        renderHitboxes(player, state, morphActualRenderState, poseStack, submitNodeCollector, cameraRenderState);

        ci.cancel();
    }

//    @Unique
//    private Vec3 sonaria$prevPos = new Vec3(0.0F, 0.0F, 0.0F);
//    private Vec3 sonaria$prevRot = new Vec3(0.0F, 0.0F, 0.0F);

    @Unique
    private boolean isOverMovementShreshold(MorphState.InterpolatedCoords coords) {
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
        MorphState.MorphVisualsInfo visualsInfo = state.getMorphVisualsInfo();
        if(!state.isMorphed()) return;
        if(visualsInfo.getHitboxHolder() == null) return;

        for(CoordinateSystemComponent component : visualsInfo.getHitboxHolder().getHitboxes()) {
            if(component instanceof Hitbox hitbox) {
                Gizmos.addGizmo(OrientedBoxGizmo.fromHitbox(player, hitbox));
            }
        }
    }

    @Deprecated
    private static void sonaria$renderOBB(Hitbox box, PoseStack stack, VertexConsumer vc) {
        Vector3f half = new Vector3f(
                box.getXSize() * 0.5f,
                box.getYSize() * 0.5f,
                box.getZSize() * 0.5f
        );

        Vector3f[] c = new Vector3f[] {
                new Vector3f(-half.x, -half.y, -half.z),
                new Vector3f( half.x, -half.y, -half.z),
                new Vector3f(-half.x,  half.y, -half.z),
                new Vector3f( half.x,  half.y, -half.z),

                new Vector3f(-half.x, -half.y,  half.z),
                new Vector3f( half.x, -half.y,  half.z),
                new Vector3f(-half.x,  half.y,  half.z),
                new Vector3f( half.x,  half.y,  half.z)
        };

        Quaternionf rot = box.world().rotation();
        Vector3f center = box.world().position();

        for (Vector3f v : c) {
            v.rotate(rot);
            v.add(center);
        }

        Matrix4f mat = stack.last().pose();

        sonaria$draw(vc, mat, c[0], c[1]);
        sonaria$draw(vc, mat, c[1], c[3]);
        sonaria$draw(vc, mat, c[3], c[2]);
        sonaria$draw(vc, mat, c[2], c[0]);

        sonaria$draw(vc, mat, c[4], c[5]);
        sonaria$draw(vc, mat, c[5], c[7]);
        sonaria$draw(vc, mat, c[7], c[6]);
        sonaria$draw(vc, mat, c[6], c[4]);

        sonaria$draw(vc, mat, c[0], c[4]);
        sonaria$draw(vc, mat, c[1], c[5]);
        sonaria$draw(vc, mat, c[2], c[6]);
        sonaria$draw(vc, mat, c[3], c[7]);
    }

    @Deprecated
    private static void sonaria$draw(VertexConsumer vc, Matrix4f mat, Vector3f a, Vector3f b) {
        vc.addVertex(mat, a.x, a.y, a.z);
        vc.addVertex(mat, b.x, b.y, b.z);
    }

    @Unique
    private void sonaria$syncEntity(Player player, LivingEntity morph) {
        morph.tickCount = player.tickCount;
        morph.setPose(player.getPose());
        morph.setSprinting(player.isSprinting());
        morph.setShiftKeyDown(player.isShiftKeyDown());
        morph.setSwimming(player.isSwimming());
        morph.setOnGround(player.onGround());
    }

    @Unique
    private void sonaria$syncRenderState(AvatarRenderState playerState, EntityRenderState morphState) {
        morphState.ageInTicks = playerState.ageInTicks;
        morphState.shadowRadius = playerState.shadowRadius;
        morphState.lightCoords = playerState.lightCoords;
        if (morphState instanceof LivingEntityRenderState livingState) {
            livingState.pose = playerState.pose;
            livingState.isInWater = playerState.isInWater;
            livingState.deathTime = playerState.deathTime;
            livingState.scale = playerState.scale;
        }
    }
}
