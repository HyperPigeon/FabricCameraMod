package net.hyper_pigeon.camera;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.hyper_pigeon.camera.block.ImageFrameBlock;
import net.hyper_pigeon.camera.block.entity.ImageFrameBlockEntity;
import net.hyper_pigeon.camera.client.render.ImageRenderer;
import net.hyper_pigeon.camera.config.CameraConfig;
import net.hyper_pigeon.camera.entity.ImageFrameEntity;
import net.hyper_pigeon.camera.items.CameraItem;
import net.hyper_pigeon.camera.items.ImageFrameItem;
import net.hyper_pigeon.camera.items.ImageItem;
import net.hyper_pigeon.camera.networking.CameraNetworkingConstants;
import net.hyper_pigeon.camera.persistent_state.ImagePersistentState;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.CallbackI;
import org.lwjgl.system.MemoryStack;

import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Camera implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("Camera");

	public static final CameraItem CAMERA_ITEM = new CameraItem(new Item.Settings().maxCount(1).group(ItemGroup.MISC));

	public static final ImageItem IMAGE_ITEM = new ImageItem(new Item.Settings().maxCount(1).group(ItemGroup.DECORATIONS));

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

	public static CameraConfig CONFIG =  AutoConfig.register(CameraConfig.class, JanksonConfigSerializer::new).getConfig();

	public static final ImageFrameBlock IMAGE_FRAME_BLOCK = new ImageFrameBlock(AbstractBlock.Settings.of(Material.WOOD, MaterialColor.BROWN).strength(1.0F).sounds(BlockSoundGroup.WOOD));
	public static final ImageFrameItem IMAGE_FRAME_ITEM = new ImageFrameItem(IMAGE_FRAME_BLOCK,new Item.Settings().maxCount(64).group(ItemGroup.DECORATIONS));
	public static final BlockEntityType<ImageFrameBlockEntity> IMAGE_FRAME_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
			new Identifier("camera","image_frame_block"),
			BlockEntityType.Builder.create(ImageFrameBlockEntity::new, IMAGE_FRAME_BLOCK).build(null));

//	public static EntityType<ImageFrameEntity> IMAGE_FRAME_ENTITY = Registry.register(Registry.ENTITY_TYPE,
//			new Identifier("camera","image_frame_entity"),
//			FabricEntityTypeBuilder.<ImageFrameEntity>create(SpawnGroup.MISC, ImageFrameEntity::new).size(EntityDimensions.fixed(0.5F, 0.5F))
//					.trackable(20,4).build());

//	public static EntityType<ImageEntity> IMAGE_ENTITY = Registry.register(Registry.ENTITY_TYPE, new Identifier("camera", "image_entity"),
//			FabricEntityTypeBuilder.create(SpawnGroup.MISC, ImageEntity::new).size(EntityDimensions.fixed(0.6F, 0.7F)).build());

	public void onInitialize() {
		Registry.register(Registry.ITEM,new Identifier("camera", "camera"), CAMERA_ITEM);
		Registry.register(Registry.ITEM, new Identifier("camera", "image"), IMAGE_ITEM);

		Registry.register(Registry.BLOCK, new Identifier("camera","image_frame"),IMAGE_FRAME_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("camera","image_frame"), IMAGE_FRAME_ITEM);


		ServerPlayNetworking.registerGlobalReceiver(CameraNetworkingConstants.SEND_SCREENSHOT_IMAGE, (server,player, handler, buf, responseSender) -> {

			//ByteBuffer byteBuffer = ByteBuffer.wrap(buf.readByteArray());
			//int imageId = player.getServerWorld().getNextMapId();
			UUID imageId = buf.readUuid();
			//int[] dimArray = buf.readIntArray();
			int width = (int) buf.readDouble();
			int height = (int) buf.readFloat();
			String identifier = buf.readString(32767);
			//Identifier identifier = buf.readIdentifier();

			//byte[] imageBytes = buf.readByteArray();
			//System.out.println(imageBytes.length);
			//System.out.println("buf byte array length:" + imageBytes.length);
			//long length = buf.readLong();
			//String base64ImageBytes = buf.readString(1999999999);
			//System.out.println("imageBytes:" + imageBytes.length);

			//byte[] imageBytes = Longs.toByteArray(buf.readLong());

//			byte[] imageBytes = new byte[0];
//
//			for (int i = 0; i < 30; i++){
//				if(i == 0){
//					imageBytes = buf.readByteArray();
//				}
//				else {
//					imageBytes = ArrayUtils.addAll(imageBytes,buf.readByteArray());
//				}
//
//			}

			//byte[] finalImageBytes = imageBytes;
			server.execute(() -> {
				ItemStack imageItemStack = new ItemStack(Camera.IMAGE_ITEM);
				imageItemStack.getOrCreateTag().putUuid("id",imageId);
				imageItemStack.getOrCreateTag().putString("imageIdentifier", identifier);
				imageItemStack.getOrCreateTag().putInt("width",width);
				imageItemStack.getOrCreateTag().putInt("height",height);
				//imageItemStack.getOrCreateTag().putString("base64ImageBytes",base64ImageBytes);
				//System.out.println("length:" + ImagePersistentState.get(player.getServerWorld()).getByteArray(identifier).length);
				imageItemStack.getOrCreateTag().putByteArray("imageBytes", ImagePersistentState.get(player.getServerWorld()).getByteArray(identifier));
				//imageItemStack.getOrCreateTag().putInt("imageBytesLength", (int) length);
				player.giveItemStack(imageItemStack);
			});
        });

		ServerPlayNetworking.registerGlobalReceiver(CameraNetworkingConstants.SEND_TO_SERVER, (server,player, handler, buf, responseSender) -> {
			byte[] bytes = buf.readByteArray();
			ByteBuffer buffer1 = ByteBuffer.wrap(bytes);
			ByteBuffer buffer2 = clone(buffer1);

			try {
				NativeImage nativeImage = NativeImage.read(buffer2);
				saveScreenshotInner(nativeImage, new File(CONFIG.serverImageDirectoryPath),null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(CameraNetworkingConstants.SEND_IMAGE_BYTES, ((server, player, handler, buf, responseSender) ->
		{
			String identifier = buf.readString();
			byte[] bytes = buf.readByteArray();
			//System.out.println(identifier);
			ImagePersistentState imagePersistentState = ImagePersistentState.get(player.getServerWorld());
			server.execute(() -> {
				if (!imagePersistentState.containsID(identifier)){
					imagePersistentState.addByteArray(identifier,bytes);
				}
				else {
					imagePersistentState.appendByteArray(identifier,bytes);
				}
			});
		}));



	}

	public static ByteBuffer clone(ByteBuffer original) {
		MemoryStack memoryStack = MemoryStack.stackPush();
		ByteBuffer clone = memoryStack.malloc(original.capacity());
		original.rewind();//copy from the beginning
		clone.put(original);
		original.rewind();
		clone.flip();
		memoryStack.close();
		return clone;
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
			} catch (Exception var7) {
			} finally {
				nativeImage.close();
			}

		});
	}

	//didn't work OOF
	public static byte[] compress(byte[] data) throws IOException {
		Deflater deflater = new Deflater();
		deflater.setInput(data);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

		deflater.finish();
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer); // returns the generated code... index
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();


		return outputStream.toByteArray();
	}

	public static byte[] decompress(byte[] data, int length) throws IOException, DataFormatException {
		Inflater inflater = new Inflater(true);
		inflater.setInput(data);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[length];
		while (!inflater.finished()) {
			int count = inflater.inflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		byte[] output = outputStream.toByteArray();
		inflater.end();

		return output;
	}




}
