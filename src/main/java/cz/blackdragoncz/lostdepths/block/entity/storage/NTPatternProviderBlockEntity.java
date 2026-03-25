package cz.blackdragoncz.lostdepths.block.entity.storage;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.item.storage.EncodedPatternItem;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetwork;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetworkNode;
import cz.blackdragoncz.lostdepths.world.inventory.NTPatternProviderMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Pattern Provider — holds encoded patterns, executes crafting/processing.
 * Tiered: T1=9, T2=27, expandable to 72 via KubeJS.
 * FACING property determines machine I/O direction for processing patterns.
 */
public class NTPatternProviderBlockEntity extends BlockEntity implements MenuProvider, StorageNetworkNode {

	private final int slotCount;
	private final ItemStackHandler patternSlots;
	private StorageNetwork network;
	private boolean validated = false;

	// Result buffer — holds finished crafting output items (first row)
	private final ItemStackHandler resultBuffer = new ItemStackHandler(9) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}
	};

	public NTPatternProviderBlockEntity(BlockPos pos, BlockState state, int slots) {
		super(slots <= 9 ? LostdepthsModBlockEntities.NT_PATTERN_PROVIDER.get()
				: LostdepthsModBlockEntities.NT_PATTERN_PROVIDER_T2.get(), pos, state);
		this.slotCount = slots;
		this.patternSlots = new ItemStackHandler(slots) {
			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				return stack.getItem() instanceof EncodedPatternItem;
			}

			@Override
			public int getSlotLimit(int slot) {
				return 1;
			}

			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
				validated = false; // re-validate on next check
			}
		};
	}

	public static NTPatternProviderBlockEntity createTier2(BlockPos pos, BlockState state) {
		return new NTPatternProviderBlockEntity(pos, state, 27);
	}

	public int getSlotCount() {
		return slotCount;
	}

	public ItemStackHandler getPatternSlots() {
		return patternSlots;
	}

	public ItemStackHandler getResultBuffer() {
		return resultBuffer;
	}

	/**
	 * Get all valid encoded patterns in this provider.
	 */
	public List<ItemStack> getValidPatterns() {
		List<ItemStack> patterns = new ArrayList<>();
		for (int i = 0; i < slotCount; i++) {
			ItemStack stack = patternSlots.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() instanceof EncodedPatternItem && !EncodedPatternItem.isInvalid(stack)) {
				patterns.add(stack);
			}
		}
		return patterns;
	}

	/**
	 * Find a pattern that produces the requested output item.
	 */
	@Nullable
	public ItemStack findPatternFor(ItemStack output) {
		for (int i = 0; i < slotCount; i++) {
			ItemStack pattern = patternSlots.getStackInSlot(i);
			if (pattern.isEmpty() || EncodedPatternItem.isInvalid(pattern)) continue;

			List<ItemStack> outputs = EncodedPatternItem.getOutputs(pattern);
			for (ItemStack out : outputs) {
				if (ItemStack.isSameItemSameTags(out, output)) return pattern;
			}
		}
		return null;
	}

	/**
	 * Get the facing direction for machine I/O.
	 */
	public Direction getFacing() {
		BlockState state = getBlockState();
		return state.hasProperty(BlockStateProperties.FACING)
				? state.getValue(BlockStateProperties.FACING) : Direction.NORTH;
	}

	/**
	 * Get the adjacent machine's IItemHandler (for processing patterns).
	 */
	@Nullable
	public IItemHandler getAdjacentHandler() {
		if (level == null) return null;
		Direction facing = getFacing();
		BlockPos targetPos = worldPosition.relative(facing);
		BlockEntity targetBe = level.getBlockEntity(targetPos);
		if (targetBe == null || targetBe instanceof StorageNetworkNode) return null;
		return targetBe.getCapability(ForgeCapabilities.ITEM_HANDLER, facing.getOpposite()).orElse(null);
	}

	/**
	 * Validate all crafting patterns against current recipe manager.
	 * Called on world load and when patterns change.
	 */
	public void validatePatterns() {
		if (level == null) return;
		RecipeManager recipeManager = level.getRecipeManager();
		for (int i = 0; i < slotCount; i++) {
			ItemStack pattern = patternSlots.getStackInSlot(i);
			if (pattern.isEmpty() || !(pattern.getItem() instanceof EncodedPatternItem)) continue;

			if (EncodedPatternItem.isCraftingPattern(pattern)) {
				if (!EncodedPatternItem.validateCraftingPattern(pattern, recipeManager)) {
					String recipeId = EncodedPatternItem.getRecipeId(pattern);
					EncodedPatternItem.markInvalid(pattern, "Recipe removed or changed: " + recipeId);
				} else {
					EncodedPatternItem.clearInvalid(pattern);
				}
			}
		}
		validated = true;
		setChanged();
	}

	public void dropContents(Level level, BlockPos pos) {
		for (int i = 0; i < slotCount; i++) {
			ItemStack stack = patternSlots.getStackInSlot(i);
			if (!stack.isEmpty()) {
				Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
			}
		}
		for (int i = 0; i < resultBuffer.getSlots(); i++) {
			ItemStack stack = resultBuffer.getStackInSlot(i);
			if (!stack.isEmpty()) {
				Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
			}
		}
	}

	@Override
	public void onJoinNetwork(StorageNetwork network) {
		this.network = network;
		validated = false; // re-validate when joining
	}

	@Override
	public void onLeaveNetwork() {
		this.network = null;
	}

	@Override
	public int getEnergyPerTick() {
		return 3;
	}

	public StorageNetwork getNetwork() {
		return network;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, NTPatternProviderBlockEntity provider) {
		// Validate patterns on first tick after load or change
		if (!provider.validated) {
			provider.validatePatterns();
		}

		// Push result buffer items back into network
		if (provider.network != null && provider.network.isActive() && level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
			for (int i = 0; i < provider.resultBuffer.getSlots(); i++) {
				ItemStack result = provider.resultBuffer.getStackInSlot(i);
				if (result.isEmpty()) continue;

				int notInserted = cz.blackdragoncz.lostdepths.storage.network.NetworkStorageHelper.insert(
						provider.network, serverLevel, result, result.getCount());
				if (notInserted < result.getCount()) {
					if (notInserted == 0) {
						provider.resultBuffer.setStackInSlot(i, ItemStack.EMPTY);
					} else {
						result.setCount(notInserted);
					}
					provider.setChanged();
				}
			}
		}
	}

	// --- NBT ---

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.put("Patterns", patternSlots.serializeNBT());
		tag.put("ResultBuffer", resultBuffer.serializeNBT());
		tag.putInt("SlotCount", slotCount);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("Patterns")) patternSlots.deserializeNBT(tag.getCompound("Patterns"));
		if (tag.contains("ResultBuffer")) resultBuffer.deserializeNBT(tag.getCompound("ResultBuffer"));
		validated = false;
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("Pattern Provider");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player) {
		return new NTPatternProviderMenu(containerId, playerInv, this);
	}
}
