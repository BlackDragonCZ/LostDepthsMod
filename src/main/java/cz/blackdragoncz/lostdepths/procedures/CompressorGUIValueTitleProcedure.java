package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;

public class CompressorGUIValueTitleProcedure {
	public static String execute(LevelAccessor world, double x, double y, double z) {
		if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == LostdepthsModBlocks.EXTRA_TERESTRIAL_COMPRESSOR.get()) {
			return "\u00A74Extra-Terrestrial Compressor";
		} else if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == LostdepthsModBlocks.GALACTIC_COMPRESSOR.get()) {
			return "\u00A74Compressor Table";
		}
		return "NULL";
	}
}
