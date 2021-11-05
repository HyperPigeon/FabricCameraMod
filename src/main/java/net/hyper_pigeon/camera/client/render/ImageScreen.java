package net.hyper_pigeon.camera.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.hyper_pigeon.camera.Camera;
import net.hyper_pigeon.camera.networking.CameraNetworkingConstants;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureUtil;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.ScreenshotUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.DataFormatException;


public class ImageScreen extends Screen {

    ItemStack stack;
    Identifier imageIdentifier;
    final int imageID;
    final int imageWidth;
    final int imageHeight;
    final byte[] imageBytes;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private static final Logger LOGGER = LogManager.getLogger();



    public ImageScreen(ItemStack itemStack) {
        super(NarratorManager.EMPTY);
        stack = itemStack;
        imageIdentifier =  Identifier.tryParse(stack.getTag().getString("imageIdentifier"));
        imageID = stack.getTag().getInt("id");
        imageWidth = stack.getTag().getInt("width");
        imageHeight = stack.getTag().getInt("height");
        //base64imageBytes = stack.getTag().getString("base64ImageBytes");
        imageBytes = stack.getTag().getByteArray("imageBytes");
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        RenderSystem.color4f(1F, 1F, 1F, 1F);


        if (imageIdentifier != null) {
            if (this.client.getTextureManager().getTexture(imageIdentifier) != null){
                this.client.getTextureManager().bindTexture(imageIdentifier);
                drawImage(width, height, 100);
            }
            else {
                NativeImage nativeImage = null;
                try {
                    //System.out.println(Base64.encodeBase64String(imageBytes));
                    //System.out.println(imageBytes.length);
                    nativeImage = NativeImage.read((new ByteArrayInputStream(imageBytes)));
                    NativeImageBackedTexture nativeImageBackedTexture = new NativeImageBackedTexture(nativeImage);
                    saveScreenshotInner(nativeImage,this.client.runDirectory,null);
                    client.getTextureManager().registerDynamicTexture("camera/" + imageID,nativeImageBackedTexture);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                this.client.getTextureManager().bindTexture(imageIdentifier);
                drawImage(width, height, 100);
            }

        }
    }

    private void drawImage(int width, int height, float zLevel) {
        RenderSystem.pushMatrix();

        float imageWidth = 12F;
        float imageHeight = 8F;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE);

        float scale = 0.95F;

        float ws = (float) width * scale;
        float hs = (float) height * scale;

        float rs = ws / hs;
        float ri = imageWidth / imageHeight;

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

        buffer.vertex(left, top, zLevel).texture(0F, 0F).next();
        buffer.vertex(left, top + hnew, zLevel).texture(0F, 1F).next();
        buffer.vertex(left + wnew, top + hnew, zLevel).texture(1F, 1F).next();
        buffer.vertex(left + wnew, top, zLevel).texture(1F, 0F).next();

        tessellator.draw();

        RenderSystem.popMatrix();
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 85) {
            NativeImageBackedTexture nativeImageBackedTexture= (NativeImageBackedTexture) this.client.getTextureManager().getTexture(imageIdentifier);
            NativeImage nativeImage = nativeImageBackedTexture.getImage();

            saveScreenshotInner(nativeImage,this.client.runDirectory,null);

            PacketByteBuf packetByteBuf = PacketByteBufs.create();
            try {
                packetByteBuf.writeBytes(nativeImage.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(Camera.CONFIG.saveImagesOnServer){
                ClientPlayNetworking.send(CameraNetworkingConstants.SEND_TO_SERVER,packetByteBuf);
            }






//            Framebuffer framebuffer = new Framebuffer(nativeImage.getWidth(), nativeImage.getHeight(), true, true);
//            ScreenshotUtils.saveScreenshot(this.client.runDirectory,nativeImage.getWidth(),nativeImage.getHeight(),framebuffer,
//                    (text) -> {
//                        this.client.execute(() -> {
//                            this.client.inGameHud.getChatHud().addMessage(text);
//                        });
//                    });

        }

        return super.keyPressed(keyCode,scanCode,modifiers);
    }

    private void saveScreenshotInner(NativeImage image, File gameDirectory, @Nullable String fileName) {
        NativeImage nativeImage = image;
        File file = new File(gameDirectory, "screenshots");
        file.mkdir();
        File file3;
        if (fileName == null) {
            String string = DATE_FORMAT.format(new Date());
            int i = 1;
            while(true) {
                File fileTemp = new File(file, string + (i == 1 ? "" : "_" + i) + ".png");
                if (!fileTemp.exists()) {
                    file3 = fileTemp;
                    break;
                }

                ++i;
            }
        } else {
            file3 = new File(file, fileName);
        }

        Util.getIoWorkerExecutor().execute(() -> {
            try {
                nativeImage.writeFile(file3);
                Text text = (new LiteralText(file3.getName())).formatted(Formatting.UNDERLINE).styled((style) -> {
                    return style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file3.getAbsolutePath()));
                });
                this.client.inGameHud.getChatHud().addMessage((new TranslatableText("screenshot.success", new Object[]{text})));
            } catch (Exception var7) {
                Text text = (new LiteralText(file3.getName())).formatted(Formatting.UNDERLINE).styled((style) -> {
                    return style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file3.getAbsolutePath()));
                });
                LOGGER.warn("Couldn't save screenshot", var7);
                this.client.inGameHud.getChatHud().addMessage((new TranslatableText("screenshot.success", new Object[]{text})));
            } finally {
                nativeImage.close();
            }

        });
    }
}
