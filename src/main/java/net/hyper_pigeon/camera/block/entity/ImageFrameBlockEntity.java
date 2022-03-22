package net.hyper_pigeon.camera.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.hyper_pigeon.camera.Camera;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ImageFrameBlockEntity extends BlockEntity  {

    public String imageIdentifier;
    public UUID imageID;
    public int imageWidth;
    public int imageHeight;
    public byte[] imageBytes;


    @Environment(EnvType.CLIENT)
    private boolean dirty = true;
    @Environment(EnvType.CLIENT)
    public Mesh mesh = null;

    public ImageFrameBlockEntity() {
        super(Camera.IMAGE_FRAME_BLOCK_ENTITY);
    }


    public void setImageInfo(String identifier, UUID id, int width, int height, byte[] bytes){
        imageIdentifier = identifier;
        imageID = id;
        imageWidth = width;
        imageHeight = height;
        imageBytes = bytes;

    }

    public void fromTag(BlockState state, CompoundTag tag) {
        imageIdentifier = tag.getString("identifier");
        imageID = tag.getUuid("imageID");
        imageWidth = tag.getInt("imageWidth");
        imageHeight = tag.getInt("imageHeight");
        imageBytes = tag.getByteArray("imageBytes");
        markDirty();
        super.fromTag(state,tag);
    }

    public CompoundTag toTag(CompoundTag tag) {
        return this.writeIdentifyingData(tag);
    }

    private CompoundTag writeIdentifyingData(CompoundTag tag) {
        Identifier identifier = BlockEntityType.getId(this.getType());
        if (identifier == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        } else {
            //tag.putString("id", identifier.toString());
            tag.putInt("x", this.pos.getX());
            tag.putInt("y", this.pos.getY());
            tag.putInt("z", this.pos.getZ());

            if(imageIdentifier != null) {
                tag.putString("identifier",imageIdentifier);
                tag.putUuid("imageID",imageID);
                tag.putInt("imageWidth",imageWidth);
                tag.putInt("imageHeight",imageHeight);
                tag.putByteArray("imageBytes",imageBytes);
            }

            return tag;
        }
    }



}
