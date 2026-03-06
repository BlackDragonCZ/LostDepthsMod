package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;

public class OmniPickaxeBlockDestroyedWithToolProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null || !(world instanceof ServerLevel level))
			return;

		BlockPos pos = BlockPos.containing(x, y, z);
		net.minecraft.world.level.block.Block block = world.getBlockState(pos).getBlock();
		ItemStack tool = entity instanceof LivingEntity livEnt ? livEnt.getMainHandItem() : ItemStack.EMPTY;
		int fortuneLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);

		Item dropItem = getDropForOre(block);
		if (dropItem == null) return;

		// 40% chance to drop (6 out of 0-10 range)
		RandomSource random = level.getRandom();
		if (Mth.nextInt(random, 0, 10) < 6) return;

		int count = 1;
		if (fortuneLevel > 0) {
			count = Mth.nextInt(random, 1, Math.min(7, fortuneLevel + 3));
		}

		for (int i = 0; i < count; i++) {
			Block.popResource(level, pos, new ItemStack(dropItem));
		}
	}

	private static Item getDropForOre(net.minecraft.world.level.block.Block block) {
		if (block == Blocks.IRON_ORE || block == Blocks.DEEPSLATE_IRON_ORE)
			return LostdepthsModItems.CELESTIAL_IRON.get();
		if (block == Blocks.DIAMOND_ORE || block == Blocks.DEEPSLATE_DIAMOND_ORE)
			return LostdepthsModItems.CELESTIAL_DIAMOND.get();
		if (block == Blocks.GOLD_ORE || block == Blocks.DEEPSLATE_GOLD_ORE)
			return LostdepthsModItems.CELESTIAL_GOLD.get();
		if (block == Blocks.REDSTONE_ORE || block == Blocks.DEEPSLATE_REDSTONE_ORE)
			return LostdepthsModItems.CELESTIAL_REDSTONE.get();
		return null;
	}
}
