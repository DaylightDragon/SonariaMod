package org.daylight.sonariaworld.mixinrelated;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.LivingEntity;

public interface MorphRenderState {
    void sonaria$setMorphEntity(LivingEntity entity);
    LivingEntity sonaria$getMorphEntity();
    void sonaria$setRealPlayerEntity(LivingEntity entity);
    LivingEntity sonaria$getRealPlayerEntity();
    EntityRenderState sonaria$getOrCreateCachedState(LivingEntity entity);
    EntityRenderer<?, ?> sonaria$getRenderer(LivingEntity entity);
}
