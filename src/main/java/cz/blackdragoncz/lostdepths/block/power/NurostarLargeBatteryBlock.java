package cz.blackdragoncz.lostdepths.block.power;

import cz.blackdragoncz.lostdepths.block.base.BaseHorizontalFacingEntityBlock;
import cz.blackdragoncz.lostdepths.block.multiblock.MultiblockHelper;
import cz.blackdragoncz.lostdepths.block.power.entity.NurostarGeneratorBlockEntity;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NurostarLargeBatteryBlock extends BaseHorizontalFacingEntityBlock {

    // 1 wide x 2 tall x 1 deep: controller at bottom, dummy on top (vertical, no rotation needed)
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
    public void appendHoverText(ItemStack itemstack, @Nullable BlockGetter world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
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
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new NurostarGeneratorBlockEntity(blockPos, blockState);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && placer instanceof Player player) {
            // Vertical offsets don't need facing rotation
            if (!MultiblockHelper.tryPlace(level, pos, Direction.NORTH, PART_OFFSETS, player, LostdepthsModBlocks.MULTIBLOCK_DUMMY.get())) {
                level.destroyBlock(pos, true);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player entity, InteractionHand pHand, BlockHitResult pHit) {
        super.use(pState, pLevel, pPos, entity, pHand, pHit);
        if (entity instanceof ServerPlayer player) {
            NurostarGeneratorBlockEntity blockEntity = (NurostarGeneratorBlockEntity) pLevel.getBlockEntity(pPos);
            NetworkHooks.openScreen(player, blockEntity, pPos);
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
