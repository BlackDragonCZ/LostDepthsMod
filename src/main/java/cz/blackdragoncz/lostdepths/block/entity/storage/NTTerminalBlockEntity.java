package cz.blackdragoncz.lostdepths.block.entity.storage;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.storage.data.CrystalInventory;
import cz.blackdragoncz.lostdepths.storage.network.NetworkStorageHelper;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetwork;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetworkNode;
import cz.blackdragoncz.lostdepths.world.inventory.NTTerminalMenu;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.*;

public class NTTerminalBlockEntity extends BlockEntity implements StorageNetworkNode, MenuProvider {

	private StorageNetwork network;

	public NTTerminalBlockEntity(BlockPos pos, BlockState state) {
		super(LostdepthsModBlockEntities.NT_TERMINAL.get(), pos, state);
	}

	protected NTTerminalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void onJoinNetwork(StorageNetwork network) {
		this.network = network;
	}

	@Override
	public void onLeaveNetwork() {
		this.network = null;
	}

	public boolean isNetworkOnline() {
		return network != null && network.isActive();
	}

	public StorageNetwork getNetwork() {
		return network;
	}

	public List<CrystalInventory.StoredItem> getAggregatedItems() {
		if (network == null || !(level instanceof ServerLevel serverLevel)) return Collections.emptyList();
		return NetworkStorageHelper.getAggregatedItems(network, serverLevel);
	}

	public int insertIntoNetwork(ItemStack stack) {
		if (network == null || !(level instanceof ServerLevel serverLevel)) return stack.getCount();
		return NetworkStorageHelper.insert(network, serverLevel, stack, stack.getCount());
	}

	public ItemStack extractFromNetwork(ItemStack match, int count) {
		if (network == null || !(level instanceof ServerLevel serverLevel)) return ItemStack.EMPTY;
		return NetworkStorageHelper.extract(network, serverLevel, match, count);
	}

	// --- MenuProvider ---

	@Override
	public Component getDisplayName() {
		return Component.literal("NuroTech Terminal");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeBlockPos(worldPosition);
		return new NTTerminalMenu(containerId, playerInv, buf);
	}
}
