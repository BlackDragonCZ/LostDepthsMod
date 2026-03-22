package cz.blackdragoncz.lostdepths.storage.network;

/**
 * Marker interface for block entities that are part of a NuroTech storage network.
 * Implement this on all NT block entities so the network BFS can discover them.
 */
public interface StorageNetworkNode {

	/**
	 * Energy consumed by this component per tick (FE/t).
	 */
	default int getEnergyPerTick() {
		return 1;
	}

	/**
	 * Called when this component is added to a network.
	 */
	default void onJoinNetwork(StorageNetwork network) {}

	/**
	 * Called when this component is removed from a network (or network is disbanded).
	 */
	default void onLeaveNetwork() {}
}
