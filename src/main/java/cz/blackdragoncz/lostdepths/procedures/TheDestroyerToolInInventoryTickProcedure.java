package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Advancement;

import java.util.Map;

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
			if (entity instanceof ServerPlayer _player) {
				Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("lostdepths:cheater"));
				AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
				if (!_ap.isDone()) {
					for (String criteria : _ap.getRemainingCriteria())
						_player.getAdvancements().award(_adv, criteria);
				}
			}
		}
		if (itemstack.getCount() > 1) {
			if (entity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(LostdepthsModItems.THE_DESTROYER.get());
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
		}
	}
}
