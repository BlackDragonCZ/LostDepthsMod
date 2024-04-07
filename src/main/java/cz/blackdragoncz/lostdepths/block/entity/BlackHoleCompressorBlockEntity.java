package cz.blackdragoncz.lostdepths.block.entity;

import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

import java.util.List;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;

public class BlackHoleCompressorBlockEntity extends AbstractCompressorBlockEntity {

	public BlackHoleCompressorBlockEntity(BlockPos position, BlockState state) {
		super(List.of(
				LostDepthsModRecipeType.V1_COMPRESSING.get(),
				LostDepthsModRecipeType.V2_COMPRESSING.get(),
				LostDepthsModRecipeType.V3_COMPRESSING.get()),
				500,
				5,
				2000000,
				493,
				10000,
				LostdepthsModBlockEntities.BLACK_HOLE_COMPRESSOR.get(),
				position, state);
	}

	@Override
	public Component getDefaultName() {
		return Component.literal("black_hole_compressor");
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("BlackHole Compressor");
	}

}
