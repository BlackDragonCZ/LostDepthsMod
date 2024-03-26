package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import cz.blackdragoncz.lostdepths.init.LostdepthsModGameRules;

public class TheProtectorNaturalEntitySpawningConditionProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z) {
		if (world.getLevelData().getGameRules().getBoolean(LostdepthsModGameRules.DOLOSTDEPTHSSPAWNING) == true && ((world instanceof Level _lvl ? _lvl.dimension() : Level.OVERWORLD) == Level.NETHER
				|| (world instanceof Level _lvl ? _lvl.dimension() : Level.OVERWORLD) == (ResourceKey.create(Registries.DIMENSION, new ResourceLocation("lostdepths:below_bedrock")))
				|| (world instanceof Level _lvl ? _lvl.dimension() : Level.OVERWORLD) == Level.OVERWORLD)) {
			if (world.getBiome(BlockPos.containing(x, y, z)).is(new ResourceLocation("birch_forest")) || world.getBiome(BlockPos.containing(x, y, z)).is(new ResourceLocation("forest"))
					|| world.getBiome(BlockPos.containing(x, y, z)).is(new ResourceLocation("dark_forest")) || world.getBiome(BlockPos.containing(x, y, z)).is(new ResourceLocation("savanna"))
					|| world.getBiome(BlockPos.containing(x, y, z)).is(new ResourceLocation("taiga"))) {
				return true;
			}
		}
		return false;
	}
}
