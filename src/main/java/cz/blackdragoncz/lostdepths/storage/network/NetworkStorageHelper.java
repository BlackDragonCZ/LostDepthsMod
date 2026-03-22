package cz.blackdragoncz.lostdepths.storage.network;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTDriveBlockEntity;
import cz.blackdragoncz.lostdepths.item.storage.StorageCrystalItem;
import cz.blackdragoncz.lostdepths.storage.data.CrystalInventory;
import cz.blackdragoncz.lostdepths.storage.data.CrystalStorageData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;

/**
 * Shared utility for inserting/extracting items across a NuroTech network's crystals.
 * Used by terminals, import/export buses, and autocrafting.
 */
public final class NetworkStorageHelper {

	private NetworkStorageHelper() {}

	/**
	 * Collects all stored items across all crystals in all drives on the network.
	 * Same item types are merged.
	 */
	public static List<CrystalInventory.StoredItem> getAggregatedItems(StorageNetwork network, ServerLevel level) {
		CrystalStorageData data = CrystalStorageData.get(level);
		Map<String, CrystalInventory.StoredItem> merged = new LinkedHashMap<>();

		for (BlockPos pos : network.getComponents()) {
			BlockEntity be = level.getBlockEntity(pos);
			if (!(be instanceof NTDriveBlockEntity drive)) continue;

			for (int i = 0; i < drive.getSlotCount(); i++) {
				ItemStack crystalStack = drive.getCrystalSlots().getStackInSlot(i);
				CrystalInventory inv = StorageCrystalItem.getInventory(crystalStack, level);
				if (inv == null) continue;

				for (CrystalInventory.StoredItem stored : inv.getItems()) {
					String key = stored.template.save(new CompoundTag()).toString();
					CrystalInventory.StoredItem existing = merged.get(key);
					if (existing != null) {
						existing.count += stored.count;
					} else {
						merged.put(key, new CrystalInventory.StoredItem(stored.template.copy(), stored.count));
					}
				}
			}
		}

		return new ArrayList<>(merged.values());
	}

	/**
	 * Insert items into the network's crystals.
	 * @return number of items NOT inserted (remainder).
	 */
	public static int insert(StorageNetwork network, ServerLevel level, ItemStack stack, int count) {
		CrystalStorageData data = CrystalStorageData.get(level);
		int remaining = count;

		for (BlockPos pos : network.getComponents()) {
			if (remaining <= 0) break;
			BlockEntity be = level.getBlockEntity(pos);
			if (!(be instanceof NTDriveBlockEntity drive)) continue;

			for (int i = 0; i < drive.getSlotCount(); i++) {
				if (remaining <= 0) break;
				ItemStack crystalStack = drive.getCrystalSlots().getStackInSlot(i);
				if (crystalStack.isEmpty()) continue;

				StorageCrystalItem.getOrAssignId(crystalStack, level);
				CrystalInventory inv = StorageCrystalItem.getInventory(crystalStack, level);
				if (inv == null) continue;

				int inserted = inv.insert(stack, remaining);
				if (inserted > 0) {
					remaining -= inserted;
					data.markModified();
				}
			}
		}

		return remaining;
	}

	/**
	 * Extract items from the network's crystals.
	 * @return the actual ItemStack extracted.
	 */
	public static ItemStack extract(StorageNetwork network, ServerLevel level, ItemStack match, int count) {
		CrystalStorageData data = CrystalStorageData.get(level);
		int remaining = count;
		ItemStack result = ItemStack.EMPTY;

		for (BlockPos pos : network.getComponents()) {
			if (remaining <= 0) break;
			BlockEntity be = level.getBlockEntity(pos);
			if (!(be instanceof NTDriveBlockEntity drive)) continue;

			for (int i = 0; i < drive.getSlotCount(); i++) {
				if (remaining <= 0) break;
				ItemStack crystalStack = drive.getCrystalSlots().getStackInSlot(i);
				CrystalInventory inv = StorageCrystalItem.getInventory(crystalStack, level);
				if (inv == null) continue;

				ItemStack extracted = inv.extract(match, remaining);
				if (!extracted.isEmpty()) {
					if (result.isEmpty()) {
						result = extracted;
					} else {
						result.grow(extracted.getCount());
					}
					remaining -= extracted.getCount();
					data.markModified();
				}
			}
		}

		return result;
	}
}
