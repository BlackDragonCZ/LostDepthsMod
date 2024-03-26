package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import cz.blackdragoncz.lostdepths.init.LostdepthsModMobEffects;

public class GalaxyDragonEntityIsHurtProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
			_entity.addEffect(new MobEffectInstance(LostdepthsModMobEffects.ULTRA_HEAL.get(), 10, 3, false, false));
	}
}
