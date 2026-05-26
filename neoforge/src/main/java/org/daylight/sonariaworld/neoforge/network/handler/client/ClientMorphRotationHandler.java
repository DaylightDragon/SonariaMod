package org.daylight.sonariaworld.neoforge.network.handler.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.daylight.sonariaworld.morph.MorphStateService;
import org.daylight.sonariaworld.morph.MorphState;
import org.daylight.sonariaworld.network.payload.MorphRotationDistributionPayload;
import org.daylight.sonariaworld.util.PlayerLookup;

public final class ClientMorphRotationHandler {
    private ClientMorphRotationHandler() {}

    public static void handle(
            MorphRotationDistributionPayload payload,
            IPayloadContext context
    ) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;

            Player player = PlayerLookup.client(payload.playerId());
            if (player == null) return;

            MorphState state = MorphStateService.get(player);
            MorphState.MorphVisualsInfo morphVisualsInfo = state.getMorphVisualsInfo();

            morphVisualsInfo.setMorphYaw(Mth.wrapDegrees(payload.yaw()));
            morphVisualsInfo.setMorphHeadYaw(Mth.wrapDegrees(payload.headYaw()));
            morphVisualsInfo.setMorphPitch(Mth.wrapDegrees(payload.pitch()));

//            System.out.println("New rotation set: " + morphVisualsInfo);
        });
    }
}
