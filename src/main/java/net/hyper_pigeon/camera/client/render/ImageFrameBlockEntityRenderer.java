package net.hyper_pigeon.camera.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hyper_pigeon.camera.block.ImageFrameBlock;
import net.hyper_pigeon.camera.block.entity.ImageFrameBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class ImageFrameBlockEntityRenderer extends BlockEntityRenderer<ImageFrameBlockEntity> {
    private final ImageFrameBlockEntityRenderer.ImageFrameModel model = new ImageFrameBlockEntityRenderer.ImageFrameModel();
    //private static final Identifier DEFAULT_IMAGE = new Identifier("camera", "textures/block/default_image.png");
    private static final Identifier EMPTY_IMAGE = new Identifier("camera", "textures/block/empty_image.png");
    private static final Identifier FRAME_SIDE = new Identifier("camera", "textures/block/frame_side.png");
    private static final Identifier FRAME_BACK = new Identifier("camera", "textures/block/frame_back.png");

    //private static final float THICKNESS = 1F / 16F;

   // private final MinecraftClient client = MinecraftClient.getInstance();


    public ImageFrameBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(ImageFrameBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState blockState = entity.getCachedState();

        //rotate matrixStack to face correct direction
        matrixStack.push();
        matrixStack.translate(0.5D, 0.5D, 0.5D);
        float directionRotation = -((Direction)blockState.get(ImageFrameBlock.FACING)).asRotation();
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(directionRotation));
        matrixStack.translate(0.0D, -0.3125D, -0.4375D);


        matrixStack.scale(0.6666667F, -0.6666667F, -0.6666667F);

        if(entity.imageIdentifier != null){
            SpriteIdentifier imageTexture =
                    new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,Identifier.tryParse(entity.imageIdentifier));
            ImageFrameBlockEntityRenderer.ImageFrameModel imageFrameModel = this.model;
            this.model.getClass();
            VertexConsumer vertexConsumer = imageTexture.getVertexConsumer(vertexConsumers, imageFrameModel::getLayer);
            this.model.field.render(matrixStack,vertexConsumer,light,overlay);

        }
        else {
            SpriteIdentifier emptyTexture = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,EMPTY_IMAGE);
            SpriteIdentifier frameSideTexture = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, FRAME_SIDE);
            SpriteIdentifier frameBackTexture = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, FRAME_BACK);

            ImageFrameBlockEntityRenderer.ImageFrameModel imageFrameModel = this.model;
            this.model.getClass();
            VertexConsumer vertexConsumer = emptyTexture.getVertexConsumer(vertexConsumers, imageFrameModel::getLayer);
            this.model.field.render(matrixStack, vertexConsumer, light, overlay);
        }
        matrixStack.pop();

    }


    @Environment(EnvType.CLIENT)
    public static final class ImageFrameModel extends Model {
        public final ModelPart field = new ModelPart(64, 32, 0, 0);

        public ImageFrameModel() {
            super(RenderLayer::getEntityCutoutNoCull);
            this.field.addCuboid(-12.0F, -14.0F, -1.0F, 24.0F, 12.0F, 2.0F, 0.0F);
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
            this.field.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        }
    }
}
