package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

public class BaneOfVenomsLivingEntityIsHitWithToolProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double totalDamage = 0;
		if (true) {
			if (entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(MobEffects.REGENERATION)) {
				totalDamage = totalDamage + (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.2;
			}
			if (entity instanceof LivingEntity _livEnt2 && _livEnt2.hasEffect(MobEffects.FIRE_RESISTANCE)) {
				totalDamage = totalDamage + (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.2;
			}
			if (entity instanceof LivingEntity _livEnt4 && _livEnt4.hasEffect(MobEffects.DAMAGE_BOOST)) {
				totalDamage = totalDamage + (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.2;
			}
			if (entity instanceof LivingEntity _livEnt6 && _livEnt6.hasEffect(MobEffects.WATER_BREATHING)) {
				totalDamage = totalDamage + (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.2;
			}
			if (entity instanceof LivingEntity _livEnt8 && _livEnt8.hasEffect(MobEffects.GLOWING)) {
				totalDamage = totalDamage + (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.2;
			}
			if (entity instanceof LivingEntity _livEnt10 && _livEnt10.hasEffect(MobEffects.INVISIBILITY)) {
				totalDamage = totalDamage + (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.2;
			}
			if (entity instanceof LivingEntity _livEnt12 && _livEnt12.hasEffect(MobEffects.JUMP)) {
				totalDamage = totalDamage + (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.2;
			}
			if (entity instanceof LivingEntity _livEnt14 && _livEnt14.hasEffect(MobEffects.NIGHT_VISION)) {
				totalDamage = totalDamage + (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.2;
			}
			if (entity instanceof LivingEntity _livEnt16 && _livEnt16.hasEffect(MobEffects.MOVEMENT_SPEED)) {
				totalDamage = totalDamage + (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.2;
			}
			if (entity instanceof LivingEntity _livEnt18 && _livEnt18.hasEffect(MobEffects.SLOW_FALLING)) {
				totalDamage = totalDamage + (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.2;
			}
		}
		if (true) {
			if (entity instanceof LivingEntity _entity)
				_entity.hurt(new DamageSource(_entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC)) {
					@Override
					public Component getLocalizedDeathMessage(LivingEntity _msgEntity) {
						String _translatekey = "death.attack." + "elimination";
						if (this.getEntity() == null && this.getDirectEntity() == null) {
							return _msgEntity.getKillCredit() != null
									? Component.translatable(_translatekey + ".player", _msgEntity.getDisplayName(), _msgEntity.getKillCredit().getDisplayName())
									: Component.translatable(_translatekey, _msgEntity.getDisplayName());
						} else {
							Component _component = this.getEntity() == null ? this.getDirectEntity().getDisplayName() : this.getEntity().getDisplayName();
							ItemStack _itemstack = ItemStack.EMPTY;
							if (this.getEntity() instanceof LivingEntity _livingentity)
								_itemstack = _livingentity.getMainHandItem();
							return !_itemstack.isEmpty() && _itemstack.hasCustomHoverName()
									? Component.translatable(_translatekey + ".item", _msgEntity.getDisplayName(), _component, _itemstack.getDisplayName())
									: Component.translatable(_translatekey, _msgEntity.getDisplayName(), _component);
						}
					}
				}, (float) (totalDamage / (entity instanceof LivingEntity _livEnt ? _livEnt.getArmorValue() : 0)));
			totalDamage = 0;
		}
	}
}
