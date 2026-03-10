package cz.blackdragoncz.lostdepths.item.tool;

import cz.blackdragoncz.lostdepths.init.LostdepthsModDamageTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class WeaponDamageHelper {

	private WeaponDamageHelper() {}

	public static DamageSource trueDamage(LivingEntity target, Entity attacker, String deathKey) {
		var registry = target.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
		var holder = registry.getHolder(LostdepthsModDamageTypes.TRUE_DAMAGE)
				.orElseGet(() -> registry.getHolderOrThrow(DamageTypes.GENERIC));
		return namedSource(holder, attacker, deathKey);
	}

	public static DamageSource genericDamage(LivingEntity target, Entity attacker, String deathKey) {
		var registry = target.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
		return namedSource(registry.getHolderOrThrow(DamageTypes.GENERIC), attacker, deathKey);
	}

	public static DamageSource magicDamage(LivingEntity target, Entity attacker, String deathKey) {
		var registry = target.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
		return namedSource(registry.getHolderOrThrow(DamageTypes.INDIRECT_MAGIC), attacker, deathKey);
	}

	private static DamageSource namedSource(Holder<DamageType> holder, Entity attacker, String deathKey) {
		return new DamageSource(holder, attacker) {
			@Override
			public Component getLocalizedDeathMessage(LivingEntity victim) {
				if (getEntity() == null && getDirectEntity() == null) {
					return victim.getKillCredit() != null
							? Component.translatable(deathKey + ".player", victim.getDisplayName(), victim.getKillCredit().getDisplayName())
							: Component.translatable(deathKey, victim.getDisplayName());
				}
				Component killerName = (getEntity() == null ? getDirectEntity() : getEntity()).getDisplayName();
				ItemStack item = ItemStack.EMPTY;
				if (getEntity() instanceof LivingEntity living) item = living.getMainHandItem();
				return !item.isEmpty() && item.hasCustomHoverName()
						? Component.translatable(deathKey + ".item", victim.getDisplayName(), killerName, item.getDisplayName())
						: Component.translatable(deathKey, victim.getDisplayName(), killerName);
			}
		};
	}
}
