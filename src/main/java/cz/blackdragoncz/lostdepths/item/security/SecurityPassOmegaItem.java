package cz.blackdragoncz.lostdepths.item.security;

import cz.blackdragoncz.lostdepths.util.TextEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
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
		list.add(Component.literal("§7Bypasses all security gates and clearance checks."));
	}

	@Override
	public Component getName(ItemStack stack) {
		long tick = 0;
		try {
			if (Minecraft.getInstance().level != null) {
				tick = Minecraft.getInstance().level.getGameTime();
			}
		} catch (Exception ignored) {}

		MutableComponent name = Component.empty();
		name.append(Component.literal("Security Pass (").withStyle(ChatFormatting.WHITE));
		name.append(TextEffects.wave("OMEGA", 0xFFAA00, tick, true).withStyle(ChatFormatting.BOLD));
		name.append(Component.literal(")").withStyle(ChatFormatting.WHITE));
		return name;
	}
}
