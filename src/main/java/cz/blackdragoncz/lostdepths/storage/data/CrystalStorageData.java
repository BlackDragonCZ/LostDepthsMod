package cz.blackdragoncz.lostdepths.storage.data;

import cz.blackdragoncz.lostdepths.storage.StorageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

/**
 * World-level SavedData that maps crystal UUIDs to their stored contents.
 * Saved as "nurotech_crystals" in the world's data directory.
 */
public class CrystalStorageData extends SavedData {
	private static final String DATA_NAME = "nurotech_crystals";
	private final Map<UUID, CrystalInventory> crystals = new HashMap<>();

	public CrystalStorageData() {
	}

	/**
	 * Get the CrystalStorageData instance for the given server level.
	 * Uses the overworld's data storage (shared across dimensions).
	 */
	public static CrystalStorageData get(ServerLevel level) {
		ServerLevel overworld = level.getServer().overworld();
		return overworld.getDataStorage().computeIfAbsent(CrystalStorageData::load, CrystalStorageData::new, DATA_NAME);
	}

	/**
	 * Get an existing crystal inventory by UUID.
	 */
	public CrystalInventory getCrystal(UUID id) {
		return crystals.get(id);
	}

	/**
	 * Create a new crystal inventory with a fresh UUID.
	 */
	public UUID createCrystal(StorageType type) {
		UUID id = UUID.randomUUID();
		crystals.put(id, new CrystalInventory(type));
		setDirty();
		return id;
	}

	/**
	 * Get or create a crystal inventory for the given UUID.
	 * If the UUID doesn't exist, creates a new inventory with the given type.
	 */
	public CrystalInventory getOrCreate(UUID id, StorageType type) {
		CrystalInventory inv = crystals.get(id);
		if (inv == null) {
			inv = new CrystalInventory(type);
			crystals.put(id, inv);
			setDirty();
		}
		return inv;
	}

	/**
	 * Remove a crystal entry. Returns true if it existed.
	 */
	public boolean removeCrystal(UUID id) {
		boolean existed = crystals.remove(id) != null;
		if (existed) setDirty();
		return existed;
	}

	/**
	 * Get all crystal UUIDs.
	 */
	public Set<UUID> getAllCrystalIds() {
		return Collections.unmodifiableSet(crystals.keySet());
	}

	/**
	 * Get the total number of crystals tracked.
	 */
	public int getCrystalCount() {
		return crystals.size();
	}

	/**
	 * Get the total number of items stored across all crystals.
	 */
	public int getTotalStoredItems() {
		int total = 0;
		for (CrystalInventory inv : crystals.values()) {
			total += inv.getTotalCount();
		}
		return total;
	}

	/**
	 * Mark data as modified so it gets saved on next world save.
	 * Call this after any modification to crystal contents.
	 */
	public void markModified() {
		setDirty();
	}

	// --- Serialization ---

	public static CrystalStorageData load(CompoundTag tag) {
		CrystalStorageData data = new CrystalStorageData();

		CompoundTag crystalsTag = tag.getCompound("crystals");
		for (String key : crystalsTag.getAllKeys()) {
			try {
				UUID id = UUID.fromString(key);
				CrystalInventory inv = CrystalInventory.load(crystalsTag.getCompound(key));
				data.crystals.put(id, inv);
			} catch (IllegalArgumentException ignored) {
				// Skip invalid UUID keys
			}
		}

		return data;
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		CompoundTag crystalsTag = new CompoundTag();
		for (Map.Entry<UUID, CrystalInventory> entry : crystals.entrySet()) {
			crystalsTag.put(entry.getKey().toString(), entry.getValue().save());
		}
		tag.put("crystals", crystalsTag);

		return tag;
	}
}
