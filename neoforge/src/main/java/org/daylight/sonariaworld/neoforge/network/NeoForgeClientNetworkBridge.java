package org.daylight.sonariaworld.neoforge.network;

import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.daylight.sonariaworld.network.ClientNetworkBridge;
import org.daylight.sonariaworld.network.payload.MorphRequestPayload;

public class NeoForgeClientNetworkBridge implements ClientNetworkBridge {
    @Override
    public void sendMorphRequest(MorphRequestPayload payload) {
        ClientPacketDistributor.sendToServer(payload);
    }
}
