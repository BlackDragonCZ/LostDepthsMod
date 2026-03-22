package cz.blackdragoncz.lostdepths.block.entity.storage;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;

/**
 * Refined Storage bridge — always registered.
 * Functional only when Refined Storage is loaded.
 * Items inside remain extractable regardless of RS presence.
 */
public class NTBridgeRSBlockEntity extends NTBridgeBlockEntity {

	public NTBridgeRSBlockEntity(BlockPos pos, BlockState state) {
		super(LostdepthsModBlockEntities.NT_BRIDGE_RS.get(), pos, state);
	}

	public static boolean isRSLoaded() {
		return ModList.get().isLoaded("refinedstorage");
	}
}
