package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;

import cz.blackdragoncz.lostdepths.network.LostdepthsModVariables;

public class GalaxyDragonOnEntityTickUpdateProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity.isVehicle() && (entity.getCapability(LostdepthsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new LostdepthsModVariables.PlayerVariables())).dragonUp) {
			entity.setNoGravity(true);
			entity.setDeltaMovement(new Vec3(0, 0.3, 0));
		} else if (entity.isVehicle() && (entity.getCapability(LostdepthsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new LostdepthsModVariables.PlayerVariables())).dragonDown) {
			entity.setNoGravity(true);
			entity.setDeltaMovement(new Vec3(0, (-0.3), 0));
		} else if (!entity.isVehicle()) {
			entity.setNoGravity(false);
		} else if (entity.onGround()) {
			entity.setNoGravity(false);
		}
	}
}
