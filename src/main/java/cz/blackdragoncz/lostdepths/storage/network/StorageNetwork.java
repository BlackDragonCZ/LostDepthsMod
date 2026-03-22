package cz.blackdragoncz.lostdepths.storage.network;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;

/**
 * Represents a single NuroTech storage network.
 * The controller block is the root — all other NT blocks must be
 * connected face-to-face (adjacent on any side) to be part of the network.
 */
public class StorageNetwork {
	private final UUID networkId;
	private final BlockPos controllerPos;
	private final Set<BlockPos> components = new HashSet<>();
	private boolean active = true;
	private int totalEnergyPerTick = 1;

	public StorageNetwork(UUID networkId, BlockPos controllerPos) {
		this.networkId = networkId;
		this.controllerPos = controllerPos;
		this.components.add(controllerPos);
	}

	public UUID getNetworkId() {
		return networkId;
	}

	public BlockPos getControllerPos() {
		return controllerPos;
	}

	public Set<BlockPos> getComponents() {
		return Collections.unmodifiableSet(components);
	}

	public int getComponentCount() {
		return components.size();
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Total energy consumption per tick across all components.
	 */
	public int getTotalEnergyPerTick() {
		return totalEnergyPerTick;
	}

	/**
	 * Rebuilds the network by BFS scanning face-to-face neighbors from the controller.
	 * Only block entities implementing StorageNetworkNode are considered.
	 * Notifies components via onJoinNetwork/onLeaveNetwork.
	 */
	public void rebuild(Level level) {
		// Notify old components they're leaving
		for (BlockPos pos : components) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof StorageNetworkNode node) {
				node.onLeaveNetwork();
			}
		}

		components.clear();
		totalEnergyPerTick = 1; // base cost for controller

		if (controllerPos == null) return;

		Queue<BlockPos> queue = new LinkedList<>();
		queue.add(controllerPos);
		components.add(controllerPos);

		while (!queue.isEmpty()) {
			BlockPos current = queue.poll();
			for (BlockPos neighbor : getNeighbors(current)) {
				if (components.contains(neighbor)) continue;
				BlockEntity be = level.getBlockEntity(neighbor);
				// Skip other controllers — they form their own networks
				if (be instanceof NTControllerBlockEntity) continue;
				if (be instanceof StorageNetworkNode) {
					components.add(neighbor);
					queue.add(neighbor);
				}
			}
		}

		// Calculate total energy and notify new components
		for (BlockPos pos : components) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof StorageNetworkNode node) {
				totalEnergyPerTick += node.getEnergyPerTick();
				node.onJoinNetwork(this);
			}
		}
	}

	/**
	 * Check if a position is part of this network.
	 */
	public boolean contains(BlockPos pos) {
		return components.contains(pos);
	}

	private static BlockPos[] getNeighbors(BlockPos pos) {
		return new BlockPos[]{
				pos.above(), pos.below(),
				pos.north(), pos.south(),
				pos.east(), pos.west()
		};
	}

	/**
	 * Scans from a given position through connected NT blocks (BFS) to find a controller.
	 * If found, tells it to rebuild. Called from any NT block's neighborChanged.
	 * Limited to 128 blocks to prevent runaway scans.
	 */
	public static void notifyControllerNearby(Level level, BlockPos origin) {
		if (level.isClientSide()) return;

		Set<BlockPos> visited = new HashSet<>();
		Queue<BlockPos> queue = new LinkedList<>();
		queue.add(origin);
		visited.add(origin);

		int limit = 128;
		while (!queue.isEmpty() && limit-- > 0) {
			BlockPos current = queue.poll();
			BlockEntity be = level.getBlockEntity(current);

			if (be instanceof NTControllerBlockEntity controller) {
				controller.scheduleRebuild();
				return;
			}

			if (be instanceof StorageNetworkNode || current.equals(origin)) {
				for (BlockPos neighbor : getNeighbors(current)) {
					if (!visited.contains(neighbor)) {
						visited.add(neighbor);
						queue.add(neighbor);
					}
				}
			}
		}
	}
}
