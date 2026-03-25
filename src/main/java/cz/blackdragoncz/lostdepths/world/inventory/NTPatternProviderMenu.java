package cz.blackdragoncz.lostdepths.world.inventory;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTPatternProviderBlockEntity;
import cz.blackdragoncz.lostdepths.init.LostdepthsModMenus;
import cz.blackdragoncz.lostdepths.item.storage.EncodedPatternItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class NTPatternProviderMenu extends AbstractContainerMenu {

	private final NTPatternProviderBlockEntity provider;
	private final BlockPos pos;

	// Client constructor
	public NTPatternProviderMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf) {
		this(containerId, playerInv, (NTPatternProviderBlockEntity) playerInv.player.level().getBlockEntity(buf.readBlockPos()));
	}

	// Server constructor
	public NTPatternProviderMenu(int containerId, Inventory playerInv, NTPatternProviderBlockEntity provider) {
		super(LostdepthsModMenus.NT_PATTERN_PROVIDER_MENU.get(), containerId);
		this.provider = provider;
		this.pos = provider.getBlockPos();

		int slotCount = provider.getSlotCount();
		int cols = 9;
		int rows = (slotCount + cols - 1) / cols;

		// Pattern slots
		for (int i = 0; i < slotCount; i++) {
			int row = i / cols;
			int col = i % cols;
			addSlot(new SlotItemHandler(provider.getPatternSlots(), i, 8 + col * 18, 18 + row * 18) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					return stack.getItem() instanceof EncodedPatternItem;
				}
			});
		}

		// Result buffer (row below patterns)
		int resultY = 18 + rows * 18 + 6;
		for (int i = 0; i < provider.getResultBuffer().getSlots(); i++) {
			addSlot(new SlotItemHandler(provider.getResultBuffer(), i, 8 + i * 18, resultY) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					return false; // output only
				}
			});
		}

		// Player inventory
		int playerY = resultY + 24;
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, playerY + row * 18));
			}
		}

		// Player hotbar
		for (int col = 0; col < 9; col++) {
			addSlot(new Slot(playerInv, col, 8 + col * 18, playerY + 58));
		}
	}

	public NTPatternProviderBlockEntity getProvider() {
		return provider;
	}

	@Override
	public boolean stillValid(Player player) {
		return player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex) {
		Slot slot = slots.get(slotIndex);
		if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;

		ItemStack slotStack = slot.getItem();
		ItemStack result = slotStack.copy();

		int patternSlots = provider.getSlotCount();
		int resultSlots = provider.getResultBuffer().getSlots();
		int totalProviderSlots = patternSlots + resultSlots;
		int playerStart = totalProviderSlots;
		int playerEnd = playerStart + 36;

		if (slotIndex < totalProviderSlots) {
			// Provider → player
			if (!moveItemStackTo(slotStack, playerStart, playerEnd, true)) return ItemStack.EMPTY;
		} else {
			// Player → pattern slots (if encoded pattern)
			if (slotStack.getItem() instanceof EncodedPatternItem) {
				if (!moveItemStackTo(slotStack, 0, patternSlots, false)) return ItemStack.EMPTY;
			} else {
				return ItemStack.EMPTY;
			}
		}

		if (slotStack.isEmpty()) slot.set(ItemStack.EMPTY);
		else slot.setChanged();

		return result;
	}
}
