package cz.blackdragoncz.lostdepths.procedures;

import cz.blackdragoncz.lostdepths.init.LostdepthsModOres.OreDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

/**
 * Unified handler for solution-based ore activation (right-click).
 * Replaces the individual per-ore MCreator procedures.
 */
public class OreActivationProcedure {

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, OreDefinition oreDef) {
		if (entity == null || oreDef.activationSolution() == null || oreDef.activeBlock() == null) return;

		Item heldItem = (entity instanceof LivingEntity liv ? liv.getMainHandItem() : ItemStack.EMPTY).getItem();
		Item requiredSolution = oreDef.activationSolution().get();

		if (heldItem == requiredSolution) {
			BlockPos pos = BlockPos.containing(x, y, z);
			world.setBlock(pos, oreDef.activeBlock().get().defaultBlockState(), 3);

			if (entity instanceof Player player) {
				player.getInventory().clearOrCountMatchingItems(
						p -> requiredSolution == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
			}
		} else {
			if (entity instanceof Player player && !player.level().isClientSide()) {
				String solutionName = new ItemStack(requiredSolution).getHoverName().getString();
				player.displayClientMessage(Component.literal("You need " + solutionName + "."), false);
			}
		}
	}
}
