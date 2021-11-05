package net.hyper_pigeon.camera.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hyper_pigeon.camera.client.render.CameraScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;


public class CameraItem extends Item {
    public CameraItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if (world.isClient()){
            openCameraScreen(world,user,hand);
            return TypedActionResult.success(user.getStackInHand(hand));
        }

        //int imageId = world.getNextMapId();

//            else{
//                boolean isHUDhidden = mc.options.hudHidden;
//                mc.options.hudHidden = true;
//                NativeImage nativeImage = ScreenshotUtils.takeScreenshot(mc.getWindow().getWidth(), mc.getWindow().getHeight(), mc.getFramebuffer());
//                mc.options.hudHidden = false;
//                NativeImageBackedTexture nativeImageBackedTexture = new NativeImageBackedTexture(nativeImage);
//
//                //mc.getTextureManager().registerTexture(Identifier.tryParse("camera:" + imageId),nativeImageBackedTexture);
//                Identifier identifier = mc.getTextureManager().registerDynamicTexture("camera/" + imageId,nativeImageBackedTexture);
//
//
//                PacketByteBuf buf = PacketByteBufs.create();
//                buf.writeInt(imageId);
//                buf.writeString(identifier.toString());
//                ClientPlayNetworking.send(CameraNetworkingConstants.SEND_SCREENSHOT_IMAGE,buf);
//
//            }





//        int imageId = world.getNextMapId();
//
//        if(world.isClient()){
//            MinecraftClient mc = MinecraftClient.getInstance();
//            boolean isHUDhidden = mc.options.hudHidden;
//            mc.options.hudHidden = true;
//            NativeImage nativeImage = ScreenshotUtils.takeScreenshot(mc.getWindow().getWidth(), mc.getWindow().getHeight(), mc.getFramebuffer());
//            mc.options.hudHidden = false;
//            NativeImageBackedTexture nativeImageBackedTexture = new NativeImageBackedTexture(nativeImage);
//
//            //mc.getTextureManager().registerTexture(Identifier.tryParse("camera:" + imageId),nativeImageBackedTexture);
//            Identifier identifier = mc.getTextureManager().registerDynamicTexture("camera/" + imageId,nativeImageBackedTexture);
//
//
//            PacketByteBuf buf = PacketByteBufs.create();
//            buf.writeInt(imageId);
//            buf.writeString(identifier.toString());
//            ClientPlayNetworking.send(CameraNetworkingConstants.SEND_SCREENSHOT_IMAGE,buf);
//
//        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Environment(EnvType.CLIENT)
    public void openCameraScreen(World world, PlayerEntity user, Hand hand){

            MinecraftClient mc = MinecraftClient.getInstance();

            if (!(mc.currentScreen instanceof CameraScreen)) {
                mc.openScreen(new CameraScreen(mc.options.fov, world));
            }

    }



}