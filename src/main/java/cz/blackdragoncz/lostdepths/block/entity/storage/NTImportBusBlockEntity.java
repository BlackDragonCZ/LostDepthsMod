package cz.blackdragoncz.lostdepths.block.entity.storage;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.storage.NTPerformanceTracker;
import cz.blackdragoncz.lostdepths.storage.network.NetworkStorageHelper;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetwork;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetworkNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

/**
 * Pulls items from an adjacent inventory into the NT network.
 * Base speed: 1 item per second (20 ticks).
 */
public class NTImportBusBlockEntity extends BlockEntity implements StorageNetworkNode {
	private static final int BASE_INTERVAL = 20; // ticks between operations

	private StorageNetwork network;
	private int tickCounter = 0;

	public NTImportBusBlockEntity(BlockPos pos, BlockState state) {
		super(LostdepthsModBlockEntities.NT_IMPORT_BUS.get(), pos, state);
	}

	@Override
	public void onJoinNetwork(StorageNetwork network) {
		this.network = network;
	}

	@Override
	public void onLeaveNetwork() {
		this.network = null;
	}

	@Override
	public int getEnergyPerTick() {
		return 2;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, NTImportBusBlockEntity be) {
		if (be.network == null || !be.network.isActive()) return;
		if (!(level instanceof ServerLevel serverLevel)) return;

		long perfStart = NTPerformanceTracker.beginMeasure();

		be.tickCounter++;
		if (be.tickCounter < BASE_INTERVAL) {
			NTPerformanceTracker.endMeasure(perfStart);
			return;
		}
		be.tickCounter = 0;

		// Find the facing direction — pull from that side
		Direction facing = state.hasProperty(BlockStateProperties.FACING) ?
				state.getValue(BlockStateProperties.FACING) : Direction.UP;

		BlockPos targetPos = pos.relative(facing);
		BlockEntity targetBe = level.getBlockEntity(targetPos);
		if (targetBe == null) {
			NTPerformanceTracker.endMeasure(perfStart);
			return;
		}

		// Skip if target is an NT component (don't pull from own network)
		if (targetBe instanceof StorageNetworkNode) {
			NTPerformanceTracker.endMeasure(perfStart);
			return;
		}

		targetBe.getCapability(ForgeCapabilities.ITEM_HANDLER, facing.getOpposite()).ifPresent(handler -> {
			importFromHandler(handler, be.network, serverLevel);
		});

		NTPerformanceTracker.endMeasure(perfStart);
	}

	private static void importFromHandler(IItemHandler handler, StorageNetwork network, ServerLevel level) {
		for (int slot = 0; slot < handler.getSlots(); slot++) {
			ItemStack inSlot = handler.getStackInSlot(slot);
			if (inSlot.isEmpty()) continue;

			// Try to extract 1 stack (up to 64)
			ItemStack simulated = handler.extractItem(slot, inSlot.getMaxStackSize(), true);
			if (simulated.isEmpty()) continue;

			int notInserted = NetworkStorageHelper.insert(network, level, simulated, simulated.getCount());
			int actuallyInserted = simulated.getCount() - notInserted;

			if (actuallyInserted > 0) {
				handler.extractItem(slot, actuallyInserted, false);
				return; // one operation per tick
			}
		}
	}
}
