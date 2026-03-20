package cz.blackdragoncz.lostdepths.item.storage;

import cz.blackdragoncz.lostdepths.storage.StorageType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class StorageCrystalItem extends Item {
	private final StorageType storageType;

	public StorageCrystalItem(StorageType storageType) {
		super(new Properties().stacksTo(1).fireResistant());
		this.storageType = storageType;
	}

	public StorageType getStorageType() {
		return storageType;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, level, list, flag);
		list.add(Component.literal("§bNT " + storageType.getDisplayName() + " Storage Crystal"));
		list.add(Component.literal("§7Capacity: " + storageType.getCapacity() + " items"));
		list.add(Component.literal("§7Type Limit: " + storageType.getTypeLimit() + " types"));
	}
}
