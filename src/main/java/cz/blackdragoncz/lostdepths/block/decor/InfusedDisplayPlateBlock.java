package cz.blackdragoncz.lostdepths.block.decor;

import cz.blackdragoncz.lostdepths.block.entity.InfusedDisplayPlateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Collections;
import java.util.List;

public class InfusedDisplayPlateBlock extends FaceAttachedHorizontalDirectionalBlock implements EntityBlock {

    private static final VoxelShape SHAPE_FLOOR = Block.box(0, 0, 0, 16, 2, 16);
    private static final VoxelShape SHAPE_CEILING = Block.box(0, 14, 0, 16, 16, 16);
    private static final VoxelShape SHAPE_WALL_SOUTH = Block.box(0, 0, 0, 16, 16, 2);
    private static final VoxelShape SHAPE_WALL_NORTH = Block.box(0, 0, 14, 16, 16, 16);
    private static final VoxelShape SHAPE_WALL_EAST = Block.box(0, 0, 0, 2, 16, 16);
    private static final VoxelShape SHAPE_WALL_WEST = Block.box(14, 0, 0, 16, 16, 16);

    public InfusedDisplayPlateBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(1f, 6f).lightLevel(s -> 7).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACE, AttachFace.FLOOR)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACE, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) return null;
        // On floor, invert facing so the item faces toward the player
        if (state.getValue(FACE) == AttachFace.FLOOR) {
            state = state.setValue(FACING, state.getValue(FACING).getOpposite());
        }
        return state;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction supportDir = getSupportDirection(state);
        BlockPos supportPos = pos.relative(supportDir);
        return !level.getBlockState(supportPos).getCollisionShape(level, supportPos).isEmpty();
    }

    public static Direction getSupportDirection(BlockState state) {
        return switch (state.getValue(FACE)) {
            case FLOOR -> Direction.DOWN;
            case CEILING -> Direction.UP;
            case WALL -> state.getValue(FACING).getOpposite();
        };
    }

    public static Direction getOutwardDirection(BlockState state) {
        return switch (state.getValue(FACE)) {
            case FLOOR -> Direction.UP;
            case CEILING -> Direction.DOWN;
            case WALL -> state.getValue(FACING);
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(FACE) == AttachFace.FLOOR) return SHAPE_FLOOR;
        if (state.getValue(FACE) == AttachFace.CEILING) return SHAPE_CEILING;
        return switch (state.getValue(FACING)) {
            case SOUTH -> SHAPE_WALL_SOUTH;
            case NORTH -> SHAPE_WALL_NORTH;
            case EAST -> SHAPE_WALL_EAST;
            case WEST -> SHAPE_WALL_WEST;
            default -> SHAPE_FLOOR;
        };
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InfusedDisplayPlateBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof InfusedDisplayPlateBlockEntity plate))
            return InteractionResult.FAIL;

        ItemStack inHand = player.getItemInHand(hand);
        ItemStack inPlate = plate.getHeldItem();

        if (!inPlate.isEmpty()) {
            player.getInventory().placeItemBackInInventory(inPlate.copy());
            plate.setHeldItem(ItemStack.EMPTY);
            level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1, 1);
            return InteractionResult.CONSUME;
        }

        if (!inHand.isEmpty()) {
            plate.setHeldItem(inHand.copyWithCount(1));
            if (!player.isCreative()) {
                inHand.shrink(1);
            }
            level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1, 1);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (level.isClientSide)
            return;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof InfusedDisplayPlateBlockEntity plate))
            return;

        ItemStack heldItem = plate.getHeldItem();
        if (heldItem.isEmpty())
            return;

        player.getInventory().placeItemBackInInventory(heldItem.copy());
        plate.setHeldItem(ItemStack.EMPTY);
        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1, 1);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof InfusedDisplayPlateBlockEntity plate) {
                ItemStack item = plate.getHeldItem();
                if (!item.isEmpty()) {
                    Block.popResource(level, pos, item);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }
}
