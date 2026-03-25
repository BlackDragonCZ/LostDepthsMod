package cz.blackdragoncz.lostdepths.world.inventory;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTPatternEncoderBlockEntity;
import cz.blackdragoncz.lostdepths.init.LostdepthsModMenus;
import cz.blackdragoncz.lostdepths.item.storage.BlankPatternItem;
import cz.blackdragoncz.lostdepths.item.storage.EncodedPatternItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class NTPatternEncoderMenu extends AbstractContainerMenu {

	private final NTPatternEncoderBlockEntity encoder;
	private final BlockPos pos;

	// Client constructor
	public NTPatternEncoderMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf) {
		this(containerId, playerInv, (NTPatternEncoderBlockEntity) playerInv.player.level().getBlockEntity(buf.readBlockPos()));
	}

	// Server constructor
	public NTPatternEncoderMenu(int containerId, Inventory playerInv, NTPatternEncoderBlockEntity encoder) {
		super(LostdepthsModMenus.NT_PATTERN_ENCODER_MENU.get(), containerId);
		this.encoder = encoder;
		this.pos = encoder.getBlockPos();

		// Crafting/input grid: 3x3 (slots 0-8)
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				addSlot(new SlotItemHandler(encoder.getInventory(), col + row * 3, 8 + col * 18, 18 + row * 18));
			}
		}

		// Blank pattern input slot (slot 9)
		addSlot(new SlotItemHandler(encoder.getInventory(), NTPatternEncoderBlockEntity.BLANK_SLOT, 116, 36) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() instanceof BlankPatternItem;
			}
		});

		// Encoded pattern output slot (slot 10)
		addSlot(new SlotItemHandler(encoder.getInventory(), NTPatternEncoderBlockEntity.OUTPUT_SLOT, 152, 36) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}
		});

		// Processing output slots (slots 11-13, visible in processing mode)
		for (int i = 0; i < encoder.getProcessingOutputs().getSlots(); i++) {
			addSlot(new SlotItemHandler(encoder.getProcessingOutputs(), i, 116 + i * 18, 58));
		}

		// Player inventory
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 90 + row * 18));
			}
		}

		// Player hotbar
		for (int col = 0; col < 9; col++) {
			addSlot(new Slot(playerInv, col, 8 + col * 18, 148));
		}
	}

	public NTPatternEncoderBlockEntity getEncoder() {
		return encoder;
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

		int totalEncoderSlots = NTPatternEncoderBlockEntity.TOTAL_SLOTS + encoder.getProcessingOutputs().getSlots();
		int playerStart = totalEncoderSlots;
		int playerEnd = playerStart + 36;

		if (slotIndex < totalEncoderSlots) {
			if (!moveItemStackTo(slotStack, playerStart, playerEnd, true)) return ItemStack.EMPTY;
		} else {
			if (slotStack.getItem() instanceof BlankPatternItem) {
				if (!moveItemStackTo(slotStack, NTPatternEncoderBlockEntity.BLANK_SLOT, NTPatternEncoderBlockEntity.BLANK_SLOT + 1, false))
					return ItemStack.EMPTY;
			} else {
				if (!moveItemStackTo(slotStack, 0, 9, false)) return ItemStack.EMPTY;
			}
		}

		if (slotStack.isEmpty()) slot.set(ItemStack.EMPTY);
		else slot.setChanged();

		return result;
	}
}
