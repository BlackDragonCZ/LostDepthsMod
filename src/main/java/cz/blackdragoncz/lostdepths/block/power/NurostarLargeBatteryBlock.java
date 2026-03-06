package cz.blackdragoncz.lostdepths.block.power;

import cz.blackdragoncz.lostdepths.block.base.BaseHorizontalFacingEntityBlock;
import cz.blackdragoncz.lostdepths.block.multiblock.MultiblockHelper;
import cz.blackdragoncz.lostdepths.block.power.entity.NurostarBatteryBlockEntity;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NurostarLargeBatteryBlock extends BaseHorizontalFacingEntityBlock {

    private static final List<BlockPos> PART_OFFSETS = List.of(
            new BlockPos(0, 1, 0)
    );

    public NurostarLargeBatteryBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.METAL)
                .strength(6f, 12f)
                .pushReaction(PushReaction.BLOCK));
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 15;
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
        return BlockPathTypes.BLOCKED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return NurostarBatteryBlockEntity.createLarge(pos, state);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (BlockEntityTicker<T>) createTickerHelper(
                LostdepthsModBlockEntities.NUROSTAR_LARGE_BATTERY.get(),
                LostdepthsModBlockEntities.NUROSTAR_LARGE_BATTERY.get(),
                NurostarBatteryBlockEntity::serverTick);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && placer instanceof Player player) {
            if (!MultiblockHelper.tryPlace(level, pos, Direction.NORTH, PART_OFFSETS, player, LostdepthsModBlocks.MULTIBLOCK_DUMMY.get())) {
                level.destroyBlock(pos, true);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        super.use(state, level, pos, player, hand, hit);
        if (player instanceof ServerPlayer serverPlayer) {
            if (level.getBlockEntity(pos) instanceof NurostarBatteryBlockEntity be) {
                NetworkHooks.openScreen(serverPlayer, be, pos);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (!level.isClientSide) {
                MultiblockHelper.removeParts(level, pos, Direction.NORTH, PART_OFFSETS);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}
