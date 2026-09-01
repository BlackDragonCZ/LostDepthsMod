package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;

// The space rock family cannot be mined without a celestial tool - the block goes straight back.
// Shared by SpaceRock, DeepSpaceRock, FungalSpaceRock and SpaceRockDirt, which each pass themselves.
public class SpaceRockRestoreProcedure {

	private static final TagKey<Item> CELESTIAL_TOOLS = ItemTags.create(new ResourceLocation("lostdepths:celestial_tools"));

	public static void execute(LevelAccessor world, BlockPos pos, Entity entity, Block block) {
		if (entity == null)
			return;
		ItemStack held = entity instanceof LivingEntity living ? living.getMainHandItem() : ItemStack.EMPTY;
		if (held.is(CELESTIAL_TOOLS))
			return;
		world.setBlock(pos, block.defaultBlockState(), 3);
	}
}
