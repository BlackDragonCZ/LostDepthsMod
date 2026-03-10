package cz.blackdragoncz.lostdepths.procedures;

import cz.blackdragoncz.lostdepths.init.LostdepthsModParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class CaneOfVenomsHitProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity instanceof LivingEntity target) {
			VenomDamageProcedure.apply(target, 0.1, true);
		}
		if (world instanceof ServerLevel level) {
			level.sendParticles((SimpleParticleType) LostdepthsModParticleTypes.VENOMPARTICLE.get(), x, y, z, 3, 0, 0, 0, 0.1);
		}
	}
}
