
package cz.blackdragoncz.lostdepths.item.tool;

import cz.blackdragoncz.lostdepths.init.LostdepthsModEntities;
import cz.blackdragoncz.lostdepths.world.entity.projectile.ThrownDraconicTrident;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;

import java.util.List;

import cz.blackdragoncz.lostdepths.procedures.CrystalizedPickaxeMakeItemGlowProcedure;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;

public class PrimeDraconicTridentItem extends AbstractCustomTrident {
	public PrimeDraconicTridentItem() {
		super(new Tier() {
			public int getUses() {
				return 840;
			}

			public float getSpeed() {
				return 4f;
			}

			public float getAttackDamageBonus() {
				return 35f;
			}

			public int getLevel() {
				return 0;
			}

			public int getEnchantmentValue() {
				return 15;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(LostdepthsModItems.CONDENSED_ETHERIUM.get()));
			}
		}, 3, -2.7f, new Item.Properties().fireResistant());
	}

	@Override
	protected EntityType<ThrownDraconicTrident> getThrowEntity() {
		return LostdepthsModEntities.THROWN_PRIME_DRACONIC_TRIDENT.get();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack itemstack) {
		return CrystalizedPickaxeMakeItemGlowProcedure.execute();
	}
}
