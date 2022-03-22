package net.hyper_pigeon.camera.items;

import net.hyper_pigeon.camera.Camera;
import net.hyper_pigeon.camera.entity.ImageFrameEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemFrameItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.lwjgl.system.CallbackI;

public class ImageFrameItem extends BlockItem {
    public ImageFrameItem(Block block, Settings settings) {
        super(block, settings);
    }


//    public ActionResult useOnBlock(ItemUsageContext context) {
//        BlockPos blockPos = context.getBlockPos();
//        Direction direction = context.getSide();
//        BlockPos blockPos2 = blockPos.offset(direction);
//        PlayerEntity playerEntity = context.getPlayer();
//        ItemStack itemStack = context.getStack();
//        if (playerEntity != null && !this.canPlaceOn(playerEntity, direction, itemStack, blockPos2)) {
//            return ActionResult.FAIL;
//        } else {
//            World world = context.getWorld();
//            ImageFrameEntity imageFrameEntity;
//
//            if(itemStack.getItem().equals(Camera.IMAGE_FRAME_ITEM)){
//                imageFrameEntity = new ImageFrameEntity(world, blockPos, direction);
//                //imageFrameEntity = new ImageFrameEntity(Camera.IMAGE_FRAME_ENTITY,world);
//
//                if (imageFrameEntity.canStayAttached()) {
//                    if (!world.isClient) {
//                        imageFrameEntity.onPlace();
//                        world.spawnEntity(imageFrameEntity);
//                    }
//
//                    itemStack.decrement(1);
//                    return ActionResult.success(world.isClient);
//                } else {
//                    return ActionResult.CONSUME;
//                }
//            }
//            else{
//                return ActionResult.FAIL;
//            }
//        }
//    }
}
