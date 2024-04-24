
package cz.blackdragoncz.lostdepths.item.tool;

import cz.blackdragoncz.lostdepths.init.LostdepthsModEntities;
import cz.blackdragoncz.lostdepths.world.entity.projectile.ThrownDraconicTrident;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.chat.Component;

import java.util.List;

import cz.blackdragoncz.lostdepths.procedures.CrystalizedPickaxeMakeItemGlowProcedure;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;

public class DraconicTridentItem extends AbstractCustomTrident {

	public static final int THROW_THRESHOLD_TIME = 10;
	public static final float BASE_DAMAGE = 8.0F;
	public static final float SHOOT_POWER = 2.5F;

	public DraconicTridentItem() {
		super(new Tier() {
			public int getUses() {
				return 520;
			}

			public float getSpeed() {
				return 4f;
			}

			public float getAttackDamageBonus() {
				return 19f;
			}

			public int getLevel() {
				return 0;
			}

			public int getEnchantmentValue() {
				return 15;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(LostdepthsModItems.CONDENSED_POLARIUM.get()));
			}
		}, 3, -2.4f, new Item.Properties().fireResistant());
	}

	@Override
	protected EntityType<ThrownDraconicTrident> getThrowEntity() {
		return LostdepthsModEntities.THROWN_DRACONIC_TRIDENT.get();
	}
}
