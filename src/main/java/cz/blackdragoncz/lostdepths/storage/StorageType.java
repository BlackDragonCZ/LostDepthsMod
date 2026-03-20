package cz.blackdragoncz.lostdepths.storage;

public enum StorageType {
	CRYSTAL_1K(1024, 8),
	CRYSTAL_4K(4096, 16),
	CRYSTAL_16K(16384, 32),
	CRYSTAL_64K(65536, 64);

	private final int capacity;
	private final int typeLimit;

	StorageType(int capacity, int typeLimit) {
		this.capacity = capacity;
		this.typeLimit = typeLimit;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getTypeLimit() {
		return typeLimit;
	}

	public String getDisplayName() {
		return switch (this) {
			case CRYSTAL_1K -> "1K";
			case CRYSTAL_4K -> "4K";
			case CRYSTAL_16K -> "16K";
			case CRYSTAL_64K -> "64K";
		};
	}
}
