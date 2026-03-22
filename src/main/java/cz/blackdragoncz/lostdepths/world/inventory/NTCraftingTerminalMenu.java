package cz.blackdragoncz.lostdepths.world.inventory;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTTerminalBlockEntity;
import cz.blackdragoncz.lostdepths.init.LostdepthsModMenus;
import cz.blackdragoncz.lostdepths.storage.data.CrystalInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NTCraftingTerminalMenu extends AbstractContainerMenu {
	private final BlockPos terminalPos;
	private final Player player;
	private final Level level;
	private NTTerminalBlockEntity terminal;

	private final TransientCraftingContainer craftingGrid;
	private final ResultContainer resultContainer = new ResultContainer();

	// Virtual grid — same as NTTerminalMenu
	private List<CrystalInventory.StoredItem> networkItems = new ArrayList<>();
	private String searchFilter = "";
	private int scrollOffset = 0;

	public static final int GRID_COLS = 9;
	public static final int GRID_ROWS = 4; // slightly smaller to fit crafting grid
	public static final int GRID_SLOTS = GRID_COLS * GRID_ROWS;

	// Slot indices: 0-8 = crafting grid, 9 = result, 10-36 = player inv, 37-45 = hotbar
	public static final int CRAFT_SLOT_START = 0;
	public static final int RESULT_SLOT = 9;
	public static final int PLAYER_INV_START = 10;

	public NTCraftingTerminalMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf) {
		super(LostdepthsModMenus.NT_CRAFTING_TERMINAL_MENU.get(), containerId);
		this.terminalPos = buf.readBlockPos();
		this.player = playerInv.player;
		this.level = playerInv.player.level();

		if (level.getBlockEntity(terminalPos) instanceof NTTerminalBlockEntity t) {
			this.terminal = t;
		}

		// Crafting grid: 3x3 at right side of GUI
		this.craftingGrid = new TransientCraftingContainer(this, 3, 3);
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				addSlot(new Slot(craftingGrid, col + row * 3, 188 + col * 18, 18 + row * 18));
			}
		}

		// Result slot
		addSlot(new ResultSlot(player, craftingGrid, resultContainer, 0, 244, 36) {
			@Override
			public void onTake(Player player, ItemStack stack) {
				super.onTake(player, stack);
				// After crafting, try to refill grid from network
				if (terminal != null && level instanceof net.minecraft.server.level.ServerLevel) {
					refillGridFromNetwork();
				}
			}
		});

		// Player inventory (3 rows)
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

	public NTTerminalBlockEntity getTerminal() {
		return terminal;
	}

	// --- Virtual grid (same as NTTerminalMenu) ---

	public List<CrystalInventory.StoredItem> getNetworkItems() {
		return networkItems;
	}

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

	public void refreshNetworkItems() {
		if (terminal != null) {
			networkItems = terminal.getAggregatedItems();
		}
	}

	public void handleGridClick(int gridIndex, int button) {
		if (terminal == null || !(player instanceof ServerPlayer)) return;

		List<CrystalInventory.StoredItem> filtered = getFilteredItems();
		int actualIndex = gridIndex + scrollOffset * GRID_COLS;
		if (actualIndex < 0 || actualIndex >= filtered.size()) return;

		CrystalInventory.StoredItem clicked = filtered.get(actualIndex);
		ItemStack carried = getCarried();

		if (carried.isEmpty()) {
			int extractCount = button == 0 ? Math.min(clicked.count, clicked.template.getMaxStackSize()) : 1;
			ItemStack extracted = terminal.extractFromNetwork(clicked.template, extractCount);
			if (!extracted.isEmpty()) setCarried(extracted);
		} else if (ItemStack.isSameItemSameTags(carried, clicked.template)) {
			int space = carried.getMaxStackSize() - carried.getCount();
			if (space > 0) {
				int extractCount = button == 0 ? Math.min(clicked.count, space) : 1;
				ItemStack extracted = terminal.extractFromNetwork(clicked.template, extractCount);
				if (!extracted.isEmpty()) carried.grow(extracted.getCount());
			}
		}
		refreshNetworkItems();
	}

	public void handleGridInsert(int button) {
		if (terminal == null || !(player instanceof ServerPlayer)) return;
		ItemStack carried = getCarried();
		if (carried.isEmpty()) return;

		if (button == 0) {
			int notInserted = terminal.insertIntoNetwork(carried.copy());
			if (notInserted == 0) {
				setCarried(ItemStack.EMPTY);
			} else {
				carried.setCount(notInserted);
			}
		} else {
			ItemStack single = carried.copy();
			single.setCount(1);
			int notInserted = terminal.insertIntoNetwork(single);
			if (notInserted == 0) {
				carried.shrink(1);
				if (carried.isEmpty()) setCarried(ItemStack.EMPTY);
			}
		}
		refreshNetworkItems();
	}

	// --- Crafting ---

	@Override
	public void slotsChanged(Container container) {
		if (container == craftingGrid) {
			updateCraftingResult();
		}
		super.slotsChanged(container);
	}

	private void updateCraftingResult() {
		if (level == null) return;
		Optional<CraftingRecipe> recipe = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingGrid, level);
		resultContainer.setItem(0, recipe.map(r -> r.assemble(craftingGrid, level.registryAccess())).orElse(ItemStack.EMPTY));
	}

	/**
	 * After crafting, attempt to refill the crafting grid from the network.
	 * Matches the pattern that was in the grid before crafting.
	 */
	private void refillGridFromNetwork() {
		if (terminal == null) return;
		for (int i = 0; i < 9; i++) {
			ItemStack gridItem = craftingGrid.getItem(i);
			if (gridItem.isEmpty()) {
				// Check if this slot had something before (it was consumed by crafting)
				// We can't know the old pattern here, so skip auto-refill for now
				// TODO: Store pattern before crafting for auto-refill
			}
		}
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		// Return crafting grid items to player or network
		if (!level.isClientSide() && terminal != null) {
			for (int i = 0; i < 9; i++) {
				ItemStack stack = craftingGrid.getItem(i);
				if (!stack.isEmpty()) {
					int remaining = terminal.insertIntoNetwork(stack);
					if (remaining > 0) {
						stack.setCount(remaining);
						player.drop(stack, false);
					}
				}
			}
		}
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

		if (slotIndex == RESULT_SLOT) {
			// Crafting result → player inventory
			if (!moveItemStackTo(slotStack, PLAYER_INV_START, PLAYER_INV_START + 36, true)) {
				return ItemStack.EMPTY;
			}
			slot.onQuickCraft(slotStack, result);
		} else if (slotIndex >= PLAYER_INV_START) {
			// Player inventory → try network first, then crafting grid
			if (terminal != null) {
				int remaining = terminal.insertIntoNetwork(slotStack.copy());
				if (remaining < slotStack.getCount()) {
					slotStack.setCount(remaining);
					if (remaining == 0) slot.set(ItemStack.EMPTY);
					else slot.setChanged();
					refreshNetworkItems();
					return result;
				}
			}
			// Fallback: try crafting grid
			if (!moveItemStackTo(slotStack, CRAFT_SLOT_START, CRAFT_SLOT_START + 9, false)) {
				return ItemStack.EMPTY;
			}
		} else if (slotIndex >= CRAFT_SLOT_START && slotIndex < CRAFT_SLOT_START + 9) {
			// Crafting grid → player inventory
			if (!moveItemStackTo(slotStack, PLAYER_INV_START, PLAYER_INV_START + 36, true)) {
				return ItemStack.EMPTY;
			}
		}

		if (slotStack.isEmpty()) slot.set(ItemStack.EMPTY);
		else slot.setChanged();

		return result;
	}

	@Override
	public void broadcastChanges() {
		super.broadcastChanges();
		refreshNetworkItems();
	}
}
