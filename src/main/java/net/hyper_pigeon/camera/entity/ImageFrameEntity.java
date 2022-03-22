package net.hyper_pigeon.camera.entity;

import net.hyper_pigeon.camera.Camera;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ImageFrameEntity extends ItemFrameEntity {
    public ImageFrameEntity(EntityType<? extends ItemFrameEntity> entityType, World world) {
        super(entityType, world);
    }


    public ImageFrameEntity(World world, BlockPos pos, Direction direction) {
        super(world,pos,direction);
        this.setFacing(direction);
        this.attachmentPos = pos;
    }



    public int getWidthPixels() {
        return 24;
    }

    public int getHeightPixels() {
        return 24;
    }


    public ActionResult interact(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        boolean bl = !this.getHeldItemStack().isEmpty();
        boolean bl2 = !itemStack.isEmpty();
        boolean bl3 = itemStack.getItem().equals(Camera.IMAGE_ITEM);
        if (!this.world.isClient) {
            if (!bl) {
                if (bl2 && !this.removed && bl3) {
                    this.setHeldItemStack(itemStack);
                    if (!player.abilities.creativeMode) {
                        itemStack.decrement(1);
                    }
                }
            }
            return ActionResult.CONSUME;
        } else {
            return !bl && !bl2 ? ActionResult.PASS : ActionResult.SUCCESS;
        }
    }

    public boolean canStayAttached() {
       if (!this.world.isSpaceEmpty(this)) {
            return false;
        } else {
            BlockState blockState = this.world.getBlockState(this.attachmentPos.offset(this.facing.getOpposite()));
            return blockState.getMaterial().isSolid() || this.facing.getAxis().isHorizontal() && AbstractRedstoneGateBlock.isRedstoneGate(blockState) ? this.world.getOtherEntities(this, this.getBoundingBox(), PREDICATE).isEmpty() : false;
        }
    }


}
