package cz.blackdragoncz.lostdepths.block.puzzle;

import cz.blackdragoncz.lostdepths.block.entity.LightPuzzleControllerBlockEntity;
import cz.blackdragoncz.lostdepths.recipe.LightPuzzleRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class LightPuzzleControllerBlock extends Block implements EntityBlock {

	public LightPuzzleControllerBlock() {
		super(BlockBehaviour.Properties.of()
				.sound(SoundType.METAL)
				.strength(-1, 3600000)
				.pushReaction(PushReaction.BLOCK));
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}

	@Override
	public BlockPathTypes getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, @Nullable net.minecraft.world.entity.Mob entity) {
		return BlockPathTypes.BLOCKED;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		return Collections.emptyList();
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new LightPuzzleControllerBlockEntity(pos, state);
	}

	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.isClientSide()) return InteractionResult.SUCCESS;

		BlockEntity be = level.getBlockEntity(pos);
		if (!(be instanceof LightPuzzleControllerBlockEntity controller)) return InteractionResult.PASS;

		if (!controller.isConfigured()) {
			player.displayClientMessage(Component.literal("§cController not configured. Use /ld_lightpuzzle command."), true);
			return InteractionResult.CONSUME;
		}

		ItemStack heldItem = player.getItemInHand(hand);

		if (controller.hasActiveRecipe()) {
			// Recipe is active — check if puzzle is solved
			if (controller.isPuzzleSolved()) {
				// Solved! Give output, reset plane to OFF, clear recipe
				ItemStack output = controller.getRecipeOutput();
				if (!output.isEmpty()) {
					ItemHandlerHelper.giveItemToPlayer(player, output.copy());
				}
				controller.setPlaneAllOff();
				controller.clearRecipe();
				player.displayClientMessage(Component.literal("§aPuzzle completed!"), true);
			} else {
				// Not solved — re-randomize for retry
				controller.randomizePlane();
				player.displayClientMessage(Component.literal("§ePuzzle reset. Try again!"), true);
			}
			return InteractionResult.CONSUME;
		}

		// No active recipe — check if held item matches any light_puzzle recipe
		if (!heldItem.isEmpty()) {
			LightPuzzleRecipe recipe = controller.findRecipe(heldItem);
			if (recipe != null) {
				// Consume 1 input item
				heldItem.shrink(1);
				// Set recipe and randomize
				controller.setActiveRecipe(recipe.getOutput());
				controller.randomizePlane();
				player.displayClientMessage(Component.literal("§bPuzzle activated! Make all tiles lit to complete."), true);
				return InteractionResult.CONSUME;
			}
		}

		// No valid input — show status
		player.displayClientMessage(Component.literal("§7Right-click with a valid item to start a puzzle."), true);
		return InteractionResult.CONSUME;
	}
}
