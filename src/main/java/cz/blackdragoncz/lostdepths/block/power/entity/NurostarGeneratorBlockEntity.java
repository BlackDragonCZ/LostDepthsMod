package cz.blackdragoncz.lostdepths.block.power.entity;

import cz.blackdragoncz.lostdepths.block.entity.base.BaseEnergyBlockEntity;
import cz.blackdragoncz.lostdepths.energy.PowerCable;
import cz.blackdragoncz.lostdepths.energy.SyncedEnergyStorage;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.init.LostdepthsModEntities;
import cz.blackdragoncz.lostdepths.util.IEnergyAccessor;
import cz.blackdragoncz.lostdepths.util.NothingNullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.List;

@NothingNullByDefault
public class NurostarGeneratorBlockEntity extends BaseEnergyBlockEntity {

    public NurostarGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(LostdepthsModBlockEntities.NUROSTAR_GENRATOR.get(), pos, state);
    }

    @Override
    protected SyncedEnergyStorage createEnergyStorage() {
        return new SyncedEnergyStorage(this, 10000, 500, 500);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, NurostarGeneratorBlockEntity blockEntity) {
        List<BlockEntity> energyEntities = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            BlockEntity entity = level.getBlockEntity(blockPos.relative(direction));

            if (entity == null)
                continue;

            if (entity instanceof NurostarCableBlockEntity cableEntity) {
                PowerCable cable = cableEntity.getThisCable();

                if (cable == null)
                    continue;

                cable.collectEnergy(energyEntities);
            } else if (!(entity instanceof NurostarGeneratorBlockEntity)) {
                LazyOptional<IEnergyStorage> energyOpt = entity.getCapability(ForgeCapabilities.ENERGY);

                if (energyOpt.isPresent()) {
                    IEnergyStorage energyStorage = energyOpt.orElse(null);

                    if (energyStorage.canReceive() && !energyEntities.contains(entity)) {
                        energyEntities.add(entity);
                    }
                }
            }
        }

        int energyReceiversCount = energyEntities.size();

        int energyPerReceiver = 0;

        for (BlockEntity energyEntity : energyEntities) {
            if (energyEntity instanceof NurostarGeneratorBlockEntity) {
                continue;
            }

            LazyOptional<IEnergyStorage> energyOpt = energyEntity.getCapability(ForgeCapabilities.ENERGY);

            if (energyOpt.isPresent()) {
                IEnergyStorage energyStorage = energyOpt.orElse(null);

                if (energyPerReceiver == 0) {
                    //energyPerReceiver = blockEntity.energyStorage.extractEnergy(20, false) / energyReceiversCount;
                    energyPerReceiver = 100 / energyReceiversCount;
                }

                energyStorage.receiveEnergy(energyPerReceiver, false);
            }
        }
    }

}
