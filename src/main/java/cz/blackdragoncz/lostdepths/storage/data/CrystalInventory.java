package cz.blackdragoncz.lostdepths.storage.data;

import cz.blackdragoncz.lostdepths.storage.StorageType;

/**
 * Represents the contents of a single storage crystal.
 * Tracks stored item types and counts, respecting capacity and type limits.
 * TODO: Implement item insertion/extraction, type limit enforcement, NBT serialization
 */
public class CrystalInventory {
	private final StorageType type;

	public CrystalInventory(StorageType type) {
		this.type = type;
	}

	public StorageType getType() {
		return type;
	}
}
