
package cz.blackdragoncz.lostdepths.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

public class AntiWarpMobEffect extends MobEffect {
	public AntiWarpMobEffect() {
		super(MobEffectCategory.HARMFUL, -10092442);
	}

	@Override
	public String getDescriptionId() {
		return "effect.lostdepths.anti_warp";
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
