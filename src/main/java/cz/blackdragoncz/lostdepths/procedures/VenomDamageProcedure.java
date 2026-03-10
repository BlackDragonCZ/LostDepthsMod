package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Shared damage logic for Cane of Venoms (ranged) and Bane of Venoms (melee).
 * Deals percentage max-health damage per active beneficial potion effect on the target,
 * including modded effects.
 */
public class VenomDamageProcedure {

	/**
	 * @param target         the entity being hit
	 * @param percentPerBuff damage per beneficial effect as a fraction of max health (0.1 = 10%, 0.2 = 20%)
	 * @param ignoreArmor    true for ranged (Cane), false for melee (Bane)
	 */
	public static void apply(LivingEntity target, double percentPerBuff, boolean ignoreArmor) {
		int buffCount = 0;
		for (MobEffectInstance effect : target.getActiveEffects()) {
			if (effect.getEffect().getCategory() == MobEffectCategory.BENEFICIAL) {
				buffCount++;
			}
		}

		if (buffCount == 0) return;

		float totalDamage = (float) (target.getMaxHealth() * percentPerBuff * buffCount);

		if (!ignoreArmor) {
			int armor = target.getArmorValue();
			if (armor > 0) {
				totalDamage /= armor;
			}
		}

		if (totalDamage <= 0) return;

		target.hurt(new DamageSource(
				target.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
						.getHolderOrThrow(DamageTypes.GENERIC)) {
			@Override
			public Component getLocalizedDeathMessage(LivingEntity msgEntity) {
				String key = "death.attack.elimination";
				if (getEntity() == null && getDirectEntity() == null) {
					return msgEntity.getKillCredit() != null
							? Component.translatable(key + ".player", msgEntity.getDisplayName(), msgEntity.getKillCredit().getDisplayName())
							: Component.translatable(key, msgEntity.getDisplayName());
				}
				Component source = getEntity() == null ? getDirectEntity().getDisplayName() : getEntity().getDisplayName();
				ItemStack held = ItemStack.EMPTY;
				if (getEntity() instanceof LivingEntity liv) held = liv.getMainHandItem();
				return !held.isEmpty() && held.hasCustomHoverName()
						? Component.translatable(key + ".item", msgEntity.getDisplayName(), source, held.getDisplayName())
						: Component.translatable(key, msgEntity.getDisplayName(), source);
			}
		}, totalDamage);
	}
}
