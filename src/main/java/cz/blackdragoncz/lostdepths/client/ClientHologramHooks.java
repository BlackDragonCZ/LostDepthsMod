package cz.blackdragoncz.lostdepths.client;

import cz.blackdragoncz.lostdepths.block.entity.HologramProjectorBlockEntity;
import cz.blackdragoncz.lostdepths.client.gui.HologramSelectScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

// Must stay out of the block class: a Screen reference there loads Screen on the dedicated server. Call only via DistExecutor.
public final class ClientHologramHooks {

	private ClientHologramHooks() {
	}

	public static void openSelector(BlockPos pos) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level == null)
			return;
		if (!(minecraft.level.getBlockEntity(pos) instanceof HologramProjectorBlockEntity projector))
			return;
		minecraft.setScreen(new HologramSelectScreen(pos, projector.getHologram(), projector.offsetX(), projector.offsetY(), projector.offsetZ(), projector.rotationX(), projector.rotationY(), projector.rotationZ(), projector.isVisible(), projector.isCulled()));
	}
}
