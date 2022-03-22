package net.hyper_pigeon.camera.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.hyper_pigeon.camera.Camera;
import net.hyper_pigeon.camera.block.entity.ImageFrameBlockEntity;
import net.hyper_pigeon.camera.client.render.ImageFrameBlockEntityRenderer;

public class CameraClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
//        EntityRendererRegistry.INSTANCE.register(Camera.IMAGE_FRAME_ENTITY, (dispatcher, context) -> {
//            return new ImageFrameEntityRenderer(dispatcher);
//        });
        BlockEntityRendererRegistry.INSTANCE.register(Camera.IMAGE_FRAME_BLOCK_ENTITY, ImageFrameBlockEntityRenderer::new);
    }
}
