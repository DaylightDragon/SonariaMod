package org.daylight.sonariaworld.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

@Deprecated
public class SonariaHitboxLayer<S extends LivingEntityRenderState>
        extends RenderLayer<S, EntityModel<S>> {
    public SonariaHitboxLayer(RenderLayerParent<S, EntityModel<S>> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack,
                       SubmitNodeCollector collector,
                       int light,
                       S state,
                       float yRot,
                       float xRot) {

    }
}
