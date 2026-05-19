package org.daylight.sonariaworld.neoforge.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.daylight.sonariaworld.SonariaWorld;
import org.jspecify.annotations.NonNull;

public record ToggleMorphPayload(boolean enabled) implements CustomPacketPayload {
    public static final Type<ToggleMorphPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(
                    SonariaWorld.MOD_ID,
                    "toggle_morph"
            ));

    public static final StreamCodec<FriendlyByteBuf, ToggleMorphPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> buf.writeBoolean(payload.enabled),
                    buf -> new ToggleMorphPayload(buf.readBoolean())
            );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
