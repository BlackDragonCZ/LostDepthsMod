package cz.blackdragoncz.lostdepths.block.entity.storage;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.world.inventory.NTCraftingTerminalMenu;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class NTCraftingTerminalBlockEntity extends NTTerminalBlockEntity {

	public NTCraftingTerminalBlockEntity(BlockPos pos, BlockState state) {
		super(LostdepthsModBlockEntities.NT_CRAFTING_TERMINAL.get(), pos, state);
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("NuroTech Crafting Terminal");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeBlockPos(worldPosition);
		return new NTCraftingTerminalMenu(containerId, playerInv, buf);
	}
}
