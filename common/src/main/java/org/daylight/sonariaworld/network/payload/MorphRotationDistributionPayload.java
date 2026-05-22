package org.daylight.sonariaworld.network.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.daylight.sonariaworld.network.NetworkIds;
import org.jspecify.annotations.NonNull;

public record MorphRotationDistributionPayload(
        String playerId,
        float yaw,
        float pitch,
        float headYaw
) implements CustomPacketPayload {
    public static final Type<MorphRotationDistributionPayload> TYPE =
            new Type<>(NetworkIds.MORPH_ROTATION_UPDATE_DISTRIBUTION);

    public static final StreamCodec<RegistryFriendlyByteBuf, MorphRotationDistributionPayload> STREAM_CODEC =
            StreamCodec.of(
                    MorphRotationDistributionPayload::write,
                    MorphRotationDistributionPayload::read
            );

    private static void write(
            RegistryFriendlyByteBuf buf,
            MorphRotationDistributionPayload payload
    ) {
        buf.writeUtf(payload.playerId());
        buf.writeFloat(payload.yaw());
        buf.writeFloat(payload.headYaw());
        buf.writeFloat(payload.pitch());
    }

    private static MorphRotationDistributionPayload read(RegistryFriendlyByteBuf buf) {
        return new MorphRotationDistributionPayload(
                buf.readUtf(),
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
