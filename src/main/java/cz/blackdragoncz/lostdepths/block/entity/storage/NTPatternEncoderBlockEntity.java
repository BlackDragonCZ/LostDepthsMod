package cz.blackdragoncz.lostdepths.block.entity.storage;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.init.LostdepthsModMenus;
import cz.blackdragoncz.lostdepths.item.storage.BlankPatternItem;
import cz.blackdragoncz.lostdepths.item.storage.EncodedPatternItem;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetworkNode;
import cz.blackdragoncz.lostdepths.world.inventory.NTPatternEncoderMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/**
 * Pattern Encoder — GUI with crafting grid (3x3), input/output definition,
 * blank pattern slot, encode button, crafting/processing toggle.
 */
public class NTPatternEncoderBlockEntity extends BlockEntity implements MenuProvider, StorageNetworkNode {

	// Slot layout: 0-8 = crafting/input grid, 9 = blank pattern input, 10 = encoded pattern output
	public static final int GRID_SIZE = 9;
	public static final int BLANK_SLOT = 9;
	public static final int OUTPUT_SLOT = 10;
	public static final int TOTAL_SLOTS = 11;

	private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS) {
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			if (slot == BLANK_SLOT) return stack.getItem() instanceof BlankPatternItem;
			if (slot == OUTPUT_SLOT) return false; // output only
			return true; // grid slots accept anything (ghost items for definition)
		}

		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}
	};

	// false = crafting pattern, true = processing pattern
	private boolean processingMode = false;

	// For processing mode: output item definitions (up to 3 outputs)
	private final ItemStackHandler processingOutputs = new ItemStackHandler(3) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}
	};

	public NTPatternEncoderBlockEntity(BlockPos pos, BlockState state) {
		super(LostdepthsModBlockEntities.NT_PATTERN_ENCODER.get(), pos, state);
	}

	public ItemStackHandler getInventory() {
		return inventory;
	}

	public ItemStackHandler getProcessingOutputs() {
		return processingOutputs;
	}

	public boolean isProcessingMode() {
		return processingMode;
	}

	public void setProcessingMode(boolean processing) {
		this.processingMode = processing;
		setChanged();
	}

	public void dropContents(Level level, BlockPos pos) {
		for (int i = 0; i < TOTAL_SLOTS; i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (!stack.isEmpty()) {
				Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
			}
		}
	}

	@Override
	public int getEnergyPerTick() {
		return 1;
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.put("Inventory", inventory.serializeNBT());
		tag.putBoolean("ProcessingMode", processingMode);
		tag.put("ProcessingOutputs", processingOutputs.serializeNBT());
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("Inventory")) inventory.deserializeNBT(tag.getCompound("Inventory"));
		processingMode = tag.getBoolean("ProcessingMode");
		if (tag.contains("ProcessingOutputs")) processingOutputs.deserializeNBT(tag.getCompound("ProcessingOutputs"));
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("Pattern Encoder");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player) {
		return new NTPatternEncoderMenu(containerId, playerInv, this);
	}
}
