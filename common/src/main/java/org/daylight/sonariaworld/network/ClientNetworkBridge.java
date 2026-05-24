package org.daylight.sonariaworld.network;

import org.daylight.sonariaworld.network.payload.MorphRequestPayload;
import org.daylight.sonariaworld.network.payload.MorphRotationRequestPayload;
import org.daylight.sonariaworld.network.payload.PlayerAttackPayload;

public interface ClientNetworkBridge {
    void sendMorphRequest(MorphRequestPayload payload);
    void sendMorphRotation(MorphRotationRequestPayload morphRotationRequestPayload);
    void sendAttackRequest(PlayerAttackPayload payload);
}
