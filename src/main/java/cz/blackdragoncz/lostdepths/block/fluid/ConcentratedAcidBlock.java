
package cz.blackdragoncz.lostdepths.block.fluid;

import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

import cz.blackdragoncz.lostdepths.procedures.ConcentratedAcidMobplayerCollidesBlockProcedure;
import cz.blackdragoncz.lostdepths.init.LostdepthsModFluids;

public class ConcentratedAcidBlock extends LiquidBlock {
	public ConcentratedAcidBlock() {
		super(() -> LostdepthsModFluids.CONCENTRATED_ACID.get(), BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).strength(100f).hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true).lightLevel(s -> 15).noCollission()
				.noLootTable().liquid().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY).replaceable());
	}

	@Override
	public void entityInside(BlockState blockstate, Level world, BlockPos pos, Entity entity) {
		super.entityInside(blockstate, world, pos, entity);
		ConcentratedAcidMobplayerCollidesBlockProcedure.execute(entity);
	}
}
