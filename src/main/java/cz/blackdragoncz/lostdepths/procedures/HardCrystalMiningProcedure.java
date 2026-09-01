package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;

import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;

// A celestial pickaxe yields two Hard Crystals; the block always comes back either way.
// Shared by HardCrystalB and HardCrystalR, which differ only in texture and registry name.
public class HardCrystalMiningProcedure {

	private static final int DROP_COUNT = 2;

	public static void execute(LevelAccessor world, BlockPos pos, Entity entity, Block block) {
		if (entity == null)
			return;
		ItemStack held = entity instanceof LivingEntity living ? living.getMainHandItem() : ItemStack.EMPTY;
		if (held.getItem() == LostdepthsModItems.CELESTIAL_PICKAXE.get() && world instanceof ServerLevel serverLevel) {
			for (int i = 0; i < DROP_COUNT; i++)
				Block.popResource(serverLevel, pos, new ItemStack(LostdepthsModItems.HARD_CRYSTALS.get()));
		}
		world.setBlock(pos, block.defaultBlockState(), 3);
	}
}
