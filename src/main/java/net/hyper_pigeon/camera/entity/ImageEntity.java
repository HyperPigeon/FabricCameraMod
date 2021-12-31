package net.hyper_pigeon.camera.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hyper_pigeon.camera.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ImageEntity extends AbstractDecorationEntity {

    public String imageIdentifier;
    public UUID imageID;
    public int imageWidth;
    public int imageHeight;
    public byte[] imageBytes;

    public ImageEntity(EntityType<? extends ImageEntity> entityType, World world) {
        super(entityType, world);
    }


    public void setImageIdentifier(String identifier){
        imageIdentifier = identifier;
    }

    public void setImageID(UUID uuid){
        imageID = uuid;
    }

    public void setImageWidth(int width){
        imageWidth = width;
    }

    public void setImageHeight(int height){
        imageHeight = height;
    }

    public void setImageBytes(byte[] byteArray){
        imageBytes = byteArray;
    }

    @Override
    public int getWidthPixels() {
        return 8;
    }

    @Override
    public int getHeightPixels() {
        return 8;
    }

    @Override
    public void onBreak(@Nullable Entity entity) {
        if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            this.playSound(SoundEvents.ENTITY_PAINTING_BREAK, 1.0F, 1.0F);
            if (entity instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity)entity;
                if (playerEntity.abilities.creativeMode) {
                    return;
                }
            }

            ItemStack imageItemStack = new ItemStack(Camera.IMAGE_ITEM);
            imageItemStack.getOrCreateTag().putUuid("id",imageID);
            imageItemStack.getOrCreateTag().putString("imageIdentifier", imageIdentifier);
            imageItemStack.getOrCreateTag().putInt("width",imageWidth);
            imageItemStack.getOrCreateTag().putInt("height",imageHeight);
            imageItemStack.getOrCreateTag().putByteArray("imageBytes",imageBytes);

            this.dropStack(imageItemStack);
        }
    }

    @Override
    public void onPlace() {
        this.playSound(SoundEvents.ENTITY_PAINTING_PLACE, 1.0F, 1.0F);
    }

    public void refreshPositionAndAngles(double x, double y, double z, float yaw, float pitch) {
        this.updatePosition(x, y, z);
    }

    @Environment(EnvType.CLIENT)
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        BlockPos blockPos = this.attachmentPos.add(x - this.getX(), y - this.getY(), z - this.getZ());
        this.updatePosition((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ());
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return null;
    }
}
