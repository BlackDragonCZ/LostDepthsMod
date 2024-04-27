package cz.blackdragoncz.lostdepths.block.machine;

import cz.blackdragoncz.lostdepths.block.base.AbstractCompressorBlock;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

import cz.blackdragoncz.lostdepths.block.entity.BlackHoleCompressorBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlackHoleCompressorBlock extends AbstractCompressorBlock {

	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
		return createCompressorTicker(level, blockEntityType, (BlockEntityType<BlackHoleCompressorBlockEntity>) LostdepthsModBlockEntities.BLACK_HOLE_COMPRESSOR.get());
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("BlackHole Compressor");
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BlackHoleCompressorBlockEntity(pos, state);
	}
}
