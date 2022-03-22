package net.hyper_pigeon.camera.block;

import com.google.common.collect.ImmutableMap;
import net.hyper_pigeon.camera.Camera;
import net.hyper_pigeon.camera.block.entity.ImageFrameBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class ImageFrameBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = Properties.LIT;

    //public static final Map<Direction, VoxelShape> SHAPES;


    public ImageFrameBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(LIT, false)
        );
    }


        @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

//    @Override
//    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
//        return SHAPES.get(state.get(FACING));
//    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.offset(state.get(FACING).getOpposite())).getMaterial().isSolid();
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        Direction facing = state.get(FACING);
        ImageFrameBlockEntity imageFrameBlockEntity = createBlockEntity(pos,world);

        if(hit.getSide() == facing){
            if(stack.getItem().equals(Camera.IMAGE_ITEM)){
                imageFrameBlockEntity.setImageInfo(stack.getTag().getString("imageIdentifier"),stack.getTag().getUuid("id"),
                        stack.getTag().getInt("width"),stack.getTag().getInt("height"),stack.getTag().getByteArray("imageBytes"));
                stack.decrement(1);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.FAIL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return Camera.IMAGE_FRAME_BLOCK_ENTITY.instantiate();
    }

    @Nullable
    public ImageFrameBlockEntity createBlockEntity(BlockPos pos, World world){
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof ImageFrameBlockEntity){
            return (ImageFrameBlockEntity) blockEntity;
        }
        return null;
    }



//    static{
//        ImmutableMap.Builder builder = ImmutableMap.<Direction, VoxelShape>builder();
//
//        builder.put(Direction.NORTH, createCuboidShape(0.0, 0.0, 15.0, 16.0, 16.0, 16.0));
//        builder.put(Direction.EAST, createCuboidShape(0.0, 0.0, 0.0, 1.0, 16.0, 16.0));
//        builder.put(Direction.SOUTH, createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 1.0));
//        builder.put(Direction.WEST, createCuboidShape(15.0, 0.0, 0.0, 16.0, 16.0, 16.0));
//
//        SHAPES = new EnumMap<>(builder.build());
//    }




}
