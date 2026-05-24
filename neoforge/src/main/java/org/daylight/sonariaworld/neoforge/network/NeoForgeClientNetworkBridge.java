package org.daylight.sonariaworld.neoforge.network;

import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.daylight.sonariaworld.network.ClientNetworkBridge;
import org.daylight.sonariaworld.network.payload.MorphRequestPayload;
import org.daylight.sonariaworld.network.payload.MorphRotationRequestPayload;
import org.daylight.sonariaworld.network.payload.PlayerAttackPayload;

public class NeoForgeClientNetworkBridge implements ClientNetworkBridge {
    @Override
    public void sendMorphRequest(MorphRequestPayload payload) {
        ClientPacketDistributor.sendToServer(payload);
    }

    @Override
    public void sendMorphRotation(MorphRotationRequestPayload payload) {
        ClientPacketDistributor.sendToServer(payload);
    }

    @Override
    public void sendAttackRequest(PlayerAttackPayload payload) {
        ClientPacketDistributor.sendToServer(payload);
    }
}
