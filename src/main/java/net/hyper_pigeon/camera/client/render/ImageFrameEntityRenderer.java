package net.hyper_pigeon.camera.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hyper_pigeon.camera.Camera;
import net.hyper_pigeon.camera.entity.ImageFrameEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;


@Environment(EnvType.CLIENT)
public class ImageFrameEntityRenderer extends EntityRenderer<ImageFrameEntity> {

    private static final Identifier DEFAULT_IMAGE = new Identifier("camera", "textures/item/default_image.png");
    private static final Identifier EMPTY_IMAGE = new Identifier("camera", "textures/item/empty_image.png");
    private static final Identifier FRAME_SIDE = new Identifier("camera", "textures/images/frame_side.png");
    private static final Identifier FRAME_BACK = new Identifier("camera", "textures/item/frame_back.png");

    private static final float THICKNESS = 1F / 16F;
    private final MinecraftClient client = MinecraftClient.getInstance();

    public ImageFrameEntityRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }




    public void render(ImageFrameEntity entity,float f, float g,  MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        UUID imageID = entity.getHeldItemStack().getTag().getUuid("id");
        renderImage(entity,imageID, entity.getHorizontalFacing(), 2, 2, matrixStack, vertexConsumerProvider, i);
        super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public void renderImage(ImageFrameEntity imageFrameEntity, UUID imageUUID, Direction facing, float width, float height, MatrixStack matrixStack, VertexConsumerProvider buffer1, int light) {
        matrixStack.push();

        float imageRatio = 1F;
        boolean stretch = true;
        Identifier identifier = EMPTY_IMAGE;



        matrixStack.translate(-0.5D, 0D, -0.5D);

        if(imageFrameEntity.getHeldItemStack().equals(Camera.IMAGE_ITEM)){

            identifier = Identifier.tryParse(imageFrameEntity.getHeldItemStack().getTag().getString("imageIdentifier"));

            Direction direction = imageFrameEntity.getHorizontalFacing();
            Vec3d vec3d = this.getPositionOffset(imageFrameEntity, width);
            matrixStack.translate(-vec3d.getX(), -vec3d.getY(), -vec3d.getZ());
            matrixStack.translate((double)direction.getOffsetX() * 0.46875D, (double)direction.getOffsetY() * 0.46875D, (double)direction.getOffsetZ() * 0.46875D);
            matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(imageFrameEntity.pitch));
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F - imageFrameEntity.yaw));


            float frameRatio = width / height;

            float ratio = imageRatio / frameRatio;

            float ratioX;
            float ratioY;

            if (stretch) {
                ratioX = 0F;
                ratioY = 0F;
            } else {
                if (ratio >= 1F) {
                    ratioY = (1F - 1F / ratio) / 2F;
                    ratioX = 0F;
                } else {
                    ratioX = (1F - ratio) / 2F;
                    ratioY = 0F;
                }

                ratioX *= width;
                ratioY *= height;
            }

            VertexConsumer builderFront = buffer1.getBuffer(RenderLayer.getText(identifier));

            // Front
            vertex(builderFront, matrixStack, 0F + ratioX, ratioY, THICKNESS, 0F, 1F, light);
            vertex(builderFront, matrixStack, width - ratioX, ratioY, THICKNESS, 1F, 1F, light);
            vertex(builderFront, matrixStack, width - ratioX, height - ratioY, THICKNESS, 1F, 0F, light);
            vertex(builderFront, matrixStack, ratioX, height - ratioY, THICKNESS, 0F, 0F, light);

            VertexConsumer builderSide = buffer1.getBuffer(RenderLayer.getText(FRAME_SIDE));

            //Left
            vertex(builderSide, matrixStack, 0F + ratioX, 0F + ratioY, 0F, 1F, 0F + ratioY, light);
            vertex(builderSide, matrixStack, 0F + ratioX, 0F + ratioY, THICKNESS, 1F - THICKNESS, 0F + ratioY, light);
            vertex(builderSide, matrixStack, 0F + ratioX, height - ratioY, THICKNESS, 1F - THICKNESS, 1F - ratioY, light);
            vertex(builderSide, matrixStack, 0F + ratioX, height - ratioY, 0F, 1F, 1F - ratioY, light);

            //Right
            vertex(builderSide, matrixStack, width - ratioX, 0F + ratioY, 0F, 0F, 0F + ratioY, light);
            vertex(builderSide, matrixStack, width - ratioX, height - ratioY, 0F, 0F, 1F - ratioY, light);
            vertex(builderSide, matrixStack, width - ratioX, height - ratioY, THICKNESS, THICKNESS, 1F - ratioY, light);
            vertex(builderSide, matrixStack, width - ratioX, 0F + ratioY, THICKNESS, THICKNESS, 0F + ratioY, light);

            //Top
            vertex(builderSide, matrixStack, 0F + ratioX, height - ratioY, 0F, 0F + ratioX, 1F, light);
            vertex(builderSide, matrixStack, 0F + ratioX, height - ratioY, THICKNESS, 0F + ratioX, 1F - THICKNESS, light);
            vertex(builderSide, matrixStack, width - ratioX, height - ratioY, THICKNESS, 1F - ratioX, 1F - THICKNESS, light);
            vertex(builderSide, matrixStack, width - ratioX, height - ratioY, 0F, 1F - ratioX, 1F, light);

            //Bottom
            vertex(builderSide, matrixStack, 0F + ratioX, 0F + ratioY, 0F, 0F + ratioX, 0F, light);
            vertex(builderSide, matrixStack, width - ratioX, 0F + ratioY, 0F, 1F - ratioX, 0F, light);
            vertex(builderSide, matrixStack, width - ratioX, 0F + ratioY, THICKNESS, 1F - ratioX, THICKNESS, light);
            vertex(builderSide, matrixStack, 0F + ratioX, 0F + ratioY, THICKNESS, 0F + ratioX, THICKNESS, light);

            VertexConsumer builderBack = buffer1.getBuffer(RenderLayer.getText(FRAME_BACK));

            //Back
            vertex(builderBack, matrixStack, width - ratioX, 0F + ratioY, 0F, 1F - ratioX, 0F + ratioY, light);
            vertex(builderBack, matrixStack, 0F + ratioX, 0F + ratioY, 0F, 0F + ratioX, 0F + ratioY, light);
            vertex(builderBack, matrixStack, 0F + ratioX, height - ratioY, 0F, 0F + ratioX, 1F - ratioY, light);
            vertex(builderBack, matrixStack, width - ratioX, height - ratioY, 0F, 1F - ratioX, 1F - ratioY, light);
        }



        matrixStack.pop();
    }

    private static void vertex(VertexConsumer builder, MatrixStack matrixStack, float x, float y, float z, float u, float v, int light) {
        Matrix4f matrix4f = matrixStack.peek().getModel();
        Matrix3f matrix3f = matrixStack.peek().getNormal();
        builder.vertex(matrix4f, x, y, z)
                .color(255, 255, 255, 255)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(matrix3f, 0F, 0F, -1F)
                .next();
    }

