package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import cz.blackdragoncz.lostdepths.init.LostdepthsModGameRules;

public class TheProtectorOnInitialEntitySpawnProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (world.getLevelData().getGameRules().getBoolean(LostdepthsModGameRules.DOLOSTDEPTHSSPAWNING) == false) {
			if (!entity.level().isClientSide())
				entity.discard();
		}
	}
}
