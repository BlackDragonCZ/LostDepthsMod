package cz.blackdragoncz.lostdepths.item.storage;

import cz.blackdragoncz.lostdepths.storage.StorageType;
import cz.blackdragoncz.lostdepths.storage.data.CrystalInventory;
import cz.blackdragoncz.lostdepths.storage.data.CrystalStorageData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class StorageCrystalItem extends Item {
	private final StorageType storageType;

	public StorageCrystalItem(StorageType storageType) {
		super(new Properties().stacksTo(1).fireResistant());
		this.storageType = storageType;
	}

	public StorageType getStorageType() {
		return storageType;
	}

	/**
	 * Get the crystal's UUID from the ItemStack NBT.
	 * Returns null if no UUID has been assigned yet.
	 */
	public static UUID getCrystalId(ItemStack stack) {
		if (!stack.hasTag()) return null;
		CompoundTagAccessor tag = new CompoundTagAccessor(stack.getTag());
		if (!tag.hasUUID("CrystalId")) return null;
		return tag.getUUID("CrystalId");
	}

	/**
	 * Get or assign a UUID for this crystal.
	 * If the crystal doesn't have an ID yet, creates a new one and registers it in SavedData.
	 */
	public static UUID getOrAssignId(ItemStack stack, ServerLevel level) {
		UUID id = getCrystalId(stack);
		if (id != null) return id;

		if (!(stack.getItem() instanceof StorageCrystalItem crystal)) return null;

		CrystalStorageData data = CrystalStorageData.get(level);
		id = data.createCrystal(crystal.storageType);
		stack.getOrCreateTag().putUUID("CrystalId", id);
		return id;
	}

	/**
	 * Get the crystal inventory for this ItemStack.
	 * Returns null if no UUID assigned or not on server.
	 */
	public static CrystalInventory getInventory(ItemStack stack, ServerLevel level) {
		UUID id = getCrystalId(stack);
		if (id == null) return null;

		if (!(stack.getItem() instanceof StorageCrystalItem crystal)) return null;

		CrystalStorageData data = CrystalStorageData.get(level);
		return data.getOrCreate(id, crystal.storageType);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, level, list, flag);
		list.add(Component.literal("§bNT " + storageType.getDisplayName() + " Storage Crystal"));
		list.add(Component.literal("§7Capacity: " + storageType.getCapacity() + " items"));
		list.add(Component.literal("§7Type Limit: " + storageType.getTypeLimit() + " types"));

		UUID id = getCrystalId(stack);
		if (id != null) {
			// Show stored items if on server (client gets synced NBT)
			if (level instanceof ServerLevel serverLevel) {
				CrystalInventory inv = getInventory(stack, serverLevel);
				if (inv != null) {
					list.add(Component.literal("§7Stored: §f" + inv.getTotalCount() + "§7/" + inv.getType().getCapacity()
							+ " §7(§f" + inv.getTypeCount() + "§7/" + inv.getType().getTypeLimit() + " types)"));
				}
			}

			// Show UUID with advanced tooltips (F3+H)
			if (flag.isAdvanced()) {
				list.add(Component.literal("§8ID: " + id));
			}
		} else {
			list.add(Component.literal("§8Not initialized"));
		}
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		// Shimmer effect when crystal contains items
		UUID id = getCrystalId(stack);
		return id != null;
	}

	/**
	 * Helper to avoid direct CompoundTag.hasUUID/getUUID which need key checks.
	 */
	private static class CompoundTagAccessor {
		private final net.minecraft.nbt.CompoundTag tag;

		CompoundTagAccessor(net.minecraft.nbt.CompoundTag tag) {
			this.tag = tag;
		}

		boolean hasUUID(String key) {
			return tag != null && tag.hasUUID(key);
		}

		UUID getUUID(String key) {
			return tag.getUUID(key);
		}
	}
}
