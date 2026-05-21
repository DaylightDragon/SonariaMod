package org.daylight.sonariaworld.mixin.client;

import net.minecraft.world.entity.WalkAnimationState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WalkAnimationState.class)
public interface WalkAnimationStateAccessor {
    @Accessor("speed")
    void setSpeed(float speed);

    @Accessor("speed")
    float getSpeed();

    @Accessor("speedOld")
    float getSpeedOld();

    @Accessor("speedOld")
    void setSpeedOld(float speedOld);
}
