
package cz.blackdragoncz.lostdepths.block.machine;

import cz.blackdragoncz.lostdepths.block.entity.MetaCollectorBlockEntity;
import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;
import cz.blackdragoncz.lostdepths.init.LostdepthsModSounds;
import cz.blackdragoncz.lostdepths.recipe.MetaMaterializerRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import cz.blackdragoncz.lostdepths.procedures.MetaMaterializerOnBlockRightClickedProcedure;
import cz.blackdragoncz.lostdepths.block.entity.MetaMaterializerBlockEntity;

public class MetaMaterializerBlock extends Block implements EntityBlock {
	public MetaMaterializerBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(-1, 3600000).pushReaction(PushReaction.BLOCK));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, BlockGetter world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}

	@Override
	public BlockPathTypes getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, Mob entity) {
		return BlockPathTypes.BLOCKED;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		List<ItemStack> dropsOriginal = super.getDrops(state, builder);
		if (!dropsOriginal.isEmpty())
			return dropsOriginal;
		return Collections.singletonList(new ItemStack(this, 1));
	}

	private Direction[] scanDirections = {
			Direction.EAST,
			Direction.WEST,
			Direction.NORTH,
			Direction.SOUTH
	};

	@Override
	public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player entity, InteractionHand hand, BlockHitResult hit) {
		ItemStack heldItem = entity.getItemInHand(hand);
		int scanLength = 30;

		BlockPos scanRootPos = pos.below(2);
		List<ItemStack> itemsInCollectors = new ArrayList<>();
		List<MetaCollectorBlockEntity> collectors = new ArrayList<MetaCollectorBlockEntity>();

		for (Direction dir : scanDirections) {
			BlockPos scanPos = new BlockPos(scanRootPos);
			boolean foundCollectors = false;
			for (int i = 0; i < scanLength; i++) {
				BlockState scannedState = level.getBlockState(scanPos);

				if (scannedState.is(LostdepthsModBlocks.META_COLLECTOR.get()))
				{
					foundCollectors = true;
					MetaCollectorBlockEntity metaCollector = (MetaCollectorBlockEntity) level.getBlockEntity(scanPos);
					itemsInCollectors.add(metaCollector.getItem(0));
					collectors.add(metaCollector);
				}

				scanPos = scanPos.relative(dir);
			}

			if (foundCollectors)
				break;
		}

		List<MetaMaterializerRecipe> recipes = LostDepthsModRecipeType.META_MATERIALIZER.get().getRecipeType().getRecipes(level);

		MetaMaterializerRecipe foundRecipe = null;
		for (MetaMaterializerRecipe recipe : recipes) {
			boolean itemsInCollectorsMatch = true;

			for (int i = 0; i < 3; i++) {
				ItemStack recipeItem = recipe.getItems().get(i);
				ItemStack checkItem = itemsInCollectors.get(3 - i - 1);

				if (!(recipeItem.is(checkItem.getItem()) && checkItem.getCount() >= recipeItem.getCount()))
				{
					itemsInCollectorsMatch = false;
				}
			}

			if (itemsInCollectorsMatch)
			{
				if (recipe.getHeldItem().is(heldItem.getItem()) && heldItem.getCount() >= recipe.getHeldItem().getCount())
				{
					foundRecipe = recipe;
					break;
				} else {

					if (level.isClientSide())
						entity.displayClientMessage(Component.literal("You need to hold ").append(Component.translatable(recipe.getHeldItem().getDescriptionId())).append(" when interacting!"), false);
				}
			}
		}

		if (foundRecipe != null)
		{
			for (int i = 0; i < 3; i++) {
				ItemStack recipeItem = foundRecipe.getItems().get(i);
				ItemStack checkItem = itemsInCollectors.get(3 - i - 1);
				collectors.get(3 - i - 1).setItem(0, new ItemStack(recipeItem.getItem(), checkItem.getCount() - recipeItem.getCount()));
			}

			heldItem.shrink(foundRecipe.getHeldItem().getCount());
			entity.setItemInHand(hand, heldItem);

			Vec3i normal = hit.getDirection().getOpposite().getNormal();
			ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, foundRecipe.getResult(), normal.getX(), normal.getY(), normal.getZ());
			itemEntity.setPickUpDelay(1);
			itemEntity.setUnlimitedLifetime();
			itemEntity.setGlowingTag(true);
			level.addFreshEntity(itemEntity);

			if (!level.isClientSide())
				level.playSound(null, pos, LostdepthsModSounds.MACHINE_CRAFT.get(), SoundSource.BLOCKS, 1, 1);
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MetaMaterializerBlockEntity(pos, state);
	}

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int eventID, int eventParam) {
		super.triggerEvent(state, world, pos, eventID, eventParam);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity == null ? false : blockEntity.triggerEvent(eventID, eventParam);
	}
}
