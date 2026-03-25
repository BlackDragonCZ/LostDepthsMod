package cz.blackdragoncz.lostdepths.storage.crafting;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTPatternProviderBlockEntity;
import cz.blackdragoncz.lostdepths.item.storage.EncodedPatternItem;
import cz.blackdragoncz.lostdepths.storage.network.NetworkStorageHelper;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.*;

/**
 * Represents a single autocrafting request.
 * Tracks state from collecting ingredients through crafting/processing to completion.
 */
public class CraftingTask {

	public enum State {
		PENDING,          // Waiting for sub-tasks to complete
		COLLECTING,       // Pulling ingredients from network
		CRAFTING,         // Internal crafting (crafting patterns)
		PUSHING,          // Pushing inputs to machine (processing patterns)
		WAITING_OUTPUT,   // Waiting for machine output (processing patterns)
		COMPLETE,         // Done, result available
		FAILED            // Missing ingredients or other error
	}

	private final UUID taskId;
	private final ItemStack pattern;
	private final ItemStack requestedOutput;
	private final int requestedCount;
	private final BlockPos providerPos;
	private State state;
	private int waitTicks = 0;
	private static final int MAX_WAIT_TICKS = 600; // 30 seconds timeout for processing

	// Collected ingredients
	private final List<ItemStack> collectedInputs = new ArrayList<>();
	// Sub-tasks for recursive crafting
	private final List<CraftingTask> subTasks = new ArrayList<>();

	public CraftingTask(ItemStack pattern, ItemStack requestedOutput, int count, BlockPos providerPos) {
		this.taskId = UUID.randomUUID();
		this.pattern = pattern.copy();
		this.requestedOutput = requestedOutput.copy();
		this.requestedCount = count;
		this.providerPos = providerPos;
		this.state = State.PENDING;
	}

	public UUID getTaskId() { return taskId; }
	public State getState() { return state; }
	public ItemStack getRequestedOutput() { return requestedOutput; }
	public int getRequestedCount() { return requestedCount; }
	public BlockPos getProviderPos() { return providerPos; }
	public List<CraftingTask> getSubTasks() { return subTasks; }

	public boolean isDone() {
		return state == State.COMPLETE || state == State.FAILED;
	}

	/**
	 * Tick this task forward. Called each server tick by CraftingManager.
	 */
	public void tick(StorageNetwork network, ServerLevel level) {
		switch (state) {
			case PENDING -> tickPending(network, level);
			case COLLECTING -> tickCollecting(network, level);
			case CRAFTING -> tickCrafting(network, level);
			case PUSHING -> tickPushing(network, level);
			case WAITING_OUTPUT -> tickWaitingOutput(network, level);
		}
	}

	private void tickPending(StorageNetwork network, ServerLevel level) {
		// Check if all sub-tasks are complete
		boolean allDone = true;
		for (CraftingTask sub : subTasks) {
			if (!sub.isDone()) {
				sub.tick(network, level);
				allDone = false;
			} else if (sub.getState() == State.FAILED) {
				state = State.FAILED;
				return;
			}
		}
		if (allDone) {
			state = State.COLLECTING;
		}
	}

	private void tickCollecting(StorageNetwork network, ServerLevel level) {
		List<ItemStack> inputs = EncodedPatternItem.getInputs(pattern);
		collectedInputs.clear();

		// For each batch (requestedCount / output count)
		List<ItemStack> outputs = EncodedPatternItem.getOutputs(pattern);
		int outputPerCraft = outputs.isEmpty() ? 1 : outputs.get(0).getCount();
		int batches = Math.max(1, (requestedCount + outputPerCraft - 1) / outputPerCraft);

		for (ItemStack input : inputs) {
			if (input.isEmpty()) continue;
			int needed = input.getCount() * batches;
			ItemStack extracted = NetworkStorageHelper.extract(network, level, input, needed);
			if (extracted.isEmpty() || extracted.getCount() < needed) {
				// Put back what we extracted
				if (!extracted.isEmpty()) {
					NetworkStorageHelper.insert(network, level, extracted, extracted.getCount());
				}
				// Put back all previously collected
				for (ItemStack collected : collectedInputs) {
					NetworkStorageHelper.insert(network, level, collected, collected.getCount());
				}
				collectedInputs.clear();
				state = State.FAILED;
				return;
			}
			collectedInputs.add(extracted);
		}

		// All ingredients collected
		if (EncodedPatternItem.isCraftingPattern(pattern)) {
			state = State.CRAFTING;
		} else {
			state = State.PUSHING;
		}
	}

	private void tickCrafting(StorageNetwork network, ServerLevel level) {
		// Internal crafting — instant for crafting patterns
		List<ItemStack> outputs = EncodedPatternItem.getOutputs(pattern);
		int outputPerCraft = outputs.isEmpty() ? 1 : outputs.get(0).getCount();
		int batches = Math.max(1, (requestedCount + outputPerCraft - 1) / outputPerCraft);

		// Validate recipe still exists
		String recipeId = EncodedPatternItem.getRecipeId(pattern);
		if (!recipeId.isEmpty()) {
			Optional<? extends Recipe<?>> recipe = level.getRecipeManager()
					.byKey(new net.minecraft.resources.ResourceLocation(recipeId));
			if (recipe.isEmpty()) {
				// Return ingredients
				returnCollectedToNetwork(network, level);
				state = State.FAILED;
				return;
			}
		}

		// Place result in provider's result buffer
		if (level.getBlockEntity(providerPos) instanceof NTPatternProviderBlockEntity provider) {
			for (ItemStack output : outputs) {
				if (output.isEmpty()) continue;
				ItemStack result = output.copy();
				result.setCount(output.getCount() * batches);

				// Try to put in result buffer
				for (int i = 0; i < provider.getResultBuffer().getSlots(); i++) {
					result = provider.getResultBuffer().insertItem(i, result, false);
					if (result.isEmpty()) break;
				}
				// Overflow goes directly to network
				if (!result.isEmpty()) {
					NetworkStorageHelper.insert(network, level, result, result.getCount());
				}
			}
		}

		collectedInputs.clear();
		state = State.COMPLETE;
	}

