package org.daylight.sonariaworld.neoforge.client.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.daylight.sonariaworld.neoforge.network.ModNet;
import org.lwjgl.glfw.GLFW;

public class KeyHandler {
    public static final KeyMapping TOGGLE_MORPH =
            new KeyMapping(
                    "key.sonariaworld.toggle_morph",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_G,
                    KeyMapping.Category.GAMEPLAY
            );

    private static boolean morphed = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (TOGGLE_MORPH.consumeClick()) {
            morphed = !morphed;

            System.out.println("Sending toggle morph");
            ModNet.sendToggleMorph(morphed);
        }
    }
}
