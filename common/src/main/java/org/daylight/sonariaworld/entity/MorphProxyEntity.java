package org.daylight.sonariaworld.entity;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

@Deprecated
public class MorphProxyEntity extends LivingEntity {
    public MorphProxyEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
        this.noPhysics = false;
    }

    @Override
    public void tick() {
        // no AI
    }

    @Override
    public void aiStep() {
        // no-op
    }

    @Override
    public @NonNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public boolean isInvisible() {
        return true;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double dist) {
        return false;
    }

    @Override
    public void push(@NonNull Entity entity) {
        // no push
    }
}
