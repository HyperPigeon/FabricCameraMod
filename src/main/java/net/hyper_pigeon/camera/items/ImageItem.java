package net.hyper_pigeon.camera.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hyper_pigeon.camera.Camera;
import net.hyper_pigeon.camera.client.render.ImageScreen;
import net.hyper_pigeon.camera.entity.ImageEntity;
import net.hyper_pigeon.camera.networking.CameraNetworkingConstants;
import net.hyper_pigeon.camera.persistent_state.ImagePersistentState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ImageItem extends DecorationItem {

    public ImageItem(Settings settings) {
        super(Camera.IMAGE_ENTITY,settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient()) {
            openClientGui(stack);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Environment(EnvType.CLIENT)
    private void openClientGui(ItemStack stack) {
        MinecraftClient.getInstance().openScreen(new ImageScreen(stack));
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos = context.getBlockPos();
        Direction direction = context.getSide();
        BlockPos blockPos2 = blockPos.offset(direction);
        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();
        if (playerEntity != null && !this.canPlaceOn(playerEntity, direction, itemStack, blockPos2)) {
            return ActionResult.FAIL;
        }
        World world = context.getWorld();
        ImageEntity imageEntity = new ImageEntity(Camera.IMAGE_ENTITY,world);
        imageEntity.setImageID(itemStack.getTag().getUuid("id"));
        imageEntity.setImageIdentifier((itemStack.getTag().getString("imageIdentifier")));
        imageEntity.setImageBytes(itemStack.getTag().getByteArray("imageBytes"));
        imageEntity.setImageHeight(itemStack.getTag().getInt("height"));
        imageEntity.setImageWidth(itemStack.getTag().getInt("width"));

        return ActionResult.CONSUME;
    }

}
