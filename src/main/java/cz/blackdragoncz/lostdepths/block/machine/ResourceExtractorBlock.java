package cz.blackdragoncz.lostdepths.block.machine;

import cz.blackdragoncz.lostdepths.block.base.BaseHorizontalFacingEntityBlock;
import cz.blackdragoncz.lostdepths.block.entity.ResourceExtractorBlockEntity;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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

public class ResourceExtractorBlock extends BaseHorizontalFacingEntityBlock {

    public ResourceExtractorBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.METAL)
                .strength(6f, 12f)
                .pushReaction(PushReaction.BLOCK));
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (BlockEntityTicker<T>) createTickerHelper(
                LostdepthsModBlockEntities.RESOURCE_EXTRACTOR.get(),
                LostdepthsModBlockEntities.RESOURCE_EXTRACTOR.get(),
                ResourceExtractorBlockEntity::serverTick);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, @Nullable BlockGetter world, List<Component> list, TooltipFlag flag) {
        list.add(Component.literal("§7Place on top of an ore block"));
        list.add(Component.literal("§7Insert a pickaxe to extract resources"));
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
        return new ResourceExtractorBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        super.use(state, level, pos, player, hand, hit);
        if (player.isSpectator()) return InteractionResult.PASS;
        if (player instanceof ServerPlayer serverPlayer) {
            if (level.getBlockEntity(pos) instanceof ResourceExtractorBlockEntity be) {
                NetworkHooks.openScreen(serverPlayer, be, pos);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof ResourceExtractorBlockEntity be) {
                Containers.dropContents(level, pos, be);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof ResourceExtractorBlockEntity be) {
            be.setRedstoneActive(!level.hasNeighborSignal(pos));
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof ResourceExtractorBlockEntity be) {
            return be.getOutputSignal();
        }
        return 0;
    }
}
