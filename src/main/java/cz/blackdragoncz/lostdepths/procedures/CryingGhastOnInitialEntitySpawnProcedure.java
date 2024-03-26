package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.world.entity.Entity;

public class CryingGhastOnInitialEntitySpawnProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putDouble("Brevive", 4);
	}
}
