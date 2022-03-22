package net.hyper_pigeon.camera.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hyper_pigeon.camera.client.render.ImageScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import space.essem.image2map.renderer.MapRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ImageItem extends Item {

    public ImageItem(Settings settings) {
        super(settings);
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


    @Environment(EnvType.CLIENT)
    public ImageScreen getImageScreen(ItemStack stack){
        return new ImageScreen(stack);
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
//        BlockPos blockPos = context.getBlockPos();
//        Direction direction = context.getSide();
//        BlockPos blockPos2 = blockPos.offset(direction);
//        PlayerEntity playerEntity = context.getPlayer();
//        ItemStack itemStack = context.getStack();
//        if (playerEntity != null && !this.canPlaceOn(playerEntity, direction, itemStack, blockPos2)) {
//            return ActionResult.FAIL;
//        }
//        World world = context.getWorld();
//        ImageEntity imageEntity = new ImageEntity(Camera.IMAGE_ENTITY,world);
//        imageEntity.setImageID(itemStack.getTag().getUuid("id"));
//        imageEntity.setImageIdentifier((itemStack.getTag().getString("imageIdentifier")));
//        imageEntity.setImageBytes(itemStack.getTag().getByteArray("imageBytes"));
//        imageEntity.setImageHeight(itemStack.getTag().getInt("height"));
//        imageEntity.setImageWidth(itemStack.getTag().getInt("width"));


        BlockPos blockPos = context.getBlockPos();
        Direction direction = context.getSide();
        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();

        if(!playerEntity.getEntityWorld().isClient()) {
            try {
                byte[] imageBytes = itemStack.getTag().getByteArray("imageBytes");
                int imageBytesLength = imageBytes.length;
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
                ItemStack mapItemStack = MapRenderer.render(bufferedImage, (ServerWorld) playerEntity.getEntityWorld(),
                        playerEntity.getX(), playerEntity.getZ(), playerEntity);
                ItemEntity itemEntity = new ItemEntity(playerEntity.world, playerEntity.getPos().x, playerEntity.getPos().y, playerEntity.getPos().z, mapItemStack);
                playerEntity.world.spawnEntity(itemEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ActionResult.CONSUME;
    }



}
