package org.daylight.sonariaworld;

import org.daylight.sonariaworld.platform.SonariaWorldPlatform;
import org.daylight.sonariaworld.registry.EntityRegistry;

import java.util.ServiceLoader;

public final class SonariaWorld {
    public static final String MOD_ID = "sonariaworld";

    public static final SonariaWorldPlatform COMMON_PLATFORM = ServiceLoader.load(SonariaWorldPlatform.class).findFirst().orElseThrow();

    public static void init() {
        // Write common init code here.
    }

    public static void doRegistrations() {
        EntityRegistry.init();
        // Uncomment when necessary. Refer to https://github.com/bernie-g/geckolib-examples/
//        SoundRegistry.init();
//        BlockRegistry.init();
//        BlockEntityRegistry.init();
//        ArmorMaterialRegistry.init();
//        ItemRegistry.init();
    }

    public static void initPostRegistering() {
        EntityRegistry.initPost();
    }
}
