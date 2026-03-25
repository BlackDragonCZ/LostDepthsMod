package cz.blackdragoncz.lostdepths.block.entity.storage;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.storage.NTPerformanceTracker;
import cz.blackdragoncz.lostdepths.storage.crafting.CraftingManager;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetwork;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetworkNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class NTControllerBlockEntity extends BlockEntity implements StorageNetworkNode {
	private static final int ENERGY_CAPACITY = 10000;
	private static final int MAX_RECEIVE = 1000;

	private final EnergyStorage energyStorage = new EnergyStorage(ENERGY_CAPACITY, MAX_RECEIVE, 0);
	private final LazyOptional<EnergyStorage> energyCap = LazyOptional.of(() -> energyStorage);

	private StorageNetwork network;
	private CraftingManager craftingManager;
	private UUID networkId;
	private boolean needsRebuild = true;
	private boolean redstoneDisabled = false;
	private boolean isSlave = false;
	private BlockPos masterPos = null;

	public NTControllerBlockEntity(BlockPos pos, BlockState state) {
		super(LostdepthsModBlockEntities.NT_CONTROLLER.get(), pos, state);
		this.networkId = UUID.randomUUID();
	}

	@Override
	public int getEnergyPerTick() {
		return 0; // controller's base cost is built into the network (1 FE/t)
	}

	public StorageNetwork getNetwork() {
		return network;
	}

	public boolean isNetworkActive() {
		return network != null && network.isActive();
	}

	public CraftingManager getCraftingManager() {
		return craftingManager;
	}

	public boolean isSlave() {
		return isSlave;
	}

	public UUID getNetworkId() {
		return networkId;
	}

	public int getStoredEnergy() {
		return energyStorage.getEnergyStored();
	}

	public int getEnergyCapacity() {
		return energyStorage.getMaxEnergyStored();
	}

	/**
	 * Mark network for rebuild on next tick (called when neighbors change).
	 */
	public void scheduleRebuild() {
		needsRebuild = true;
	}

	/**
	 * Server tick — consumes energy, manages network.
	 */
	/**
	 * Check if this controller should become a slave.
	 * If an adjacent controller exists without a bridge between them,
	 * the controller with the larger UUID becomes slave.
	 */
	private void checkSlaveStatus(Level level, BlockPos pos) {
		isSlave = false;
		masterPos = null;

		for (Direction dir : Direction.values()) {
			BlockPos neighborPos = pos.relative(dir);
			BlockEntity neighborBe = level.getBlockEntity(neighborPos);

			if (neighborBe instanceof NTControllerBlockEntity otherController) {
				// Two controllers adjacent — no bridge between them
				// The one with the larger UUID becomes slave
				if (networkId.compareTo(otherController.networkId) > 0) {
					isSlave = true;
					masterPos = neighborPos;
					return;
				}
			}

			// Check if connected through NT blocks (no bridge) to another controller
			// For now, only direct adjacency triggers slave mode
		}
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, NTControllerBlockEntity be) {
		long perfStart = NTPerformanceTracker.beginMeasure();

		// Check redstone
		be.redstoneDisabled = level.hasNeighborSignal(pos);

		// Check master/slave status
		if (be.needsRebuild) {
			be.checkSlaveStatus(level, pos);
		}

		// Slave mode — do nothing, wait for promotion
		if (be.isSlave) {
			if (be.network != null && be.network.isActive()) {
				be.network.setActive(false);
			}
			// Check if master is still alive — promote if not
			if (be.masterPos != null) {
				BlockEntity masterBe = level.getBlockEntity(be.masterPos);
				if (!(masterBe instanceof NTControllerBlockEntity)) {
					// Master gone — promote self
					be.isSlave = false;
					be.masterPos = null;
					be.needsRebuild = true;
				}
			}
			NTPerformanceTracker.endMeasure(perfStart);
			return;
		}

		// Rebuild network if needed
		if (be.needsRebuild) {
			be.needsRebuild = false;
			if (be.network == null) {
				be.network = new StorageNetwork(be.networkId, pos);
			}
			be.network.rebuild(level);
			if (be.craftingManager == null) {
				be.craftingManager = new CraftingManager(be.network);
			}
		}

		if (be.network == null) {
			NTPerformanceTracker.endMeasure(perfStart);
			return;
		}

		// Handle redstone toggle
		if (be.redstoneDisabled) {
			if (be.network.isActive()) {
				be.network.setActive(false);
			}
			NTPerformanceTracker.endMeasure(perfStart);
			return;
		}

		// Consume energy
		int required = be.network.getTotalEnergyPerTick();
		int available = be.energyStorage.getEnergyStored();

		if (available >= required) {
			be.energyStorage.extractEnergy(required, false);
			if (!be.network.isActive()) {
				be.network.setActive(true);
			}
		} else {
			// Not enough energy — network goes offline
			if (be.network.isActive()) {
				be.network.setActive(false);
			}
		}

		// Tick crafting manager
		if (be.craftingManager != null && be.network.isActive() && level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
			be.craftingManager.tick(serverLevel);
		}

		NTPerformanceTracker.endMeasure(perfStart);
	}

	// --- Neighbor change triggers rebuild ---

	public void onNeighborChanged() {
		scheduleRebuild();
	}

	// --- NBT ---

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("Energy")) {
			// Manually set energy since EnergyStorage doesn't have a load method
			int stored = tag.getInt("Energy");
			energyStorage.receiveEnergy(stored, false);
		}
		if (tag.hasUUID("NetworkId")) {
			networkId = tag.getUUID("NetworkId");
		}
		needsRebuild = true;
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putInt("Energy", energyStorage.getEnergyStored());
		tag.putUUID("NetworkId", networkId);
	}

	// --- Capabilities ---

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ENERGY) {
			return energyCap.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		energyCap.invalidate();
	}
}
