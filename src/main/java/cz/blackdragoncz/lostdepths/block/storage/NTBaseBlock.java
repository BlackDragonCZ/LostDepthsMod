package cz.blackdragoncz.lostdepths.block.storage;

import cz.blackdragoncz.lostdepths.storage.network.StorageNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

/**
 * Shared base for NT storage blocks (except controller which has its own handling).
 * Propagates neighbor changes to the network controller for rebuilds.
 */
public abstract class NTBaseBlock extends Block implements EntityBlock {

	public NTBaseBlock() {
		super(BlockBehaviour.Properties.of()
				.mapColor(MapColor.STONE)
				.strength(3.5f)
				.sound(SoundType.STONE)
				.requiresCorrectToolForDrops());
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean movedByPiston) {
		super.neighborChanged(state, level, pos, block, neighborPos, movedByPiston);
		StorageNetwork.notifyControllerNearby(level, pos);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		if (!state.is(newState.getBlock())) {
			StorageNetwork.notifyControllerNearby(level, pos);
		}
		super.onRemove(state, level, pos, newState, movedByPiston);
	}
}
