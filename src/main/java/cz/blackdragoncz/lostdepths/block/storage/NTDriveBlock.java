package cz.blackdragoncz.lostdepths.block.storage;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTDriveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class NTDriveBlock extends NTBaseBlock {
	private final int slots;

	public NTDriveBlock(int slots) {
		this.slots = slots;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new NTDriveBlockEntity(pos, state, slots);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.isClientSide()) return InteractionResult.SUCCESS;
		if (player.isSpectator()) return InteractionResult.CONSUME;

		if (level.getBlockEntity(pos) instanceof NTDriveBlockEntity drive) {
			NetworkHooks.openScreen((ServerPlayer) player, drive, pos);
		}
		return InteractionResult.CONSUME;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		if (!state.is(newState.getBlock())) {
			if (level.getBlockEntity(pos) instanceof NTDriveBlockEntity drive) {
				drive.dropContents(level, pos);
			}
		}
		super.onRemove(state, level, pos, newState, movedByPiston);
	}
}
