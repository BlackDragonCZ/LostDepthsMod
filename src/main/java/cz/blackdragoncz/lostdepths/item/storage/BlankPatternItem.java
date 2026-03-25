package cz.blackdragoncz.lostdepths.item.storage;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class BlankPatternItem extends Item {

	public BlankPatternItem() {
		super(new Item.Properties().stacksTo(64));
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
		list.add(Component.literal("§7Use in a Pattern Encoder to create crafting patterns."));
	}
}
