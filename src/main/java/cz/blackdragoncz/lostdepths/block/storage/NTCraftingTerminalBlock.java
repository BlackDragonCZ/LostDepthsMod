package cz.blackdragoncz.lostdepths.block.storage;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTCraftingTerminalBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.storage.NTTerminalBlockEntity;
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

public class NTCraftingTerminalBlock extends NTBaseBlock {
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new NTCraftingTerminalBlockEntity(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.isClientSide()) return InteractionResult.SUCCESS;
		if (player.isSpectator()) return InteractionResult.CONSUME;

		if (level.getBlockEntity(pos) instanceof NTTerminalBlockEntity terminal) {
			if (!terminal.isNetworkOnline()) {
				player.displayClientMessage(net.minecraft.network.chat.Component.literal("§c[NuroTech] Network offline"), true);
				return InteractionResult.CONSUME;
			}
			NetworkHooks.openScreen((ServerPlayer) player, terminal, pos);
		}
		return InteractionResult.CONSUME;
	}
}
