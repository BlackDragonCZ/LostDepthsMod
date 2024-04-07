package cz.blackdragoncz.lostdepths.block.entity;

import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

import java.util.List;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;

public class ExtraTerestrialCompressorBlockEntity extends AbstractCompressorBlockEntity {

	public ExtraTerestrialCompressorBlockEntity(BlockPos position, BlockState state) {
		super(List.of(LostDepthsModRecipeType.V1_COMPRESSING.get(), LostDepthsModRecipeType.V2_COMPRESSING.get()), 600, 10, LostdepthsModBlockEntities.EXTRA_TERESTRIAL_COMPRESSOR.get(), position, state);
	}

	@Override
	public Component getDefaultName() {
		return Component.literal("extra_terestrial_compressor");
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("Extra-Terrestrial Compressor");
	}
}
