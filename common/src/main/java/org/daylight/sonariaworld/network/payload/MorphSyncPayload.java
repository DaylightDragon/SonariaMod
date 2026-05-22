package org.daylight.sonariaworld.network.payload;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;import org.daylight.sonariaworld.network.NetworkIds;

import java.util.UUID;

public record MorphSyncPayload(
        String playerId,
        Identifier entityId,
        int variant,
        boolean morphed
) implements CustomPacketPayload {

    public static final Type<MorphSyncPayload> TYPE =
            new Type<>(NetworkIds.MORPH_SYNC);

    public static final StreamCodec<RegistryFriendlyByteBuf, MorphSyncPayload> STREAM_CODEC =
            StreamCodec.of(
                    MorphSyncPayload::write,
                    MorphSyncPayload::read
            );

    private static MorphSyncPayload read(RegistryFriendlyByteBuf buf) {
        return new MorphSyncPayload(
                buf.readUtf(),
                buf.readIdentifier(),
                buf.readInt(),
                buf.readBoolean()
        );
    }

    private static void write(
            FriendlyByteBuf buf,
            MorphSyncPayload payload
    ) {
        buf.writeUtf(payload.playerId);
        buf.writeIdentifier(payload.entityId);
        buf.writeInt(payload.variant);
        buf.writeBoolean(payload.morphed);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
