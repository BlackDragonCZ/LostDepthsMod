package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.entity.Entity;

import cz.blackdragoncz.lostdepths.network.LostdepthsModVariables;

public class FluxLanternRightclickedOnBlockProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (entity.isShiftKeyDown()) {
			{
				double _setval = entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(10)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX();
				entity.getCapability(LostdepthsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.flux_x = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
			{
				double _setval = entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(10)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY();
				entity.getCapability(LostdepthsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.flux_y = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
			{
				double _setval = entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(10)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ();
				entity.getCapability(LostdepthsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.flux_z = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
			{
				String _setval = "" + (world instanceof Level _lvl ? _lvl.dimension() : Level.OVERWORLD);
				entity.getCapability(LostdepthsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.flux_dim = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		}
	}
}
