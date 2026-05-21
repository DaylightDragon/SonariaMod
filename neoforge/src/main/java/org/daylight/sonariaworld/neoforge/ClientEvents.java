package org.daylight.sonariaworld.neoforge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.event.entity.EntityEvent;
import org.daylight.sonariaworld.SonariaWorld;
import org.daylight.sonariaworld.client.data.ClientState;
import org.daylight.sonariaworld.mixin.client.WalkAnimationStateAccessor;
import org.daylight.sonariaworld.morph.ClientMorphManager;
import org.daylight.sonariaworld.morph.MorphMovementController;
import org.daylight.sonariaworld.morph.MorphService;
import org.daylight.sonariaworld.morph.MorphState;

//@EventBusSubscriber(
//        modid = SonariaWorld.MOD_ID,
//        value = Dist.CLIENT
//)
public class ClientEvents {
    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre<?> event) {
//        System.out.println("Render");
    }

    @SubscribeEvent
    public static void onSize(EntityEvent.Size event) {
        if (event.getEntity() instanceof Player player) {
            MorphState state = MorphService.get(player);
            System.out.println("onSize called - Morphed: " + state.isMorphed() +
//                    " Entity: " + player.getLivingEntity().getDisplayName() +
                    " Side: " + (player.level().isClientSide() ? "CLIENT" : "SERVER"));

            if (state.isMorphed()) {
                event.setNewSize(EntityDimensions.scalable(0.9F, 0.8F));
            } else {
                event.setNewSize(EntityDimensions.scalable(0.6F, 1.8F));
            }
        }
    }

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        for (Player player : mc.level.players()) {
            LivingEntity morph = ClientMorphManager.getRenderEntity(player);
            if (morph == null) continue;
            if(!(player instanceof LocalPlayer)) sync(player, morph);
//            System.out.println("tick");
//            morph.tick();

            morph.tickCount++;

            morph.xOld = morph.getX();
            morph.yOld = morph.getY();
            morph.zOld = morph.getZ();

            morph.xRotO = morph.getXRot();
            morph.yRotO = morph.getYRot();

            morph.aiStep();

            if (morph instanceof LivingEntity living) {
                Vec3 vel = player.getDeltaMovement();
                float speed = (float)Math.sqrt(vel.x * vel.x + vel.z * vel.z);
                living.walkAnimation.update(speed, speed, ClientState.getPartialTick());
            }
        }

//        MorphMovementController.tick(mc.player);
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
