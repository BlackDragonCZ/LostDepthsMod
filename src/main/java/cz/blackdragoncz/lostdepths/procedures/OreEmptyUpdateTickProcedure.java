package cz.blackdragoncz.lostdepths.procedures;

import cz.blackdragoncz.lostdepths.init.LostdepthsModOres;
import cz.blackdragoncz.lostdepths.init.LostdepthsModOres.OreDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;

/** Regrow timer, once per second. The "timeLeft" / "oreType" keys must not be renamed - existing worlds use them. */
public class OreEmptyUpdateTickProcedure {

	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (world.isClientSide()) return;

		BlockPos pos = BlockPos.containing(x, y, z);
		BlockEntity be = world.getBlockEntity(pos);
		if (be == null) return;

		CompoundTag data = be.getPersistentData();
		double elapsed = data.getDouble("timeLeft");

		if (elapsed < LostdepthsModOres.REGROW_SECONDS) {
			data.putDouble("timeLeft", elapsed + 1);
			be.setChanged();
			return;
		}

		// Blank or unknown tag: stay empty rather than regrow into nothing.
		OreDefinition def = LostdepthsModOres.findByOreTag(data.getString("oreType"));
		if (def == null) return;

		world.setBlock(pos, def.oreBlock().get().defaultBlockState(), 3);
	}
}
