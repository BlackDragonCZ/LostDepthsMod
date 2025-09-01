
package cz.blackdragoncz.lostdepths.item.tool;

import cz.blackdragoncz.lostdepths.procedures.item.TheDestroyerSwordFunctionality;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

import java.util.List;

import cz.blackdragoncz.lostdepths.procedures.TheDestroyerToolInInventoryTickProcedure;
import cz.blackdragoncz.lostdepths.procedures.TheDestroyerLivingEntityIsHitWithToolProcedure;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;

public class TheDestroyerItem extends SwordItem {
	public TheDestroyerItem() {
		super(new Tier() {
			public int getUses() {
				return 1000;
			}

			public float getSpeed() {
				return 4f;
			}

			public float getAttackDamageBonus() {
				return -0.5f;
			}

			public int getLevel() {
				return 0;
			}

			public int getEnchantmentValue() {
				return 2;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(LostdepthsModItems.ENERGYZED_ALLOY.get()));
			}
		}, 3, -3.5f, new Item.Properties().fireResistant());
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
		TheDestroyerSwordFunctionality.execute(entity, sourceentity);
		return retval;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		list.add(Component.literal("§2Deal 60% max health damage to players."));
		list.add(Component.literal("§4Deal 20% more damage to transformed entities"));
	}

	@Override
	public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(itemstack, world, entity, slot, selected);
		TheDestroyerToolInInventoryTickProcedure.execute(entity, itemstack);
	}
}
