package cz.blackdragoncz.lostdepths.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.EnergyStorage;

public class SyncedEnergyStorage extends EnergyStorage {

    private final BlockEntity blockEntity;

    public SyncedEnergyStorage(BlockEntity blockEntity, int capacity) {
        super(capacity);
        this.blockEntity = blockEntity;
    }

    public SyncedEnergyStorage(BlockEntity blockEntity, int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
        this.blockEntity = blockEntity;
    }

    public SyncedEnergyStorage(BlockEntity blockEntity, int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
        this.blockEntity = blockEntity;
    }

    public SyncedEnergyStorage(BlockEntity blockEntity, int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
        this.blockEntity = blockEntity;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int val = super.receiveEnergy(maxReceive, simulate);

        if (!simulate)
            updateClients();

        return val;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int val = super.extractEnergy(maxExtract, simulate);

        if (!simulate)
            updateClients();

        return val;
    }

    private void updateClients() {
        Level level = blockEntity.getLevel();

        if (level == null)
            return;

        BlockPos blockPos = blockEntity.getBlockPos();;
        BlockState state = level.getBlockState(blockPos);
        blockEntity.setChanged();
        blockEntity.getLevel().sendBlockUpdated(blockPos, state, state, Block.UPDATE_CLIENTS);
    }
}
