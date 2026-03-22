package cz.blackdragoncz.lostdepths.block.storage;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTBridgeRSBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class NTBridgeRSBlock extends NTBaseBlock {
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new NTBridgeRSBlockEntity(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.isClientSide()) return InteractionResult.SUCCESS;

		if (!NTBridgeRSBlockEntity.isRSLoaded()) {
			player.displayClientMessage(Component.literal("§c[NuroTech] Refined Storage not detected"), true);
			return InteractionResult.CONSUME;
		}

		// TODO: Open bridge config GUI when RS compat is implemented
		player.displayClientMessage(Component.literal("§b[NuroTech] RS Bridge — not yet implemented"), true);
		return InteractionResult.CONSUME;
	}
}
