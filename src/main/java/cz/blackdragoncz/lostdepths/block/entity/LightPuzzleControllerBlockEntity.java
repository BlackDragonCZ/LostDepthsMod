package cz.blackdragoncz.lostdepths.block.entity;

import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;
import cz.blackdragoncz.lostdepths.recipe.LightPuzzleRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LightPuzzleControllerBlockEntity extends BlockEntity {

	// Puzzle plane corners (configured via command)
	private BlockPos corner1 = BlockPos.ZERO;
	private BlockPos corner2 = BlockPos.ZERO;
	private int maxLitBlocks = -1; // -1 = auto (~2/3 of total)
	private boolean configured = false;

	// Active recipe state
	private ItemStack recipeOutput = ItemStack.EMPTY;
	private boolean hasActiveRecipe = false;

	public LightPuzzleControllerBlockEntity(BlockPos pos, BlockState state) {
		super(LostdepthsModBlockEntities.LIGHT_PUZZLE_CONTROLLER.get(), pos, state);
	}

	// --- Configuration ---

	public void configure(BlockPos c1, BlockPos c2, int maxLit) {
		// Normalize corners to min/max
		this.corner1 = new BlockPos(
				Math.min(c1.getX(), c2.getX()),
				Math.min(c1.getY(), c2.getY()),
				Math.min(c1.getZ(), c2.getZ())
		);
		this.corner2 = new BlockPos(
				Math.max(c1.getX(), c2.getX()),
				Math.max(c1.getY(), c2.getY()),
				Math.max(c1.getZ(), c2.getZ())
		);
		this.maxLitBlocks = maxLit;
		this.configured = true;
		this.hasActiveRecipe = false;
		this.recipeOutput = ItemStack.EMPTY;
		setChanged();
	}

	public boolean isConfigured() {
		return configured;
	}

	public int getPlaneWidth() {
		return corner2.getX() - corner1.getX() + 1;
	}

	public int getPlaneDepth() {
		return corner2.getZ() - corner1.getZ() + 1;
	}

	public int getTotalBlocks() {
		return getPlaneWidth() * getPlaneDepth();
	}

	public boolean hasActiveRecipe() {
		return hasActiveRecipe;
	}

	public ItemStack getRecipeOutput() {
		return recipeOutput;
	}

	// --- Recipe lookup ---

	public LightPuzzleRecipe findRecipe(ItemStack input) {
		if (level == null) return null;
		List<LightPuzzleRecipe> recipes = LostDepthsModRecipeType.LIGHT_PUZZLE.get().getRecipes(level);
		for (LightPuzzleRecipe recipe : recipes) {
			if (recipe.matches(input)) return recipe;
		}
		return null;
	}

	/**
	 * Set the active recipe. Called when player right-clicks with valid input.
	 */
	public void setActiveRecipe(ItemStack output) {
		this.recipeOutput = output.copy();
		this.hasActiveRecipe = true;
		setChanged();
	}

	public void clearRecipe() {
		this.recipeOutput = ItemStack.EMPTY;
		this.hasActiveRecipe = false;
		setChanged();
	}

	// --- Plane operations ---

	/**
	 * Get all puzzle block positions within the configured plane.
	 */
	public List<BlockPos> getPuzzlePositions() {
		if (!configured || level == null) return Collections.emptyList();
		List<BlockPos> positions = new ArrayList<>();
		for (int x = corner1.getX(); x <= corner2.getX(); x++) {
			for (int z = corner1.getZ(); z <= corner2.getZ(); z++) {
				BlockPos pos = new BlockPos(x, corner1.getY(), z);
				Block block = level.getBlockState(pos).getBlock();
				if (block == LostdepthsModBlocks.LIGHT_PUZZLE_A.get() || block == LostdepthsModBlocks.LIGHT_PUZZLE_B.get()) {
					positions.add(pos);
				}
			}
		}
		return positions;
	}

	/**
	 * Set all puzzle blocks in the plane to LIT (B) state.
	 */
	public void setPlaneAllLit() {
		if (level == null || level.isClientSide()) return;
		List<BlockPos> positions = getPuzzlePositions();
		for (BlockPos pos : positions) {
			setPuzzleBlockState(pos, true);
		}
	}

	/**
	 * Set all puzzle blocks in the plane to OFF (A) state.
	 */
	public void setPlaneAllOff() {
		if (level == null || level.isClientSide()) return;
		List<BlockPos> positions = getPuzzlePositions();
		for (BlockPos pos : positions) {
			setPuzzleBlockState(pos, false);
		}
	}

	/**
	 * Randomize the plane: set exactly maxLitBlocks to LIT, rest to OFF.
	 * maxLitBlocks = -1: default (~2/3 of total lit)
	 * maxLitBlocks = 0: all OFF
	 * maxLitBlocks = total: all LIT
	 */
	public void randomizePlane() {
		if (level == null || level.isClientSide()) return;
		List<BlockPos> positions = getPuzzlePositions();
		if (positions.isEmpty()) return;

		int total = positions.size();
		int litCount;
		if (maxLitBlocks < 0) {
			// Default: ~2/3 of total
			litCount = Math.max(1, total * 2 / 3);
		} else {
			litCount = Math.min(maxLitBlocks, total);
		}

		// Set all to OFF first
		for (BlockPos pos : positions) {
			setPuzzleBlockState(pos, false);
		}

		// Randomly pick positions to turn ON
		if (litCount > 0) {
			List<BlockPos> shuffled = new ArrayList<>(positions);
			Collections.shuffle(shuffled, new java.util.Random(RandomSource.create().nextLong()));
			for (int i = 0; i < litCount; i++) {
				setPuzzleBlockState(shuffled.get(i), true);
			}
		}
	}

	/**
	 * Check if all puzzle blocks in the plane are LIT (status=true).
	 */
	public boolean isPuzzleSolved() {
		if (level == null) return false;
		List<BlockPos> positions = getPuzzlePositions();
		if (positions.isEmpty()) return false;

		for (BlockPos pos : positions) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be == null) return false;
			if (!be.getPersistentData().getBoolean("status")) return false;
		}
		return true;
	}

	/**
	 * Set a single puzzle block's state (status boolean + swap block type).
	 */
	private void setPuzzleBlockState(BlockPos pos, boolean lit) {
		if (level == null) return;
		BlockState currentState = level.getBlockState(pos);
		Block currentBlock = currentState.getBlock();
		Block targetBlock = lit ? LostdepthsModBlocks.LIGHT_PUZZLE_B.get() : LostdepthsModBlocks.LIGHT_PUZZLE_A.get();

		if (currentBlock != targetBlock) {
			// Preserve block entity data during swap
			BlockEntity existingBe = level.getBlockEntity(pos);
			CompoundTag savedData = null;
			if (existingBe != null) {
				savedData = existingBe.saveWithFullMetadata();
				existingBe.setRemoved();
			}
			level.setBlock(pos, targetBlock.defaultBlockState(), 3);
			if (savedData != null) {
				BlockEntity newBe = level.getBlockEntity(pos);
				if (newBe != null) {
					try {
						newBe.load(savedData);
					} catch (Exception ignored) {}
				}
			}
		}

		// Always update the persistent data
		BlockEntity be = level.getBlockEntity(pos);
		if (be != null) {
			be.getPersistentData().putBoolean("status", lit);
			BlockState bs = level.getBlockState(pos);
			level.sendBlockUpdated(pos, bs, bs, 3);
		}
	}

	// --- NBT ---

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putBoolean("Configured", configured);
		if (configured) {
			tag.putInt("C1X", corner1.getX());
			tag.putInt("C1Y", corner1.getY());
			tag.putInt("C1Z", corner1.getZ());
			tag.putInt("C2X", corner2.getX());
			tag.putInt("C2Y", corner2.getY());
			tag.putInt("C2Z", corner2.getZ());
			tag.putInt("MaxLit", maxLitBlocks);
		}
		tag.putBoolean("HasRecipe", hasActiveRecipe);
		if (hasActiveRecipe && !recipeOutput.isEmpty()) {
			tag.put("RecipeOutput", recipeOutput.save(new CompoundTag()));
		}
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		configured = tag.getBoolean("Configured");
		if (configured) {
			corner1 = new BlockPos(tag.getInt("C1X"), tag.getInt("C1Y"), tag.getInt("C1Z"));
			corner2 = new BlockPos(tag.getInt("C2X"), tag.getInt("C2Y"), tag.getInt("C2Z"));
			maxLitBlocks = tag.getInt("MaxLit");
		}
		hasActiveRecipe = tag.getBoolean("HasRecipe");
		if (hasActiveRecipe && tag.contains("RecipeOutput")) {
			recipeOutput = ItemStack.of(tag.getCompound("RecipeOutput"));
		} else {
			recipeOutput = ItemStack.EMPTY;
		}
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return saveWithFullMetadata();
	}
}
