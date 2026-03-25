package cz.blackdragoncz.lostdepths.block.entity.storage;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetwork;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetworkNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Wraps adjacent inventories (chests, barrels, modded containers) as network storage.
 * Scans all 6 sides for IItemHandler capabilities, skipping NT network components.
 * Items in connected inventories appear in the terminal and can be inserted/extracted.
 */
public class NTExternalStorageBlockEntity extends BlockEntity implements StorageNetworkNode {

	private StorageNetwork network;

	public NTExternalStorageBlockEntity(BlockPos pos, BlockState state) {
		super(LostdepthsModBlockEntities.NT_EXTERNAL_STORAGE.get(), pos, state);
	}

	@Override
	public void onJoinNetwork(StorageNetwork network) {
		this.network = network;
	}

	@Override
	public void onLeaveNetwork() {
		this.network = null;
	}

	@Override
	public int getEnergyPerTick() {
		return 2;
	}

	/**
	 * Get all adjacent IItemHandlers (non-NT blocks only).
	 */
	public List<IItemHandler> getAdjacentHandlers() {
		List<IItemHandler> handlers = new ArrayList<>();
		if (level == null) return handlers;

		for (Direction dir : Direction.values()) {
			BlockPos adjacent = worldPosition.relative(dir);
			BlockEntity adjBe = level.getBlockEntity(adjacent);
			if (adjBe == null) continue;
			if (adjBe instanceof StorageNetworkNode) continue;

			adjBe.getCapability(ForgeCapabilities.ITEM_HANDLER, dir.getOpposite()).ifPresent(handlers::add);
		}
		return handlers;
	}

	/**
	 * Count connected external inventories.
	 */
	public int getConnectedCount() {
		return getAdjacentHandlers().size();
	}

	/**
	 * Get all items from all connected inventories (for terminal display).
	 */
	public List<ExternalStoredItem> getExternalItems() {
		List<ExternalStoredItem> items = new ArrayList<>();
		for (IItemHandler handler : getAdjacentHandlers()) {
			for (int slot = 0; slot < handler.getSlots(); slot++) {
				ItemStack stack = handler.getStackInSlot(slot);
				if (!stack.isEmpty()) {
					items.add(new ExternalStoredItem(stack.copy(), handler, slot));
				}
			}
		}
		return items;
	}

	/**
	 * Insert an item into connected external inventories.
	 * @return number of items NOT inserted.
	 */
	public int insertExternal(ItemStack stack, int count) {
		ItemStack toInsert = stack.copy();
		toInsert.setCount(count);

		for (IItemHandler handler : getAdjacentHandlers()) {
			toInsert = ItemHandlerHelper.insertItemStacked(handler, toInsert, false);
			if (toInsert.isEmpty()) return 0;
		}
		return toInsert.getCount();
	}

	/**
	 * Extract an item from connected external inventories.
	 * @return the extracted ItemStack.
	 */
	public ItemStack extractExternal(ItemStack match, int count) {
		int remaining = count;
		ItemStack result = ItemStack.EMPTY;

		for (IItemHandler handler : getAdjacentHandlers()) {
			for (int slot = 0; slot < handler.getSlots(); slot++) {
				if (remaining <= 0) break;
				ItemStack inSlot = handler.getStackInSlot(slot);
				if (inSlot.isEmpty() || !ItemStack.isSameItemSameTags(inSlot, match)) continue;

				ItemStack extracted = handler.extractItem(slot, remaining, false);
				if (!extracted.isEmpty()) {
					if (result.isEmpty()) {
						result = extracted;
					} else {
						result.grow(extracted.getCount());
					}
					remaining -= extracted.getCount();
				}
			}
		}
		return result;
	}

	/**
	 * Holds a reference to an item in an external handler slot.
	 */
	public record ExternalStoredItem(ItemStack stack, IItemHandler handler, int slot) {}
}
