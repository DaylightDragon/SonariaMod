package org.daylight.sonariaworld.network.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.daylight.sonariaworld.network.NetworkIds;
import org.jspecify.annotations.NonNull;

public record MorphRotationRequestPayload(
        String playerId,
        float yaw,
        float pitch,
        float roll,
        float headYaw,
        float headPitch
) implements CustomPacketPayload {
    public static final Type<MorphRotationRequestPayload> TYPE =
            new Type<>(NetworkIds.MORPH_ROTATION_UPDATE_REQUEST);

    public static final StreamCodec<RegistryFriendlyByteBuf, MorphRotationRequestPayload> STREAM_CODEC =
            StreamCodec.of(
                    MorphRotationRequestPayload::write,
                    MorphRotationRequestPayload::read
            );

    private static void write(
            RegistryFriendlyByteBuf buf,
            MorphRotationRequestPayload payload
    ) {
        buf.writeUtf(payload.playerId());
        buf.writeFloat(payload.yaw());
        buf.writeFloat(payload.pitch());
        buf.writeFloat(payload.roll());
        buf.writeFloat(payload.headYaw());
        buf.writeFloat(payload.headPitch());
    }

    private static MorphRotationRequestPayload read(RegistryFriendlyByteBuf buf) {
        return new MorphRotationRequestPayload(
                buf.readUtf(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat()
        );
    }

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
