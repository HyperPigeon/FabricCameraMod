package net.hyper_pigeon.camera.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ImageEntity extends AbstractDecorationEntity {
    protected ImageEntity(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
        super(entityType, world);
    }

    protected ImageEntity(EntityType<? extends AbstractDecorationEntity> type, World world, BlockPos pos) {
        super(type, world, pos);
    }

    @Override
    public int getWidthPixels() {
        return 0;
    }

    @Override
    public int getHeightPixels() {
        return 0;
    }

    @Override
    public void onBreak(@Nullable Entity entity) {

    }

    @Override
    public void onPlace() {

    }

    @Override
    public Packet<?> createSpawnPacket() {
        return null;
    }
}
