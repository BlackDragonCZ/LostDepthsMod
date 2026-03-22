package cz.blackdragoncz.lostdepths.world.inventory;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTDriveBlockEntity;
import cz.blackdragoncz.lostdepths.init.LostdepthsModMenus;
import cz.blackdragoncz.lostdepths.item.storage.StorageCrystalItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class NTDriveMenu extends AbstractContainerMenu {
	private final BlockPos blockPos;
	private final int slotCount;
	private final ItemStackHandler crystalSlots;

	public NTDriveMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf) {
		super(LostdepthsModMenus.NT_DRIVE_MENU.get(), containerId);
		this.blockPos = buf.readBlockPos();
		this.slotCount = buf.readInt();

		Level level = playerInv.player.level();
		NTDriveBlockEntity drive = null;
		if (level.getBlockEntity(blockPos) instanceof NTDriveBlockEntity d) {
			drive = d;
		}

		this.crystalSlots = drive != null ? drive.getCrystalSlots() : new ItemStackHandler(slotCount);

		// Crystal slots — centered horizontally
		int startX = (176 - slotCount * 18) / 2;
		for (int i = 0; i < slotCount; i++) {
			addSlot(new SlotItemHandler(crystalSlots, i, startX + 1 + i * 18, 35) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					return stack.getItem() instanceof StorageCrystalItem;
				}

				@Override
				public int getMaxStackSize() {
					return 1;
				}
			});
		}

		// Player inventory (3 rows)
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
			}
		}

		// Player hotbar
		for (int col = 0; col < 9; col++) {
			addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
		}
	}

	public int getSlotCount() {
		return slotCount;
	}

	public BlockPos getBlockPos() {
		return blockPos;
	}

	@Override
	public boolean stillValid(Player player) {
		return player.distanceToSqr(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5) <= 64.0;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex) {
		Slot slot = slots.get(slotIndex);
		if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;

		ItemStack slotStack = slot.getItem();
		ItemStack result = slotStack.copy();

		if (slotIndex < slotCount) {
			// Crystal slot → player inventory
			if (!moveItemStackTo(slotStack, slotCount, slots.size(), true)) {
				return ItemStack.EMPTY;
			}
		} else {
			// Player inventory → crystal slots (only if StorageCrystalItem)
			if (slotStack.getItem() instanceof StorageCrystalItem) {
				if (!moveItemStackTo(slotStack, 0, slotCount, false)) {
					return ItemStack.EMPTY;
				}
			} else {
				return ItemStack.EMPTY;
			}
		}

		if (slotStack.isEmpty()) {
			slot.set(ItemStack.EMPTY);
		} else {
			slot.setChanged();
		}

		return result;
	}
}
