package cz.blackdragoncz.lostdepths.block.storage;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTExternalStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NTExternalStorageBlock extends NTBaseBlock {
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new NTExternalStorageBlockEntity(pos, state);
	}
}
