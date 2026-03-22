package cz.blackdragoncz.lostdepths.block.entity.storage;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.item.storage.StorageCrystalItem;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetworkNode;
import cz.blackdragoncz.lostdepths.world.inventory.NTDriveMenu;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class NTDriveBlockEntity extends BlockEntity implements StorageNetworkNode, MenuProvider {
	private final int slotCount;
	private final ItemStackHandler crystalSlots;

	public NTDriveBlockEntity(BlockPos pos, BlockState state, int slots) {
		super(slots <= 3 ? LostdepthsModBlockEntities.NT_DRIVE.get() : LostdepthsModBlockEntities.NT_DRIVE_T2.get(), pos, state);
		this.slotCount = slots;
		this.crystalSlots = new ItemStackHandler(slots) {
			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				return stack.getItem() instanceof StorageCrystalItem;
			}

			@Override
			public int getSlotLimit(int slot) {
				return 1;
			}

			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
			}
		};
	}

	/** Factory for tier 2 registration */
	public static NTDriveBlockEntity createTier2(BlockPos pos, BlockState state) {
		return new NTDriveBlockEntity(pos, state, 7);
	}

	public int getSlotCount() {
		return slotCount;
	}

	public ItemStackHandler getCrystalSlots() {
		return crystalSlots;
	}

	public void dropContents(Level level, BlockPos pos) {
		for (int i = 0; i < crystalSlots.getSlots(); i++) {
			ItemStack stack = crystalSlots.getStackInSlot(i);
			if (!stack.isEmpty()) {
				Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
				crystalSlots.setStackInSlot(i, ItemStack.EMPTY);
			}
		}
	}

	@Override
	public int getEnergyPerTick() {
		return 2;
	}

	// --- MenuProvider ---

	@Override
	public Component getDisplayName() {
		return Component.literal("NT Drive (" + slotCount + " slots)");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeBlockPos(worldPosition);
		buf.writeInt(slotCount);
		return new NTDriveMenu(containerId, playerInv, buf);
	}

	// --- NBT ---

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("Crystals")) {
			crystalSlots.deserializeNBT(tag.getCompound("Crystals"));
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.put("Crystals", crystalSlots.serializeNBT());
	}
}
