package cz.blackdragoncz.lostdepths.client;

import cz.blackdragoncz.lostdepths.network.LostdepthsModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

// Must stay out of the packet class: Minecraft.player is a LocalPlayer, which the verifier loads on the dedicated server. Call only via DistExecutor.
public final class ClientVariableHooks {

	private ClientVariableHooks() {
	}

	public static void applyPlayerVariables(LostdepthsModVariables.PlayerVariables data) {
		Player player = Minecraft.getInstance().player;
		if (player == null)
			return;
		LostdepthsModVariables.PlayerVariables variables = player.getCapability(LostdepthsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new LostdepthsModVariables.PlayerVariables());
		variables.x = data.x;
		variables.y = data.y;
		variables.z = data.z;
		variables.modidShieldTimer = data.modidShieldTimer;
		variables.dragonDown = data.dragonDown;
		variables.dragonUp = data.dragonUp;
		variables.flux_x = data.flux_x;
		variables.flux_y = data.flux_y;
		variables.flux_z = data.flux_z;
		variables.flux_dim = data.flux_dim;
		variables.flux_set = data.flux_set;
	}
}
