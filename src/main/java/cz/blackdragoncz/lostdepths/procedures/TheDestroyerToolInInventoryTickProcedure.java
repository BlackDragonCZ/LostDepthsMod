package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import java.util.Map;

import cz.blackdragoncz.lostdepths.advancements.LostdepthsAdvancementTriggers;
import cz.blackdragoncz.lostdepths.advancements.LostdepthsAdvancements;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;

public class TheDestroyerToolInInventoryTickProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, itemstack) != 0 || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SMITE, itemstack) != 0
				|| EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS, itemstack) != 0) {
			{
				Map<Enchantment, Integer> _enchantments = EnchantmentHelper.getEnchantments(itemstack);
				if (_enchantments.containsKey(Enchantments.SMITE)) {
					_enchantments.remove(Enchantments.SMITE);
					EnchantmentHelper.setEnchantments(_enchantments, itemstack);
				}
			}
			{
				Map<Enchantment, Integer> _enchantments = EnchantmentHelper.getEnchantments(itemstack);
				if (_enchantments.containsKey(Enchantments.SHARPNESS)) {
					_enchantments.remove(Enchantments.SHARPNESS);
					EnchantmentHelper.setEnchantments(_enchantments, itemstack);
				}
			}
			{
				Map<Enchantment, Integer> _enchantments = EnchantmentHelper.getEnchantments(itemstack);
				if (_enchantments.containsKey(Enchantments.BANE_OF_ARTHROPODS)) {
					_enchantments.remove(Enchantments.BANE_OF_ARTHROPODS);
					EnchantmentHelper.setEnchantments(_enchantments, itemstack);
				}
			}
			LostdepthsAdvancementTriggers.award(entity, LostdepthsAdvancements.CHEATER);
		}
		if (itemstack.getCount() > 1) {
			if (entity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(LostdepthsModItems.THE_DESTROYER.get());
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
		}
	}
}
