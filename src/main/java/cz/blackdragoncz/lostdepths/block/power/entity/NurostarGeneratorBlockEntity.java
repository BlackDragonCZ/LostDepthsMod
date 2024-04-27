package cz.blackdragoncz.lostdepths.block.power.entity;

import cz.blackdragoncz.lostdepths.block.entity.base.BaseEnergyBlockEntity;
import cz.blackdragoncz.lostdepths.energy.SyncedEnergyStorage;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.init.LostdepthsModEntities;
import cz.blackdragoncz.lostdepths.util.IEnergyAccessor;
import cz.blackdragoncz.lostdepths.util.NothingNullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.EnergyStorage;

@NothingNullByDefault
public class NurostarGeneratorBlockEntity extends BaseEnergyBlockEntity {

    public NurostarGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(LostdepthsModBlockEntities.NUROSTAR_GENRATOR.get(), pos, state);
    }

    @Override
    protected SyncedEnergyStorage createEnergyStorage() {
        return new SyncedEnergyStorage(this, 10000, 500, 500);
    }

}
