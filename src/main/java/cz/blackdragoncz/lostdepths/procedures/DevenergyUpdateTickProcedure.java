package cz.blackdragoncz.lostdepths.procedures;

import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class DevenergyUpdateTickProcedure {
	private static final int PUSH_PER_SIDE = 100000;
	private static final int MAX_ENERGY = 10000000;
	private static final int REFILL_AMOUNT = 1000000;

	public static void execute(LevelAccessor world, double x, double y, double z) {
		BlockPos pos = BlockPos.containing(x, y, z);
		BlockEntity self = world.getBlockEntity(pos);
		if (self == null) return;

		// Keep the generator topped up (creative = infinite energy)
		self.getCapability(ForgeCapabilities.ENERGY).ifPresent(storage -> {
			while (storage.getEnergyStored() < MAX_ENERGY) {
				storage.receiveEnergy(REFILL_AMOUNT, false);
			}
		});

		// Push energy to all 6 sides
		for (Direction dir : Direction.values()) {
			BlockEntity neighbor = world.getBlockEntity(pos.relative(dir));
			if (neighbor == null) continue;

			neighbor.getCapability(ForgeCapabilities.ENERGY, dir.getOpposite()).ifPresent(neighborStorage -> {
				if (neighborStorage.canReceive()) {
					self.getCapability(ForgeCapabilities.ENERGY).ifPresent(selfStorage -> {
						int canExtract = selfStorage.extractEnergy(PUSH_PER_SIDE, true);
						if (canExtract > 0) {
							int accepted = neighborStorage.receiveEnergy(canExtract, false);
							if (accepted > 0) {
								selfStorage.extractEnergy(accepted, false);
							}
						}
					});
				}
			});
		}
	}
}
