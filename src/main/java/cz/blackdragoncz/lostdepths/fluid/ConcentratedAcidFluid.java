
package cz.blackdragoncz.lostdepths.fluid;

import net.minecraftforge.fluids.ForgeFlowingFluid;

import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.LiquidBlock;

import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.init.LostdepthsModFluids;
import cz.blackdragoncz.lostdepths.init.LostdepthsModFluidTypes;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;

public abstract class ConcentratedAcidFluid extends ForgeFlowingFluid {
	public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(() -> LostdepthsModFluidTypes.CONCENTRATED_ACID_TYPE.get(), () -> LostdepthsModFluids.CONCENTRATED_ACID.get(),
			() -> LostdepthsModFluids.FLOWING_CONCENTRATED_ACID.get()).explosionResistance(100f).bucket(() -> LostdepthsModItems.CONCENTRATED_ACID_BUCKET.get()).block(() -> (LiquidBlock) LostdepthsModBlocks.CONCENTRATED_ACID.get());

	private ConcentratedAcidFluid() {
		super(PROPERTIES);
	}

	public static class Source extends ConcentratedAcidFluid {
		public int getAmount(FluidState state) {
			return 8;
		}

		public boolean isSource(FluidState state) {
			return true;
		}
	}

	public static class Flowing extends ConcentratedAcidFluid {
		protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}

		public int getAmount(FluidState state) {
			return state.getValue(LEVEL);
		}

		public boolean isSource(FluidState state) {
			return false;
		}
	}
}
