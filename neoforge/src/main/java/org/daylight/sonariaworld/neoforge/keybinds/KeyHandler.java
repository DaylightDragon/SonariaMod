package org.daylight.sonariaworld.neoforge.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.daylight.sonariaworld.SonariaWorld;
import org.daylight.sonariaworld.network.client.ClientMorphApi;
import org.daylight.sonariaworld.registry.EntityRegistry;
import org.lwjgl.glfw.GLFW;

public class KeyHandler {
    public static final KeyMapping TOGGLE_MORPH =
            new KeyMapping(
                    "key.sonariaworld.toggle_morph",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_G,
                    KeyMapping.Category.GAMEPLAY
            );

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (TOGGLE_MORPH.consumeClick()) {
            System.out.println("Sending toggle morph");
            ClientMorphApi.requestMorph(Identifier.fromNamespaceAndPath(SonariaWorld.MOD_ID, "olatua"), 1);
        }
    }

    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_MORPH);
    }
}