//    private void drawImage(ImageFrameEntity imageFrameEntity, float f, float g,
//                           MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i,float width,float height) {
//
//        matrixStack.push();
//        //first, I rotate the matrix stack depending on what direction the ImageFrameEntity is facing
//        Direction direction = imageFrameEntity.getHorizontalFacing();
//        Vec3d vec3d = this.getPositionOffset(imageFrameEntity, g);
//        matrixStack.translate(-vec3d.getX(), -vec3d.getY(), -vec3d.getZ());
//        matrixStack.translate((double)direction.getOffsetX() * 0.46875D, (double)direction.getOffsetY() * 0.46875D, (double)direction.getOffsetZ() * 0.46875D);
//        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(imageFrameEntity.pitch));
//        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F - imageFrameEntity.yaw));
//
//        if(imageFrameEntity.getHeldItemStack().equals(Camera.IMAGE_ITEM)) {
//            //we need to get information about the image, such as the width and height
//            ItemStack itemStack = imageFrameEntity.getHeldItemStack();
//            float imageWidth = itemStack.getTag().getInt("width");
//            float imageHeight = itemStack.getTag().getInt("height");
//
//            //get the image id as well
//            Identifier identifier = Identifier.tryParse(itemStack.getTag().getString("imageIdentifier"));
//            RenderLayer renderLayer = RenderLayer.getText(identifier);
//
//
//            float frameRatio = width / height;
//            float imageRatio = imageWidth/imageHeight;
//
//            float ratio = imageRatio / frameRatio;
//
//            float ratioX;
//            float ratioY;
//
//            if (ratio >= 1F) {
//                ratioY = (1F - 1F / ratio) / 2F;
//                ratioX = 0F;
//            } else {
//                ratioX = (1F - ratio) / 2F;
//                ratioY = 0F;
//            }
//
//            ratioX *= width;
//            ratioY *= height;
//
//            //bind the texture
//            this.client.getTextureManager().bindTexture(identifier);
//
//            //draw the image
//            Matrix4f matrix4f = matrixStack.peek().getModel();
//            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
//
//            vertexConsumer.vertex(matrix4f, 0F + ratioX, ratioY, THICKNESS).color(255, 255, 255, 255).texture(0.0F, 1.0F).light(i).next();
//
//
//        }
//
//        matrixStack.pop();
//
//    }

