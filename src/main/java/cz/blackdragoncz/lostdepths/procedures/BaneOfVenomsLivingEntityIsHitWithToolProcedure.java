package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class BaneOfVenomsLivingEntityIsHitWithToolProcedure {
	public static void execute(Entity entity) {
		if (entity instanceof LivingEntity target) {
			VenomDamageProcedure.apply(target, 0.2, false);
		}
	}
}
