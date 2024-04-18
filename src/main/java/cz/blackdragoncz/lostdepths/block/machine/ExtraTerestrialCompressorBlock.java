
package cz.blackdragoncz.lostdepths.block.machine;

import cz.blackdragoncz.lostdepths.block.entity.ExtraTerestrialCompressorBlockEntity;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExtraTerestrialCompressorBlock extends AbstractCompressorBlock {

	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
		return createCompressorTicker(level, blockEntityType, (BlockEntityType<ExtraTerestrialCompressorBlockEntity>) LostdepthsModBlockEntities.EXTRA_TERESTRIAL_COMPRESSOR.get());
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("Extra-Terrestrial Compressor");
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
		return new ExtraTerestrialCompressorBlockEntity(pPos, pState);
	}
}