//    public void drawImage(ImageFrameEntity imageFrameEntity, float f, float g,
//                          MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider,
//                          int i,float width,float height) {
//
//        //System.out.println("check");
//
//        matrixStack.push();
//
//        ItemStack itemStack = imageFrameEntity.getHeldItemStack();
//        float imageWidth = itemStack.getTag().getInt("width");
//        float imageHeight = itemStack.getTag().getInt("height");
//
//        //get the image id as well
//        Identifier identifier = Identifier.tryParse(itemStack.getTag().getString("imageIdentifier"));
//        RenderLayer renderLayer = RenderLayer.getText(identifier);
//
//        float scale = 1.0F;
//
//        float ws = (float) width * scale;
//        float hs = (float) height * scale;
//
//        float rs = ws / hs;
//        float ri = imageWidth / imageHeight;
//
//        float hnew;
//        float wnew;
//
//        if (rs > ri) {
//            wnew = imageWidth * hs / imageHeight;
//            hnew = hs;
//        } else {
//            wnew = ws;
//            hnew = imageHeight * ws / imageWidth;
//        }
//
//        float top = (hs - hnew) / 2F;
//        float left = (ws - wnew) / 2F;
//
//        left += ((1F - scale) * ws) / 2F;
//        top += ((1F - scale) * hs) / 2F;
//
//        //first, I rotate the matrix stack depending on what direction the ImageFrameEntity is facing
//        Direction direction = imageFrameEntity.getHorizontalFacing();
//        Vec3d vec3d = this.getPositionOffset(imageFrameEntity, g);
//        matrixStack.translate(-vec3d.getX(), -vec3d.getY(), -vec3d.getZ());
//        matrixStack.translate((double)direction.getOffsetX() * 0.46875D, (double)direction.getOffsetY() * 0.46875D, (double)direction.getOffsetZ() * 0.46875D);
//        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(imageFrameEntity.pitch));
//        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F - imageFrameEntity.yaw));
//
//        //draw the image
//        Matrix4f matrix4f = matrixStack.peek().getModel();
//        VertexConsumer buffer = vertexConsumerProvider.getBuffer(renderLayer);
//
//        buffer.vertex(left, top, THICKNESS).texture(0F, 0F).next();
//        buffer.vertex(left, top + hnew, THICKNESS).texture(0F, 1F).next();
//        buffer.vertex(left + wnew, top + hnew, THICKNESS).texture(1F, 1F).next();
//        buffer.vertex(left + wnew, top, THICKNESS).texture(1F, 0F).next();
//
//        matrixStack.pop();
//    }




    @Override
    public Identifier getTexture(ImageFrameEntity entity) {

        if(entity.getHeldItemStack().equals(Camera.IMAGE_ITEM)){
            ItemStack itemStack = entity.getHeldItemStack();
            Identifier imageIdentifier= Identifier.tryParse(itemStack.getTag().getString("imageIdentifier"));
            return imageIdentifier;
        }

        return EMPTY_IMAGE;
    }

    protected boolean hasLabel(ImageFrameEntity imageFrameEntity) {
        if (MinecraftClient.isHudEnabled() && !imageFrameEntity.getHeldItemStack().isEmpty() && imageFrameEntity.getHeldItemStack().hasCustomName() && this.dispatcher.targetedEntity == imageFrameEntity) {
            double d = this.dispatcher.getSquaredDistanceToCamera(imageFrameEntity);
            float f = imageFrameEntity.isSneaky() ? 32.0F : 64.0F;
            return d < (double)(f * f);
        } else {
            return false;
        }
    }

    protected void renderLabelIfPresent(ImageFrameEntity imageFrameEntity, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.renderLabelIfPresent(imageFrameEntity, imageFrameEntity.getHeldItemStack().getName(), matrixStack, vertexConsumerProvider, i);
    }


}
