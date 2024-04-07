package cz.blackdragoncz.lostdepths.block.entity;

import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;

import java.util.List;

public class GalacticCompressorBlockEntity extends AbstractCompressorBlockEntity {

	public GalacticCompressorBlockEntity(BlockPos position, BlockState state) {
		super(List.of(LostDepthsModRecipeType.V1_COMPRESSING.get()), 1000, 15, LostdepthsModBlockEntities.GALACTIC_COMPRESSOR.get(), position, state);
	}

	@Override
	public Component getDefaultName() {
		return Component.literal("galactic_compressor");
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("Galactic Compressor");
	}
}
