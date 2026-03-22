package cz.blackdragoncz.lostdepths.block.storage;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTBridgeAEBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class NTBridgeAEBlock extends NTBaseBlock {
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new NTBridgeAEBlockEntity(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.isClientSide()) return InteractionResult.SUCCESS;

		if (!NTBridgeAEBlockEntity.isAE2Loaded()) {
			player.displayClientMessage(Component.literal("§c[NuroTech] Applied Energistics not detected"), true);
			return InteractionResult.CONSUME;
		}

		// TODO: Open bridge config GUI when AE2 compat is implemented
		player.displayClientMessage(Component.literal("§b[NuroTech] AE2 Bridge — not yet implemented"), true);
		return InteractionResult.CONSUME;
	}
}
