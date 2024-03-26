package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.world.entity.Entity;

import cz.blackdragoncz.lostdepths.network.LostdepthsModVariables;

public class DragonUpOnKeyPressedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			boolean _setval = true;
			entity.getCapability(LostdepthsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				capability.dragonUp = _setval;
				capability.syncPlayerVariables(entity);
			});
		}
	}
}
