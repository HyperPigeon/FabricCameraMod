package net.hyper_pigeon.camera.client.render;

import net.hyper_pigeon.camera.entity.ImageEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.util.Identifier;

public class ImageEntityRenderer extends EntityRenderer<ImageEntity> {
    protected ImageEntityRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public Identifier getTexture(ImageEntity entity) {
        return null;
    }
}
