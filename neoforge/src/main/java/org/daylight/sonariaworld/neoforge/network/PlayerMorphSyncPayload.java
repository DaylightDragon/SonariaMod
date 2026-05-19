package org.daylight.sonariaworld.neoforge.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.daylight.sonariaworld.SonariaWorld;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public record PlayerMorphSyncPayload(UUID playerId, boolean morphed) implements CustomPacketPayload {
    public static final Type<PlayerMorphSyncPayload> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(SonariaWorld.MOD_ID, "morph_sync")
    );

    public static final StreamCodec<FriendlyByteBuf, PlayerMorphSyncPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
                buf.writeUUID(payload.playerId);
                buf.writeBoolean(payload.morphed);
            },
            buf -> new PlayerMorphSyncPayload(buf.readUUID(), buf.readBoolean())
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
