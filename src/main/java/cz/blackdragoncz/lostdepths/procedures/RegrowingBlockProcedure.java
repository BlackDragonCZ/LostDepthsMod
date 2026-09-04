package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

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
				popTowardEntity(level, pos, entity, new ItemStack(drop));
		}
		execute(world, pos, block);
	}

	// These blocks come straight back, so a centre spawn lands inside solid stone and collision flings the item off at speed.
	// Drop it just outside the face the breaker is standing on instead. Shared by every never-consumed block that yields a resource.
	public static void popTowardEntity(ServerLevel level, BlockPos pos, Entity entity, ItemStack stack) {
		Vec3 eye = entity.getEyePosition();
		Direction face = Direction.getNearest(eye.x - (pos.getX() + 0.5), eye.y - (pos.getY() + 0.5), eye.z - (pos.getZ() + 0.5));
		Block.popResourceFromFace(level, pos, face, stack);
	}
}
