package cz.blackdragoncz.lostdepths.block.storage;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.Nullable;

public class NTControllerBlock extends Block implements EntityBlock {
	public NTControllerBlock() {
		super(BlockBehaviour.Properties.of()
				.mapColor(MapColor.STONE)
				.strength(3.5f)
				.sound(SoundType.STONE)
				.requiresCorrectToolForDrops());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new NTControllerBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (level.isClientSide()) return null;
		return (lvl, pos, st, be) -> {
			if (be instanceof NTControllerBlockEntity controller) {
				NTControllerBlockEntity.serverTick(lvl, pos, st, controller);
			}
		};
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean movedByPiston) {
		super.neighborChanged(state, level, pos, block, neighborPos, movedByPiston);
		if (!level.isClientSide() && level.getBlockEntity(pos) instanceof NTControllerBlockEntity controller) {
			controller.onNeighborChanged();
		}
	}
}
