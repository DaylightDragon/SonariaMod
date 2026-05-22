package org.daylight.sonariaworld.neoforge.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.daylight.sonariaworld.neoforge.network.handler.client.ClientMorphHandler;
import org.daylight.sonariaworld.neoforge.network.handler.client.ClientMorphRotationHandler;
import org.daylight.sonariaworld.neoforge.network.handler.server.ServerMorphHandler;
import org.daylight.sonariaworld.neoforge.network.handler.server.ServerMorphRotationHandler;
import org.daylight.sonariaworld.network.payload.*;

public final class NeoForgeNetwork {
    private NeoForgeNetwork() {

    }

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                MorphRequestPayload.TYPE,
                MorphRequestPayload.STREAM_CODEC,
                ServerMorphHandler::handle
        );

        registrar.playToClient(
                MorphSyncPayload.TYPE,
                MorphSyncPayload.STREAM_CODEC,
                ClientMorphHandler::handle
        );

        registrar.playToServer(
                MorphRotationRequestPayload.TYPE,
                MorphRotationRequestPayload.STREAM_CODEC,
                ServerMorphRotationHandler::handle
        );

        registrar.playToClient(
                MorphRotationDistributionPayload.TYPE,
                MorphRotationDistributionPayload.STREAM_CODEC,
                ClientMorphRotationHandler::handle
        );
    }
}
