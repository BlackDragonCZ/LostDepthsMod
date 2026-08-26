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

		int count = rollBonusCount(level.getRandom(), fortuneLevel);
		for (int i = 0; i < count; i++) {
			Block.popResource(level, pos, new ItemStack(dropItem));
		}
	}

	/** 40% to yield anything, fortune widens the count. Shared with the Resource Extractor. */
	public static int rollBonusCount(RandomSource random, int fortuneLevel) {
		if (random.nextFloat() >= 0.40f) return 0;
		return fortuneLevel > 0 ? Mth.nextInt(random, 1, Math.min(7, fortuneLevel + 3)) : 1;
	}

	/** Null if the block is not one of the four vanilla ores. */
	public static Item getDropForOre(net.minecraft.world.level.block.Block block) {
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
