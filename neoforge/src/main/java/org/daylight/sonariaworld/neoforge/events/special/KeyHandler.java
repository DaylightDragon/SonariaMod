package org.daylight.sonariaworld.neoforge.events.special;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.daylight.sonariaworld.Services;
import org.daylight.sonariaworld.SonariaWorld;
import org.daylight.sonariaworld.client.data.ClientState;
import org.daylight.sonariaworld.network.payload.MorphRequestPayload;
import org.lwjgl.glfw.GLFW;

public class KeyHandler {
    public static final KeyMapping TOGGLE_MORPH =
            new KeyMapping(
                    "key.sonariaworld.toggle_morph",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_G,
                    KeyMapping.Category.GAMEPLAY
            );

    public static final KeyMapping TOGGLE_MOVEMENT_MODE =
            new KeyMapping(
                    "key.sonariaworld.toggle_movement_mode",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_LEFT_ALT,
                    KeyMapping.Category.GAMEPLAY
            );

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (TOGGLE_MORPH.consumeClick()) {
            System.out.println("Sending toggle morph");
            if(Minecraft.getInstance().player != null) {
                Services.CLIENT_NETWORK.sendMorphRequest(
                        new MorphRequestPayload(
                                Identifier.fromNamespaceAndPath(SonariaWorld.MOD_ID, "olatua"),
                                1
                        )
                );
            }

        }
        while (TOGGLE_MOVEMENT_MODE.consumeClick()) {
            ClientState.setMovementMode(ClientState.getMovementMode() == ClientState.MovementMode.VANILLA ?
                    ClientState.MovementMode.PHYSICAL :
                    ClientState.MovementMode.VANILLA);
            if(Minecraft.getInstance().player != null) Minecraft.getInstance().player.displayClientMessage(
                    Component.literal("Movement mode: " + ClientState.getMovementMode().name()), false);
        }
    }

    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_MORPH);
        event.register(TOGGLE_MOVEMENT_MODE);
    }
}
