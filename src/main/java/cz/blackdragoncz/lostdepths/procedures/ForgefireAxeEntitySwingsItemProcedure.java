package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;

public class ForgefireAxeEntitySwingsItemProcedure {
	public static void execute(ItemStack itemstack) {
		if (!(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, itemstack) != 0)) {
			itemstack.enchant(Enchantments.BLOCK_EFFICIENCY, 5);
		}
	}
}
