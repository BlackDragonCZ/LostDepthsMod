package cz.blackdragoncz.lostdepths.item.security;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class SecurityPassOmegaItem extends SecurityPassItem {

	public SecurityPassOmegaItem() {
		super(Integer.MAX_VALUE, '*');
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		list.add(Component.literal("DEBUG TOOL ONLY").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD));
		list.add(Component.literal("§6Bypasses all security gates and clearance checks."));
	}

	@Override
	public Component getName(ItemStack stack) {
		return Component.literal("§6Security Pass (§6§lOMEGA§r§6)");
	}

}
