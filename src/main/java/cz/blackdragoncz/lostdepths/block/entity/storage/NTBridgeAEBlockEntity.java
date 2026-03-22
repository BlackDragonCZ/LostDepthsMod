package cz.blackdragoncz.lostdepths.block.entity.storage;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;

/**
 * Applied Energistics bridge — always registered.
 * Functional only when AE2 is loaded.
 * Items inside remain extractable regardless of AE2 presence.
 */
public class NTBridgeAEBlockEntity extends NTBridgeBlockEntity {

	public NTBridgeAEBlockEntity(BlockPos pos, BlockState state) {
		super(LostdepthsModBlockEntities.NT_BRIDGE_AE.get(), pos, state);
	}

	public static boolean isAE2Loaded() {
		return ModList.get().isLoaded("ae2");
	}
}
