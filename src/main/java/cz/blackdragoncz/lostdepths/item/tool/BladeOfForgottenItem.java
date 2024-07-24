
package cz.blackdragoncz.lostdepths.item.tool;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.chat.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import cz.blackdragoncz.lostdepths.procedures.BladeOfForgottenLivingEntityIsHitWithToolProcedure;

public class BladeOfForgottenItem extends SwordItem {
	public BladeOfForgottenItem() {
		super(new Tier() {
			public int getUses() {
				return 2000;
			}

			public float getSpeed() {
				return 4f;
			}

			public float getAttackDamageBonus() {
				return 0.0f;
			}

			public int getLevel() {
				return 1;
			}

			public int getEnchantmentValue() {
				return 0;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of();
			}
		}, 0, -2.4f, new Item.Properties().fireResistant());
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
		BladeOfForgottenLivingEntityIsHitWithToolProcedure.execute(entity, sourceentity);

		if (entity.isDeadOrDying() && entity instanceof Mob mob)
		{
            try {
                Method method = LivingEntity.class.getDeclaredMethod("getDeathSound");
				method.setAccessible(true);
				entity.playSound((SoundEvent) method.invoke(mob), 1, 1);
            } catch (NoSuchMethodException e) {
            } catch (InvocationTargetException e) {
            } catch (IllegalAccessException e) {
            }
		}

		return retval;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		list.add(Component.literal("§6Deals 30% Health True Damage if you currently have, above 50% Max Health"));
		list.add(Component.literal("§4Otherwise deals 60% Max Health"));
	}
}
