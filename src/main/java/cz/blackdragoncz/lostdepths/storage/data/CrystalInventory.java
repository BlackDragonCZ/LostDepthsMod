package cz.blackdragoncz.lostdepths.storage.data;

import cz.blackdragoncz.lostdepths.storage.StorageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the contents of a single storage crystal.
 * Stores items as type→count pairs, enforcing capacity and type limits.
 */
public class CrystalInventory {
	private final StorageType type;
	private final List<StoredItem> items = new ArrayList<>();
	private int totalCount = 0;

	public CrystalInventory(StorageType type) {
		this.type = type;
	}

	public StorageType getType() {
		return type;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getTypeCount() {
		return items.size();
	}

	public int getRemainingCapacity() {
		return type.getCapacity() - totalCount;
	}

	public int getRemainingTypes() {
		return type.getTypeLimit() - items.size();
	}

	public List<StoredItem> getItems() {
		return items;
	}

	/**
	 * Insert items into this crystal.
	 * @return the number of items actually inserted
	 */
	public int insert(ItemStack stack, int count) {
		if (stack.isEmpty() || count <= 0) return 0;

		int canInsert = Math.min(count, getRemainingCapacity());
		if (canInsert <= 0) return 0;

		// Find existing entry for this item type
		for (StoredItem stored : items) {
			if (ItemStack.isSameItemSameTags(stored.template, stack)) {
				stored.count += canInsert;
				totalCount += canInsert;
				return canInsert;
			}
		}

		// New type — check type limit
		if (items.size() >= type.getTypeLimit()) return 0;

		ItemStack template = stack.copy();
		template.setCount(1);
		items.add(new StoredItem(template, canInsert));
		totalCount += canInsert;
		return canInsert;
	}

	/**
	 * Extract items from this crystal.
	 * @return the actual ItemStack extracted (may have less than requested count)
	 */
	public ItemStack extract(ItemStack match, int count) {
		if (match.isEmpty() || count <= 0) return ItemStack.EMPTY;

		for (int i = 0; i < items.size(); i++) {
			StoredItem stored = items.get(i);
			if (ItemStack.isSameItemSameTags(stored.template, match)) {
				int toExtract = Math.min(count, stored.count);
				stored.count -= toExtract;
				totalCount -= toExtract;

				if (stored.count <= 0) {
					items.remove(i);
				}

				ItemStack result = stored.template.copy();
				result.setCount(toExtract);
				return result;
			}
		}

		return ItemStack.EMPTY;
	}

	/**
	 * Get the count of a specific item type stored in this crystal.
	 */
	public int getCount(ItemStack match) {
		for (StoredItem stored : items) {
			if (ItemStack.isSameItemSameTags(stored.template, match)) {
				return stored.count;
			}
		}
		return 0;
	}

	public CompoundTag save() {
		CompoundTag tag = new CompoundTag();
		tag.putString("type", type.name());

		ListTag itemList = new ListTag();
		for (StoredItem stored : items) {
			CompoundTag entry = new CompoundTag();
			entry.put("item", stored.template.save(new CompoundTag()));
			entry.putInt("count", stored.count);
			itemList.add(entry);
		}
		tag.put("items", itemList);

		return tag;
	}

	public static CrystalInventory load(CompoundTag tag) {
		StorageType storageType;
		try {
			storageType = StorageType.valueOf(tag.getString("type"));
		} catch (IllegalArgumentException e) {
			storageType = StorageType.CRYSTAL_1K;
		}

		CrystalInventory inv = new CrystalInventory(storageType);

		ListTag itemList = tag.getList("items", Tag.TAG_COMPOUND);
		for (int i = 0; i < itemList.size(); i++) {
			CompoundTag entry = itemList.getCompound(i);
			ItemStack template = ItemStack.of(entry.getCompound("item"));
			int count = entry.getInt("count");

			if (!template.isEmpty() && count > 0) {
				template.setCount(1);
				inv.items.add(new StoredItem(template, count));
				inv.totalCount += count;
			}
		}

		return inv;
	}

	public static class StoredItem {
		public final ItemStack template;
		public int count;

		public StoredItem(ItemStack template, int count) {
			this.template = template;
			this.count = count;
		}
	}
}
