package cz.blackdragoncz.lostdepths.block.entity.storage;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.storage.NTPerformanceTracker;
import cz.blackdragoncz.lostdepths.storage.data.CrystalInventory;
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
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

/**
 * Pushes items from the NT network into an adjacent inventory.
 * Base speed: 1 item per second (20 ticks).
 * Exports the first available item type from the network.
 * TODO: Add filter slots to control what gets exported.
 */
public class NTExportBusBlockEntity extends BlockEntity implements StorageNetworkNode {
	private static final int BASE_INTERVAL = 20;

	private StorageNetwork network;
	private int tickCounter = 0;

	public NTExportBusBlockEntity(BlockPos pos, BlockState state) {
		super(LostdepthsModBlockEntities.NT_EXPORT_BUS.get(), pos, state);
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

	public static void serverTick(Level level, BlockPos pos, BlockState state, NTExportBusBlockEntity be) {
		if (be.network == null || !be.network.isActive()) return;
		if (!(level instanceof ServerLevel serverLevel)) return;

		long perfStart = NTPerformanceTracker.beginMeasure();

		be.tickCounter++;
		if (be.tickCounter < BASE_INTERVAL) {
			NTPerformanceTracker.endMeasure(perfStart);
			return;
		}
		be.tickCounter = 0;

		Direction facing = state.hasProperty(BlockStateProperties.FACING) ?
				state.getValue(BlockStateProperties.FACING) : Direction.UP;

		BlockPos targetPos = pos.relative(facing);
		BlockEntity targetBe = level.getBlockEntity(targetPos);
		if (targetBe == null) {
			NTPerformanceTracker.endMeasure(perfStart);
			return;
		}

		if (targetBe instanceof StorageNetworkNode) {
			NTPerformanceTracker.endMeasure(perfStart);
			return;
		}

		targetBe.getCapability(ForgeCapabilities.ITEM_HANDLER, facing.getOpposite()).ifPresent(handler -> {
			exportToHandler(handler, be.network, serverLevel);
		});

		NTPerformanceTracker.endMeasure(perfStart);
	}

	private static void exportToHandler(IItemHandler handler, StorageNetwork network, ServerLevel level) {
		// Get what's in the network and try to push the first available type
		List<CrystalInventory.StoredItem> items = NetworkStorageHelper.getAggregatedItems(network, level);

		for (CrystalInventory.StoredItem stored : items) {
			int toExport = Math.min(stored.count, stored.template.getMaxStackSize());

			// Check if target can accept this item
			ItemStack testStack = stored.template.copy();
			testStack.setCount(toExport);
			ItemStack remainder = ItemHandlerHelper.insertItemStacked(handler, testStack, true);
			int canAccept = toExport - remainder.getCount();

			if (canAccept > 0) {
				// Extract from network and insert into target
				ItemStack extracted = NetworkStorageHelper.extract(network, level, stored.template, canAccept);
				if (!extracted.isEmpty()) {
					ItemHandlerHelper.insertItemStacked(handler, extracted, false);
					return; // one operation per tick
				}
			}
		}
	}
}
