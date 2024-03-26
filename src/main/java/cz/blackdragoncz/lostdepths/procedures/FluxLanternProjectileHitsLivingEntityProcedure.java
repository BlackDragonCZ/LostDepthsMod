package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;

import cz.blackdragoncz.lostdepths.network.LostdepthsModVariables;

public class FluxLanternProjectileHitsLivingEntityProcedure {
	public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if ((sourceentity.getCapability(LostdepthsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new LostdepthsModVariables.PlayerVariables())).flux_set == true) {
			if (((sourceentity.getCapability(LostdepthsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new LostdepthsModVariables.PlayerVariables())).flux_dim)
					.contains("" + (world instanceof Level _lvl ? _lvl.dimension() : Level.OVERWORLD))) {
				{
					Entity _ent = entity;
					_ent.teleportTo(((sourceentity.getCapability(LostdepthsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new LostdepthsModVariables.PlayerVariables())).flux_x),
							((sourceentity.getCapability(LostdepthsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new LostdepthsModVariables.PlayerVariables())).flux_y),
							((sourceentity.getCapability(LostdepthsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new LostdepthsModVariables.PlayerVariables())).flux_z));
					if (_ent instanceof ServerPlayer _serverPlayer)
						_serverPlayer.connection.teleport(((sourceentity.getCapability(LostdepthsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new LostdepthsModVariables.PlayerVariables())).flux_x),
								((sourceentity.getCapability(LostdepthsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new LostdepthsModVariables.PlayerVariables())).flux_y),
								((sourceentity.getCapability(LostdepthsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new LostdepthsModVariables.PlayerVariables())).flux_z), _ent.getYRot(), _ent.getXRot());
				}
			}
		}
	}
}
