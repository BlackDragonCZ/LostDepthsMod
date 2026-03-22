package cz.blackdragoncz.lostdepths.block.storage;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTSecurityTerminalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class NTSecurityTerminalBlock extends NTBaseBlock {
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new NTSecurityTerminalBlockEntity(pos, state);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);
		if (!level.isClientSide() && placer instanceof Player player) {
			if (level.getBlockEntity(pos) instanceof NTSecurityTerminalBlockEntity security) {
				security.setOwner(player);
			}
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.isClientSide()) return InteractionResult.SUCCESS;
		if (player.isSpectator()) return InteractionResult.CONSUME;

		if (level.getBlockEntity(pos) instanceof NTSecurityTerminalBlockEntity security) {
			if (security.isOwner(player) || player.hasPermissions(2)) {
				// TODO: Open security config GUI
				String ownerInfo = security.getOwnerName().isEmpty() ? "none" : security.getOwnerName();
				int permCount = security.getAllPermissions().size();
				player.displayClientMessage(Component.literal(
						"§b[NuroTech Security] Owner: §f" + ownerInfo +
						"§b | Rules: §f" + permCount +
						"§b | Defaults: I:" + (security.getDefaultInsert() ? "§a✓" : "§c✗") +
						"§b E:" + (security.getDefaultExtract() ? "§a✓" : "§c✗") +
						"§b C:" + (security.getDefaultCraft() ? "§a✓" : "§c✗")), false);
			} else {
				player.displayClientMessage(Component.literal("§c[NuroTech] Access denied — you are not the network owner"), true);
			}
		}
		return InteractionResult.CONSUME;
	}
}
