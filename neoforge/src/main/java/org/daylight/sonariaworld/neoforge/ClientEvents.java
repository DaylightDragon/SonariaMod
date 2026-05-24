package org.daylight.sonariaworld.neoforge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.event.entity.EntityEvent;
import org.daylight.sonariaworld.SonariaWorld;
import org.daylight.sonariaworld.client.SonariaHitboxLayer;
import org.daylight.sonariaworld.client.data.ClientState;
import org.daylight.sonariaworld.entity.species.OlatuaEntity;
import org.daylight.sonariaworld.morph.*;
import org.daylight.sonariaworld.network.client.ClientMorphApi;
import org.daylight.sonariaworld.registry.EntityRegistry;

//@EventBusSubscriber(
//        modid = SonariaWorld.MOD_ID,
//        value = Dist.CLIENT
//)
public class ClientEvents {
//    @SubscribeEvent
//    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
//        EntityRenderer<?, ?> renderer =
//                event.getRenderer(EntityRegistry.OLATUA.get());
//
//        if (renderer instanceof LivingEntityRenderer livingRenderer) {
//            livingRenderer.addLayer(new SonariaHitboxLayer<LivingEntityRenderState>(livingRenderer));
//        }
//    }

    @SubscribeEvent
    public static void onSize(EntityEvent.Size event) {
        if (event.getEntity() instanceof Player player) {
            MorphState state = MorphService.get(player);
            if(player.level() != null) System.out.println("onSize called - Morphed: " + state.isMorphed() +
//                    " Entity: " + player.getLivingEntity().getDisplayName() +
                    " Side: " + (player.level().isClientSide() ? "CLIENT" : "SERVER"));

            EntityDimensions result;

            if (state.isMorphed()) {
                result = MorphDimensions.get(
                        BuiltInRegistries.ENTITY_TYPE.getValue(state.getEntityIdentifier()),
                        player.getPose()
                );
            } else {
                result = MorphDimensions.getNormalPlayerDimensions();
            }
            state.setRealPlayerDimensions(result);
            event.setNewSize(result);
        }
    }

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        for (Player player : mc.level.players()) {
            LivingEntity morph = ClientMorphManager.getRenderEntity(player);
            if (morph == null) continue;
//            if(!(player instanceof LocalPlayer)) sync(player, morph);
//            System.out.println("tick");
//            morph.tick();

            morph.tickCount++;

            morph.xOld = morph.getX();
            morph.yOld = morph.getY();
            morph.zOld = morph.getZ();

//            morph.xRotO = morph.getXRot();
//            morph.yRotO = morph.getYRot();

            morph.aiStep();

            if (morph instanceof LivingEntity living) {
                float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);

                Vec3 vel = player.getDeltaMovement();
                float speed = (float)Math.sqrt(vel.x * vel.x + vel.z * vel.z);
                living.walkAnimation.update(speed, speed, partialTick);
            }
        }

//        MorphMovementController.tick(mc.player);
    }

    @SubscribeEvent
    public static void onAttack(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (event.isAttack()) {
            System.out.println("Attack event");
            ClientMorphApi.onAttack();
//            System.out.println("Attack");
        }
    }

    public static void sync(Player player, LivingEntity morph) {
        // previous interpolation values
        morph.xo = morph.getX();
        morph.yo = morph.getY();
        morph.zo = morph.getZ();

        morph.setPos(player.getX(), player.getY(), player.getZ());

        morph.yRotO = morph.getYRot();
        morph.xRotO = morph.getXRot();

        morph.setYRot(player.getYRot());
        morph.setXRot(player.getXRot());

        morph.yBodyRotO = player.yBodyRotO;
        morph.yHeadRotO = player.yHeadRotO;

        // current position
        morph.setPos(
                player.getX(),
                player.getY(),
                player.getZ()
        );

        // rotations
        morph.setYRot(player.getYRot());
        morph.setXRot(player.getXRot());

        morph.setYBodyRot(player.yBodyRot);
        morph.setYHeadRot(player.getYHeadRot());

        // movement
        morph.setDeltaMovement(player.getDeltaMovement());

        // states
        morph.setPose(player.getPose());

        morph.setSprinting(player.isSprinting());

        morph.setSwimming(player.isSwimming());

        morph.setShiftKeyDown(player.isShiftKeyDown());

        morph.setOnGround(player.onGround());

        // hurt/death animation
        morph.hurtTime = player.hurtTime;
        morph.deathTime = player.deathTime;

        // fire/freeze states
        morph.setRemainingFireTicks(player.getRemainingFireTicks());

        morph.setTicksFrozen(player.getTicksFrozen());

        // air
        morph.setAirSupply(player.getAirSupply());

        // swing animation
        morph.swinging = player.swinging;
        morph.swingTime = player.swingTime;

        // using item
        if (player.isUsingItem()) {
            morph.startUsingItem(player.getUsedItemHand());
        } else {
            morph.stopUsingItem();
        }

        // walk animation
        morph.walkAnimation.setSpeed(player.walkAnimation.speed());
        morph.walkAnimation.position(player.walkAnimation.position());

        // age
        morph.tickCount = player.tickCount;
    }
}
