
package cz.blackdragoncz.lostdepths.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

public class IronheartMobEffect extends MobEffect {
	public IronheartMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -3407821);
	}

	@Override
	public String getDescriptionId() {
		return "effect.lostdepths.ironheart";
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
