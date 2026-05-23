package org.daylight.sonariaworld.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.daylight.sonariaworld.client.model.entity.OlatuaModel;
import org.daylight.sonariaworld.entity.species.OlatuaEntity;
import org.jspecify.annotations.NonNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class OlatuaEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<OlatuaEntity, @NonNull R> {
    public OlatuaEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new OlatuaModel());

        // Add the glow layer
//        withRenderLayer(AutoGlowingGeoLayer::new);
    }

//    @Override
//    public void preRenderPass(RenderPassInfo<@NonNull R> renderPassInfo, SubmitNodeCollector renderTasks) {
//        super.preRenderPass(renderPassInfo, renderTasks);
//    }
}
