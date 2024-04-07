
package cz.blackdragoncz.lostdepths.block;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.util.NothingNullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.*;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

import cz.blackdragoncz.lostdepths.block.entity.GalacticCompressorBlockEntity;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class GalacticCompressorBlock extends AbstractCompressorBlock {

	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
		return createCompressorTicker(level, blockEntityType, (BlockEntityType<GalacticCompressorBlockEntity>)LostdepthsModBlockEntities.GALACTIC_COMPRESSOR.get());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new GalacticCompressorBlockEntity(pos, state);
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("Galactic Compressor");
	}
}
