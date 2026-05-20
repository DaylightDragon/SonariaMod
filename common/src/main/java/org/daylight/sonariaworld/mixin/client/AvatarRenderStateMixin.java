package org.daylight.sonariaworld.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.daylight.sonariaworld.mixinrelated.MorphRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public class AvatarRenderStateMixin implements MorphRenderState {
    @Unique
    private LivingEntity sonaria$realPlayerEntity;
    @Unique
    private LivingEntity sonaria$morphEntity;
    @Unique
    private EntityRenderState sonaria$cachedState = null;
    @Unique
    private EntityRenderer sonaria$renderer = null;

    @Override
    public void sonaria$setMorphEntity(
            LivingEntity entity
    ) {
        this.sonaria$morphEntity = entity;
    }

    @Override
    public LivingEntity sonaria$getMorphEntity() {
        return sonaria$morphEntity;
    }

    @Override
    public void sonaria$setRealPlayerEntity(LivingEntity entity) {
        this.sonaria$realPlayerEntity = entity;
    }

    @Override
    public LivingEntity sonaria$getRealPlayerEntity() {
        return sonaria$realPlayerEntity;
    }

    @Override
    public EntityRenderState sonaria$getOrCreateCachedState(LivingEntity entity) {
        if(this.sonaria$cachedState == null) {
            this.sonaria$renderer =
                    Minecraft.getInstance().getEntityRenderDispatcher()
                            .getRenderer(entity);
            this.sonaria$cachedState = this.sonaria$renderer.createRenderState(entity, 1.0F);
        }

        return this.sonaria$cachedState;
    }

    @Override
    public EntityRenderer<?, ?> sonaria$getRenderer(LivingEntity entity) {
        if(this.sonaria$renderer == null) {
            this.sonaria$renderer =
                    Minecraft.getInstance().getEntityRenderDispatcher()
                            .getRenderer(entity);
        }
        return this.sonaria$renderer;
    }
}
