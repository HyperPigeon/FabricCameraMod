package net.hyper_pigeon.camera.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class ImageRenderer{

    private final TextureManager textureManager;
    private RenderLayer renderLayer;

    public ImageRenderer(TextureManager textureManager, Identifier identifier) {
        this.textureManager = textureManager;
        this.renderLayer = RenderLayer.getText(identifier);
    }

//    public void draw(ItemFrameEntity itemFrameEntity, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider,
//                     boolean bl, int i, float width, float height) {
//        matrixStack.push();
//
//        ItemStack imageItemStack = itemFrameEntity.getHeldItemStack();
//        float imageWidth = imageItemStack.getTag().getInt("width");
//        float imageHeight = imageItemStack.getTag().getInt("height");
//
//        Identifier identifier = Identifier.tryParse(imageItemStack.getTag().getString("imageIdentifier"));
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
//        float f = 0.0F;
//        Matrix4f matrix4f = matrixStack.peek().getModel();
//        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
//        vertexConsumer.vertex(matrix4f, left, top, -0.01F).color(255, 255, 255, 255).texture(0.0F, 1.0F).light(i).next();
//        vertexConsumer.vertex(matrix4f, left, top+hnew, -0.01F).color(255, 255, 255, 255).texture(1.0F, 1.0F).light(i).next();
//        vertexConsumer.vertex(matrix4f, left + wnew, top + hnew, -0.01F).color(255, 255, 255, 255).texture(1.0F, 0.0F).light(i).next();
//        vertexConsumer.vertex(matrix4f, left + wnew, top, -0.01F).color(255, 255, 255, 255).texture(0.0F, 0.0F).light(i).next();
//
//        matrixStack.push();
//
//
//
//    }

    public void draw(ItemFrameEntity itemFrameEntity, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider,
                     boolean bl, int i, float width, float height) {
        RenderSystem.pushMatrix();

        ItemStack itemFrameStack = itemFrameEntity.getHeldItemStack();
        float imageWidth = (float)  itemFrameStack.getTag().getInt("width");
        float imageHeight = (float) itemFrameStack.getTag().getInt("height");

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE);

        float scale = 0.9F;

        float ws = (float) width * scale;
        float hs = (float) height * scale;

        float rs = ws / hs;
        float ri = (float) imageWidth / (float) imageHeight;

        float hnew;
        float wnew;

        if (rs > ri) {
            wnew = imageWidth * hs / imageHeight;
            hnew = hs;
        } else {
            wnew = ws;
            hnew = imageHeight * ws / imageWidth;
        }

        float top = (hs - hnew) / 2F;
        float left = (ws - wnew) / 2F;

        left += ((1F - scale) * ws) / 2F;
        top += ((1F - scale) * hs) / 2F;

        buffer.vertex(left, top, -0.01F).texture(0F, 0F).next();
        buffer.vertex(left, top + hnew, -0.01F).texture(0F, 1F).next();
        buffer.vertex(left + wnew, top + hnew, -0.01F).texture(1F, 1F).next();
        buffer.vertex(left + wnew, top, -0.01F).texture(1F, 0F).next();

        tessellator.draw();

        RenderSystem.popMatrix();

    }


}
