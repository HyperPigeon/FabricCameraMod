package net.hyper_pigeon.camera.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.hyper_pigeon.camera.Camera;
import net.hyper_pigeon.camera.client.render.ImageItemRenderer;
import net.hyper_pigeon.camera.networking.CameraNetworkingConstants;
import net.minecraft.client.MinecraftClient;

public class CameraClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //BuiltinItemRendererRegistry.DynamicItemRenderer imageRenderer = ImageItemRenderer::render;
        //BuiltinItemRendererRegistry.INSTANCE.register(Camera.IMAGE_ITEM, imageRenderer);
    }
}
