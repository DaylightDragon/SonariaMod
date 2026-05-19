package org.daylight.sonariaworld.client;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.daylight.sonariaworld.client.renderer.entity.OlatuaEntityRenderer;
import org.daylight.sonariaworld.registry.EntityRegistry;

import java.util.function.BiConsumer;

public class SonariaWorldClient {
    public static void registerRenderers(BiConsumer<EntityType<? extends Entity>, EntityRendererProvider> entityRenderers,
                                         BiConsumer<BlockEntityType<? extends BlockEntity>, BlockEntityRendererProvider> blockEntityRenderers
    ) {
        entityRenderers.accept(EntityRegistry.OLATUA.get(), OlatuaEntityRenderer::new);

//        blockEntityRenderers.accept(BlockEntityRegistry.SOMETHING.get(), context -> new SomethingBlockRenderer<>());
    }
}
