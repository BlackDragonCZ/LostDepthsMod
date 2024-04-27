package cz.blackdragoncz.lostdepths.block.base;

import cz.blackdragoncz.lostdepths.util.NothingNullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

@NothingNullByDefault
public abstract class BaseFacingEntityBlock extends BaseFacingBlock implements EntityBlock {

    public BaseFacingEntityBlock(Properties pProperties) {
        super(pProperties);
    }

    public boolean triggerEvent(BlockState pState, Level pLevel, BlockPos pPos, int pId, int pParam) {
        super.triggerEvent(pState, pLevel, pPos, pId, pParam);
        BlockEntity $$5 = pLevel.getBlockEntity(pPos);
        return $$5 == null ? false : $$5.triggerEvent(pId, pParam);
    }

    @javax.annotation.Nullable
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        BlockEntity $$3 = pLevel.getBlockEntity(pPos);
        return $$3 instanceof MenuProvider ? (MenuProvider)$$3 : null;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
        return pClientType == pServerType ? (BlockEntityTicker<A>) pTicker : null;
    }
}
