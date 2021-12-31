package net.hyper_pigeon.camera.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.SpriteAtlasManager;
import net.minecraft.client.render.model.json.ModelTransformation.Mode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ImageItemRenderer {

    public static void render(ItemStack stack, Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        if(stack.getTag() != null) {
            matrices.push();
            Identifier identifier = Identifier.tryParse(stack.getTag().getString("imageIdentifier"));
            MinecraftClient.getInstance().getTextureManager().bindTexture(identifier);
            RenderSystem.bindTexture(MinecraftClient.getInstance().getTextureManager().getTexture(identifier).getGlId());
            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
            BakedModel imageModel = itemRenderer.getHeldItemModel(stack, null,null);
            matrices.pop();
        }

    }

}