	private void tickPushing(StorageNetwork network, ServerLevel level) {
		// Push inputs to adjacent machine
		if (!(level.getBlockEntity(providerPos) instanceof NTPatternProviderBlockEntity provider)) {
			returnCollectedToNetwork(network, level);
			state = State.FAILED;
			return;
		}

		IItemHandler machineHandler = provider.getAdjacentHandler();
		if (machineHandler == null) {
			returnCollectedToNetwork(network, level);
			state = State.FAILED;
			return;
		}

		// Push all collected inputs into the machine
		for (ItemStack input : collectedInputs) {
			ItemStack remaining = ItemHandlerHelper.insertItemStacked(machineHandler, input.copy(), false);
			if (!remaining.isEmpty()) {
				// Machine full — return remainder to network
				NetworkStorageHelper.insert(network, level, remaining, remaining.getCount());
			}
		}

		collectedInputs.clear();
		waitTicks = 0;
		state = State.WAITING_OUTPUT;
	}

	private void tickWaitingOutput(StorageNetwork network, ServerLevel level) {
		waitTicks++;

		if (!(level.getBlockEntity(providerPos) instanceof NTPatternProviderBlockEntity provider)) {
			state = State.FAILED;
			return;
		}

		// Check if expected output appeared in adjacent machine's output
		IItemHandler machineHandler = provider.getAdjacentHandler();
		if (machineHandler != null) {
			List<ItemStack> expectedOutputs = EncodedPatternItem.getOutputs(pattern);
			for (ItemStack expected : expectedOutputs) {
				if (expected.isEmpty()) continue;

				// Scan machine slots for the expected output
				for (int slot = 0; slot < machineHandler.getSlots(); slot++) {
					ItemStack inSlot = machineHandler.getStackInSlot(slot);
					if (ItemStack.isSameItemSameTags(inSlot, expected)) {
						// Found it! Extract and put in result buffer
						ItemStack extracted = machineHandler.extractItem(slot, expected.getCount(), false);
						if (!extracted.isEmpty()) {
							for (int i = 0; i < provider.getResultBuffer().getSlots(); i++) {
								extracted = provider.getResultBuffer().insertItem(i, extracted, false);
								if (extracted.isEmpty()) break;
							}
							if (!extracted.isEmpty()) {
								NetworkStorageHelper.insert(network, level, extracted, extracted.getCount());
							}
							state = State.COMPLETE;
							return;
						}
					}
				}
			}
		}

		// Also check if expected output appeared in network (via import bus from different block)
		List<ItemStack> expectedOutputs = EncodedPatternItem.getOutputs(pattern);
		// This is handled by CraftingManager.onItemInserted() claiming items

		if (waitTicks >= MAX_WAIT_TICKS) {
			state = State.FAILED;
		}
	}

	/**
	 * Called by CraftingManager when an item enters the network.
	 * If this task is WAITING_OUTPUT and the item matches expected output, claim it.
	 * @return true if the item was claimed.
	 */
	public boolean tryClaimNetworkItem(ItemStack inserted, StorageNetwork network, ServerLevel level) {
		if (state != State.WAITING_OUTPUT) return false;

		List<ItemStack> expectedOutputs = EncodedPatternItem.getOutputs(pattern);
		for (ItemStack expected : expectedOutputs) {
			if (ItemStack.isSameItemSameTags(inserted, expected)) {
				// Claim it — put in result buffer
				if (level.getBlockEntity(providerPos) instanceof NTPatternProviderBlockEntity provider) {
					ItemStack toPut = inserted.copy();
					for (int i = 0; i < provider.getResultBuffer().getSlots(); i++) {
						toPut = provider.getResultBuffer().insertItem(i, toPut, false);
						if (toPut.isEmpty()) break;
					}
					state = State.COMPLETE;
					return true;
				}
			}
		}
		return false;
	}

	private void returnCollectedToNetwork(StorageNetwork network, ServerLevel level) {
		for (ItemStack collected : collectedInputs) {
			if (!collected.isEmpty()) {
				NetworkStorageHelper.insert(network, level, collected, collected.getCount());
			}
		}
		collectedInputs.clear();
	}

	/**
	 * Get required ingredients for this task (for UI display).
	 */
	public List<ItemStack> getRequiredIngredients() {
		List<ItemStack> inputs = EncodedPatternItem.getInputs(pattern);
		List<ItemStack> outputs = EncodedPatternItem.getOutputs(pattern);
		int outputPerCraft = outputs.isEmpty() ? 1 : outputs.get(0).getCount();
		int batches = Math.max(1, (requestedCount + outputPerCraft - 1) / outputPerCraft);

		List<ItemStack> required = new ArrayList<>();
		for (ItemStack input : inputs) {
			if (input.isEmpty()) continue;
			ItemStack scaled = input.copy();
			scaled.setCount(input.getCount() * batches);
			required.add(scaled);
		}
		return required;
	}
}
