package org.daylight.sonariaworld.network.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.daylight.sonariaworld.network.NetworkIds;

public record MorphRequestPayload(
        Identifier entityId,
        int variant
) implements CustomPacketPayload {
    public static final Type<MorphRequestPayload> TYPE =
            new Type<>(NetworkIds.MORPH_REQUEST);

    public static final StreamCodec<RegistryFriendlyByteBuf, MorphRequestPayload> STREAM_CODEC =
            StreamCodec.of(
                    MorphRequestPayload::write,
                    MorphRequestPayload::read
            );

    private static void write(
            RegistryFriendlyByteBuf buf,
            MorphRequestPayload payload
    ) {
        buf.writeIdentifier(payload.entityId());
        buf.writeInt(payload.variant());
    }

    private static MorphRequestPayload read(RegistryFriendlyByteBuf buf) {
        return new MorphRequestPayload(
                buf.readIdentifier(),
                buf.readInt()
        );
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
