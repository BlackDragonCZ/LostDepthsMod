
package cz.blackdragoncz.lostdepths.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import cz.blackdragoncz.lostdepths.procedures.UltraHealOnEffectActiveTickProcedure;

public class UltraHealMobEffect extends MobEffect {
	public UltraHealMobEffect() {
		super(MobEffectCategory.NEUTRAL, -6750208);
	}

	@Override
	public String getDescriptionId() {
		return "effect.lostdepths.ultra_heal";
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		UltraHealOnEffectActiveTickProcedure.execute(entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
