package org.daylight.sonariaworld.neoforge.events;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.EntityEvent;
import org.daylight.sonariaworld.Services;
import org.daylight.sonariaworld.morph.*;
import org.daylight.sonariaworld.network.payload.PlayerAttackPayload;

public class ClientEvents {
    @SubscribeEvent
    public static void onSize(EntityEvent.Size event) {
        if (event.getEntity() instanceof Player player) {
            MorphState state = MorphStateService.get(player);
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
            Services.CLIENT_NETWORK.sendAttackRequest(
                    new PlayerAttackPayload()
            );
//            System.out.println("Attack");
        }
    }
}
