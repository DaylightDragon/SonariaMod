package org.daylight.sonariaworld.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.daylight.sonariaworld.client.model.entity.OlatuaModel;
import org.daylight.sonariaworld.entity.OlatuaEntity;
import org.jspecify.annotations.NonNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class OlatuaEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<OlatuaEntity, @NonNull R> {
    public OlatuaEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new OlatuaModel());

        // Add the glow layer to the bat so that it can live out its dreams of being rudolph
//        withRenderLayer(AutoGlowingGeoLayer::new);
    }
}
