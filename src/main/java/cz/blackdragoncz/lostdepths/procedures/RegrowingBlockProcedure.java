package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;

/*
 * A block that is never actually consumed: breaking it puts it straight back. With the resource overload
 * it also acts as a renewable material source, yielding a drop to anyone using the right tool.
 * Every block using this must carry noLootTable() - they must never drop themselves.
 *
 * Plain regrow: Ferro Leaves, Sunder Leaves.
 * With a resource: Clovinite Ore, Sunder Wood.
 */
public class RegrowingBlockProcedure {

	// Regrows and yields nothing. The leaves are due their own progression drops - when those exist,
	// switch the call site to the overload below rather than writing a new procedure.
	public static void execute(LevelAccessor world, BlockPos pos, Block block) {
		world.setBlock(pos, block.defaultBlockState(), 3);
	}

	// Regrows, and yields the drop to anyone who broke it holding the right tool.
	public static void execute(LevelAccessor world, BlockPos pos, Entity entity, Item tool, Item drop, Block block) {
		if (entity != null) {
			ItemStack held = entity instanceof LivingEntity living ? living.getMainHandItem() : ItemStack.EMPTY;
			if (held.getItem() == tool && world instanceof ServerLevel level)
				Block.popResource(level, pos, new ItemStack(drop));
		}
		execute(world, pos, block);
	}
}
