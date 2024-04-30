package cz.blackdragoncz.lostdepths.block.power;

import com.mojang.logging.LogUtils;
import cz.blackdragoncz.lostdepths.block.base.BaseHorizontalFacingEntityBlock;
import cz.blackdragoncz.lostdepths.block.power.entity.NurostarGeneratorBlockEntity;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.util.NothingNullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@NothingNullByDefault
public class NurostarGeneratorBlock extends BaseHorizontalFacingEntityBlock {

    public NurostarGeneratorBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.METAL)
                .strength(6f, 12f)
                .pushReaction(PushReaction.BLOCK));
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (BlockEntityTicker<T>) createTickerHelper(LostdepthsModBlockEntities.NUROSTAR_GENRATOR.get(), LostdepthsModBlockEntities.NUROSTAR_GENRATOR.get(), NurostarGeneratorBlockEntity::serverTick);
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
}
