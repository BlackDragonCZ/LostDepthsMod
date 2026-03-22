package cz.blackdragoncz.lostdepths.world.inventory;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTTerminalBlockEntity;
import cz.blackdragoncz.lostdepths.init.LostdepthsModMenus;
import cz.blackdragoncz.lostdepths.storage.data.CrystalInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class NTTerminalMenu extends AbstractContainerMenu {
	private final BlockPos terminalPos;
	private final Player player;
	private NTTerminalBlockEntity terminal;

	// Virtual item list — synced from server to client
	private List<CrystalInventory.StoredItem> networkItems = new ArrayList<>();
	private String searchFilter = "";
	private int scrollOffset = 0;

	// Grid layout: 9 columns, 5 rows visible
	public static final int GRID_COLS = 9;
	public static final int GRID_ROWS = 5;
	public static final int GRID_SLOTS = GRID_COLS * GRID_ROWS;

	public NTTerminalMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf) {
		super(LostdepthsModMenus.NT_TERMINAL_MENU.get(), containerId);
		this.terminalPos = buf.readBlockPos();
		this.player = playerInv.player;

		if (player.level().getBlockEntity(terminalPos) instanceof NTTerminalBlockEntity t) {
			this.terminal = t;
		}

		// Player inventory (3 rows) — positioned below the virtual grid
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 140 + row * 18));
			}
		}

		// Player hotbar
		for (int col = 0; col < 9; col++) {
			addSlot(new Slot(playerInv, col, 8 + col * 18, 198));
		}
	}

	public BlockPos getTerminalPos() {
		return terminalPos;
	}

	public List<CrystalInventory.StoredItem> getNetworkItems() {
		return networkItems;
	}

	/**
	 * Get the filtered list based on current search text.
	 */
	public List<CrystalInventory.StoredItem> getFilteredItems() {
		if (searchFilter.isEmpty()) return networkItems;

		List<CrystalInventory.StoredItem> filtered = new ArrayList<>();
		String lower = searchFilter.toLowerCase();
		for (CrystalInventory.StoredItem item : networkItems) {
			if (item.template.getHoverName().getString().toLowerCase().contains(lower)) {
				filtered.add(item);
			}
		}
		return filtered;
	}

	public void setSearchFilter(String filter) {
		this.searchFilter = filter;
		this.scrollOffset = 0;
	}

	public int getScrollOffset() {
		return scrollOffset;
	}

	public void setScrollOffset(int offset) {
		List<CrystalInventory.StoredItem> filtered = getFilteredItems();
		int maxRows = Math.max(0, (filtered.size() + GRID_COLS - 1) / GRID_COLS - GRID_ROWS);
		this.scrollOffset = Math.max(0, Math.min(offset, maxRows));
	}

	/**
	 * Refreshes the virtual item list from the network.
	 * Called on menu open and periodically.
	 */
	public void refreshNetworkItems() {
		if (terminal != null) {
			networkItems = terminal.getAggregatedItems();
		}
	}

	/**
	 * Handle clicking on a virtual grid slot (not a real Slot).
	 * @param gridIndex index in the filtered+scrolled view
	 * @param button 0=left, 1=right
	 */
	public void handleGridClick(int gridIndex, int button) {
		if (terminal == null || !(player instanceof ServerPlayer)) return;

		List<CrystalInventory.StoredItem> filtered = getFilteredItems();
		int actualIndex = gridIndex + scrollOffset * GRID_COLS;
		if (actualIndex < 0 || actualIndex >= filtered.size()) return;

		CrystalInventory.StoredItem clicked = filtered.get(actualIndex);
		ItemStack carried = getCarried();

		if (carried.isEmpty()) {
			// Extract from network
			int extractCount = button == 0 ? Math.min(clicked.count, clicked.template.getMaxStackSize()) : 1;
			ItemStack extracted = terminal.extractFromNetwork(clicked.template, extractCount);
			if (!extracted.isEmpty()) {
				setCarried(extracted);
			}
		} else {
			// If clicking on same type, extract more into carried stack
			if (ItemStack.isSameItemSameTags(carried, clicked.template)) {
				int space = carried.getMaxStackSize() - carried.getCount();
				if (space > 0) {
					int extractCount = button == 0 ? Math.min(clicked.count, space) : 1;
					ItemStack extracted = terminal.extractFromNetwork(clicked.template, extractCount);
					if (!extracted.isEmpty()) {
						carried.grow(extracted.getCount());
					}
				}
			}
		}

		refreshNetworkItems();
	}

	/**
	 * Handle clicking on the grid background (empty area) with a carried item.
	 * Inserts carried items into the network.
	 */
	public void handleGridInsert(int button) {
		if (terminal == null || !(player instanceof ServerPlayer)) return;

		ItemStack carried = getCarried();
		if (carried.isEmpty()) return;

		int toInsert = button == 0 ? carried.getCount() : 1;
		int notInserted = terminal.insertIntoNetwork(carried.copy());

		if (button == 0) {
			if (notInserted == 0) {
				setCarried(ItemStack.EMPTY);
			} else {
				carried.setCount(notInserted);
			}
		} else {
			// Right click: insert 1
			int wasInserted = 1 - terminal.insertIntoNetwork(new ItemStack(carried.getItem(), 1));
			if (wasInserted > 0) {
				carried.shrink(1);
				if (carried.isEmpty()) setCarried(ItemStack.EMPTY);
			}
		}

		refreshNetworkItems();
	}

	@Override
	public boolean stillValid(Player player) {
		return player.distanceToSqr(terminalPos.getX() + 0.5, terminalPos.getY() + 0.5, terminalPos.getZ() + 0.5) <= 64.0;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex) {
		Slot slot = slots.get(slotIndex);
		if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;

		ItemStack slotStack = slot.getItem();
		ItemStack result = slotStack.copy();

		if (terminal != null) {
			// Player inventory → network
			int remaining = terminal.insertIntoNetwork(slotStack.copy());
			if (remaining < slotStack.getCount()) {
				slotStack.setCount(remaining);
				if (remaining == 0) {
					slot.set(ItemStack.EMPTY);
				} else {
					slot.setChanged();
				}
				refreshNetworkItems();
				return result;
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public void broadcastChanges() {
		super.broadcastChanges();
		// Refresh network items periodically (every call from server tick)
		refreshNetworkItems();
	}
}
