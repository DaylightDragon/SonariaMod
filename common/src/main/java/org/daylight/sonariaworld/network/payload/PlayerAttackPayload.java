package org.daylight.sonariaworld.network.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.daylight.sonariaworld.network.NetworkIds;
import org.jspecify.annotations.NonNull;

public record PlayerAttackPayload(

) implements CustomPacketPayload {
    public static final Type<PlayerAttackPayload> TYPE =
            new Type<>(NetworkIds.PLAYER_ATTACK);

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerAttackPayload> STREAM_CODEC =
            StreamCodec.of(
                    PlayerAttackPayload::write,
                    PlayerAttackPayload::read
            );

    private static void write(
            RegistryFriendlyByteBuf buf,
            PlayerAttackPayload payload
    ) {

    }

    private static PlayerAttackPayload read(RegistryFriendlyByteBuf buf) {
        return new PlayerAttackPayload(

        );
    }

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
