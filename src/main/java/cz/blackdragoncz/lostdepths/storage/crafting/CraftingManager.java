package cz.blackdragoncz.lostdepths.storage.crafting;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTPatternProviderBlockEntity;
import cz.blackdragoncz.lostdepths.item.storage.EncodedPatternItem;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Manages autocrafting tasks for a single NuroTech network.
 * Lives on the controller, ticked each server tick.
 */
public class CraftingManager {

	private final StorageNetwork network;
	private final List<CraftingTask> activeTasks = new ArrayList<>();
	private static final int MAX_CONCURRENT_TASKS = 16;

	public CraftingManager(StorageNetwork network) {
		this.network = network;
	}

	/**
	 * Request a craft. Returns the task if queued, or null if no pattern found.
	 */
	@Nullable
	public CraftingTask requestCraft(ItemStack output, int count, ServerLevel level) {
		// Find a pattern provider that can make this item
		for (BlockPos pos : network.getComponents()) {
			BlockEntity be = level.getBlockEntity(pos);
			if (!(be instanceof NTPatternProviderBlockEntity provider)) continue;

			ItemStack pattern = provider.findPatternFor(output);
			if (pattern != null) {
				CraftingTask task = new CraftingTask(pattern, output, count, pos);

				// Check for recursive sub-crafts
				buildSubTasks(task, level);

				activeTasks.add(task);
				return task;
			}
		}
		return null;
	}

	/**
	 * Build recursive sub-tasks for missing ingredients.
	 */
	private void buildSubTasks(CraftingTask task, ServerLevel level) {
		List<ItemStack> required = task.getRequiredIngredients();
		for (ItemStack ingredient : required) {
			// Check if we have enough in network
			int available = countInNetwork(ingredient, level);
			if (available < ingredient.getCount()) {
				int missing = ingredient.getCount() - available;
				// Check if this ingredient can be crafted
				CraftingTask subTask = requestCraft(ingredient, missing, level);
				if (subTask != null) {
					task.getSubTasks().add(subTask);
					// Remove from active (it's managed as a sub-task)
					activeTasks.remove(subTask);
				}
				// If no pattern exists for ingredient, the parent task will fail at COLLECTING
			}
		}
	}

	/**
	 * Count how many of an item are available in the network.
	 */
	private int countInNetwork(ItemStack match, ServerLevel level) {
		var items = cz.blackdragoncz.lostdepths.storage.network.NetworkStorageHelper.getAggregatedItems(network, level);
		for (var item : items) {
			if (ItemStack.isSameItemSameTags(item.template, match)) {
				return item.count;
			}
		}
		return 0;
	}

	/**
	 * Tick all active tasks. Called from controller's serverTick.
	 */
	public void tick(ServerLevel level) {
		if (!network.isActive()) return;

		Iterator<CraftingTask> it = activeTasks.iterator();
		while (it.hasNext()) {
			CraftingTask task = it.next();
			task.tick(network, level);
			if (task.isDone()) {
				it.remove();
			}
		}
	}

	/**
	 * Called when an item is inserted into the network (via any source).
	 * Processing tasks waiting for output can claim it.
	 * @return true if the item was claimed by a task.
	 */
	public boolean onItemInserted(ItemStack inserted, ServerLevel level) {
		for (CraftingTask task : activeTasks) {
			if (task.tryClaimNetworkItem(inserted, network, level)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Find a pattern anywhere on the network that can produce this item.
	 */
	@Nullable
	public ItemStack findPattern(ItemStack output, ServerLevel level) {
		for (BlockPos pos : network.getComponents()) {
			BlockEntity be = level.getBlockEntity(pos);
			if (!(be instanceof NTPatternProviderBlockEntity provider)) continue;
			ItemStack pattern = provider.findPatternFor(output);
			if (pattern != null) return pattern;
		}
		return null;
	}

	/**
	 * Check if an item is craftable on this network.
	 */
	public boolean isCraftable(ItemStack output, ServerLevel level) {
		return findPattern(output, level) != null;
	}

	public List<CraftingTask> getActiveTasks() {
		return Collections.unmodifiableList(activeTasks);
	}

	public int getActiveTaskCount() {
		return activeTasks.size();
	}

	public boolean canAcceptTask() {
		return activeTasks.size() < MAX_CONCURRENT_TASKS;
	}
}
