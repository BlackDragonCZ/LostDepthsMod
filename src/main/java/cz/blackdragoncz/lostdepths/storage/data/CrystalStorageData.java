package cz.blackdragoncz.lostdepths.storage.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * World-level SavedData that maps crystal UUIDs to their stored contents.
 * TODO: Implement UUID→CrystalInventory mapping, save/load, cleanup of orphaned entries
 */
public class CrystalStorageData extends SavedData {

	public CrystalStorageData() {
	}

	public static CrystalStorageData load(CompoundTag tag) {
		CrystalStorageData data = new CrystalStorageData();
		// TODO: deserialize crystal inventories from tag
		return data;
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		// TODO: serialize crystal inventories to tag
		return tag;
	}
}
