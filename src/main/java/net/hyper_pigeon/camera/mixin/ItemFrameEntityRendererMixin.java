package net.hyper_pigeon.camera.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hyper_pigeon.camera.Camera;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Environment(EnvType.CLIENT)
@Mixin(ItemFrameEntityRenderer.class)
public abstract class ItemFrameEntityRendererMixin extends EntityRenderer<ItemFrameEntity> {

    @Shadow
    @Mutable
    @Final
    private static ModelIdentifier NORMAL_FRAME;

    @Shadow
    @Mutable
    @Final
    private static  ModelIdentifier MAP_FRAME;

    @Shadow
    @Mutable
    @Final
    private  MinecraftClient client = MinecraftClient.getInstance();

    @Shadow
    @Mutable
    @Final
    private ItemRenderer itemRenderer;

    @Shadow
    public abstract Vec3d getPositionOffset(ItemFrameEntity itemFrameEntity, float f);

    protected ItemFrameEntityRendererMixin(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }


    @Override
    public void render(ItemFrameEntity itemFrameEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(itemFrameEntity, f, g, matrixStack, vertexConsumerProvider, i);
        matrixStack.push();
        Direction direction = itemFrameEntity.getHorizontalFacing();
        Vec3d vec3d = this.getPositionOffset(itemFrameEntity, g);
        matrixStack.translate(-vec3d.getX(), -vec3d.getY(), -vec3d.getZ());
        double d = 0.46875D;
        matrixStack.translate((double)direction.getOffsetX() * 0.46875D, (double)direction.getOffsetY() * 0.46875D, (double)direction.getOffsetZ() * 0.46875D);
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(itemFrameEntity.pitch));
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F - itemFrameEntity.yaw));
        boolean bl = itemFrameEntity.isInvisible();
        if (!bl) {
            BlockRenderManager blockRenderManager = this.client.getBlockRenderManager();
            BakedModelManager bakedModelManager = blockRenderManager.getModels().getModelManager();
            ModelIdentifier modelIdentifier = itemFrameEntity.getHeldItemStack().getItem() == Items.FILLED_MAP ? MAP_FRAME : NORMAL_FRAME;
            matrixStack.push();
            matrixStack.translate(-0.5D, -0.5D, -0.5D);
            blockRenderManager.getModelRenderer().render(matrixStack.peek(), vertexConsumerProvider.getBuffer(TexturedRenderLayers.getEntitySolid()), (BlockState)null, bakedModelManager.getModel(modelIdentifier), 1.0F, 1.0F, 1.0F, i, OverlayTexture.DEFAULT_UV);
            matrixStack.pop();
        }

        ItemStack itemStack = itemFrameEntity.getHeldItemStack();
        if (!itemStack.isEmpty()) {
            boolean bl2 = itemStack.getItem() == Items.FILLED_MAP;
            if (bl) {
                matrixStack.translate(0.0D, 0.0D, 0.5D);
            } else {
                matrixStack.translate(0.0D, 0.0D, 0.4375D);
            }

            int j = bl2 ? itemFrameEntity.getRotation() % 4 * 2 : itemFrameEntity.getRotation();
            matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion((float)j * 360.0F / 8.0F));
            if (bl2) {
                matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
                float h = 0.0078125F;
                matrixStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
                matrixStack.translate(-64.0D, -64.0D, 0.0D);
                MapState mapState = FilledMapItem.getOrCreateMapState(itemStack, itemFrameEntity.world);
                matrixStack.translate(0.0D, 0.0D, -1.0D);
                if (mapState != null) {
                    this.client.gameRenderer.getMapRenderer().draw(matrixStack, vertexConsumerProvider, mapState, true, i);
                }
            }
            else if(itemStack.getItem() == Camera.IMAGE_ITEM){
                NativeImage nativeImage = null;
                try {
                    nativeImage = NativeImage.read((new ByteArrayInputStream(itemStack.getTag().getByteArray("imageBytes"))));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Identifier identifier = Identifier.tryParse(itemStack.getTag().getString("imageIdentifier"));
                this.client.getTextureManager().bindTexture(identifier);

                RenderSystem.pushMatrix();

                ItemStack itemFrameStack = itemFrameEntity.getHeldItemStack();

                float width = itemFrameEntity.getWidth();
                float height = itemFrameEntity.getHeight();
                float imageWidth = (float)  itemFrameStack.getTag().getInt("width");
                float imageHeight = (float) itemFrameStack.getTag().getInt("height");

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();
                buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE);

                float scale = 0.7F;

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
            else {
                matrixStack.scale(0.5F, 0.5F, 0.5F);
                this.itemRenderer.renderItem(itemStack, ModelTransformation.Mode.FIXED, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider);
            }
        }

        matrixStack.pop();
    }







}
