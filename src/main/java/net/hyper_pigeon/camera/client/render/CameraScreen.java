package net.hyper_pigeon.camera.client.render;

import com.google.common.primitives.Longs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.hyper_pigeon.camera.ByteUtils;
import net.hyper_pigeon.camera.Camera;
import net.hyper_pigeon.camera.networking.CameraNetworkingConstants;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureUtil;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.ScreenshotUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.DataFormatException;

public class CameraScreen extends Screen {

    public final double defaultFOV;
    public double currentFOV;
    private World world;

    public CameraScreen(double fov, World world) {
        super(NarratorManager.EMPTY);
        defaultFOV = fov;
        currentFOV = defaultFOV;
        this.world = world;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.client.options.hudHidden = true;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers){
        int imageId = world.getNextMapId();
        if(keyCode == 67){
                boolean isHUDhidden = client.options.hudHidden;
                client.options.hudHidden = true;
                NativeImage nativeImage = ScreenshotUtils.takeScreenshot(client.getWindow().getWidth(), client.getWindow().getHeight(), client.getFramebuffer());
                client.options.hudHidden = false;
                NativeImageBackedTexture nativeImageBackedTexture = new NativeImageBackedTexture(nativeImage);

                //mc.getTextureManager().registerTexture(Identifier.tryParse("camera:" + imageId),nativeImageBackedTexture);
                Identifier identifier = client.getTextureManager().registerDynamicTexture("camera/" + imageId,nativeImageBackedTexture);
                nativeImageBackedTexture.upload();

                PacketByteBuf buf = PacketByteBufs.create();

                //ArrayList<PacketByteBuf> byteStoragePackets = new ArrayList<>();
//                for(int i = 0; i < 30; i++){
//                    //PacketByteBuf byteStorage = PacketByteBufs.create();
//                    try {
//                        if(i == 29){
//                            buf.writeBytes(Arrays.copyOfRange(nativeImage.getBytes(),i*(nativeImage.getBytes().length/30),nativeImage.getBytes().length));
//                        }
//                        else {
//                            buf.writeBytes(Arrays.copyOfRange(nativeImage.getBytes(),i*(nativeImage.getBytes().length/30),(i+1)*(nativeImage.getBytes().length/30)));
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                try {
//                    buf = buf.writeByteArray(nativeImage.getBytes());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            //int[] dimArray = new int[2];
                //dimArray[0] = nativeImage.getWidth();
                //dimArray[1] = nativeImage.getHeight();
                //buf.writeIntArray(dimArray);
//                try {
//                    buf.writeBytes(nativeImage.getBytes());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

//            try {
//                byte[] compressed = Camera.compress(nativeImage.getBytes());
//                System.out.println("compress length:" + compressed.length);
//                System.out.println("decompress length:" + Camera.decompress(compressed,compressed.length).length);
//            } catch (IOException | DataFormatException e) {
//                e.printStackTrace();
//            }

            //using jank way to write width and height (and framebuffer) since using array results in crash
                buf.writeDouble(nativeImage.getWidth());
                buf.writeFloat(nativeImage.getHeight());
                buf.writeInt(imageId);
                buf.writeString(String.valueOf(identifier));

//                //write byte array to long
//                try {
//                    buf.writeLong(Longs.fromByteArray(nativeImage.getBytes()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            //write byte array of image to packet
//            try {
//                buf.writeBytes(TextureUtil.readAllToByteBuffer(new ByteArrayInputStream(nativeImage.getBytes())));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//                try {
//                    //System.out.println("og length:" + Camera.compress(nativeImage.getBytes()).length);
//                    //buf.writeBytes(TextureUtil.readAllToByteBuffer(new ByteArrayInputStream(nativeImage.getBytes())));
//                    buf.writeBytes((nativeImage.getBytes()));
//                 } catch (IOException e) {
//                    e.printStackTrace();
//                }

            ClientPlayNetworking.send(CameraNetworkingConstants.SEND_SCREENSHOT_IMAGE,buf);
        }
        if(keyCode == GLFW.GLFW_KEY_W){
            this.client.player.pitch += 1;
        }
        if(keyCode ==  GLFW.GLFW_KEY_S){
            this.client.player.pitch -= 1;
        }
        if(keyCode ==  GLFW.GLFW_KEY_D){
            this.client.player.yaw += 1;
        }
        if(keyCode ==  GLFW.GLFW_KEY_A){
            this.client.player.yaw -= 1;
        }
        return super.keyPressed(keyCode,scanCode,modifiers);
    }


    public boolean isPauseScreen() {
        return false;
    }

    public void onClose() {
        this.client.options.hudHidden = false;
        this.client.options.fov = defaultFOV;
        super.onClose();
    }


}
