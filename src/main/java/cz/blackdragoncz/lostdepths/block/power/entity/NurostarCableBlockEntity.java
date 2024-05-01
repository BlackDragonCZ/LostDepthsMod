package cz.blackdragoncz.lostdepths.block.power.entity;

import com.mojang.logging.LogUtils;
import cz.blackdragoncz.lostdepths.block.power.PowerManager;
import cz.blackdragoncz.lostdepths.energy.ICableInterface;
import cz.blackdragoncz.lostdepths.energy.PowerCable;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class NurostarCableBlockEntity extends BlockEntity implements ICableInterface {

    private PowerCable thisCable;

    public NurostarCableBlockEntity(BlockPos pos, BlockState state) {
        super(LostdepthsModBlockEntities.NUROSTAR_CABLE.get(), pos, state);
    }

    public void updateEnergySides() {

        thisCable.clearForgeEnergies();
        thisCable.clearSideCables();

        for (Direction dir : Direction.values()) {
            BlockEntity sideEntity = level.getBlockEntity(worldPosition.relative(dir));

            if (sideEntity == null)
                continue;

            if (sideEntity instanceof NurostarCableBlockEntity sideCableEntity) {
                thisCable.setSideCable(dir, sideCableEntity);
                continue;
            }

            LazyOptional<IEnergyStorage> energyCapOpt = sideEntity.getCapability(ForgeCapabilities.ENERGY);

            if (!energyCapOpt.isPresent())
                continue;

            IEnergyStorage energyStorage = energyCapOpt.orElse(null);

            if (energyStorage.canReceive()) {
                thisCable.addForgeEnergy(sideEntity);
            }
        }
    }

    public PowerCable getThisCable() {
        return thisCable;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (!hasLevel())
            return;

        if (level.isClientSide())
            return;

        thisCable = PowerManager.INSTANCE.addCable(this);
        updateEnergySides();
    }

    @Override
    public void onChunkUnloaded() {
        if (hasLevel() & !level.isClientSide()) {
            PowerManager.INSTANCE.removeCable(this);
        }

        super.onChunkUnloaded();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        PowerManager.INSTANCE.removeCable(this);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void syncData(ServerPlayer player) {
        player.connection.send(getUpdatePacket());
    }

    public void syncData() {
        if (level == null || level.isClientSide) {
            return;
        }
        LevelChunk chunk = level.getChunkAt(getBlockPos());
        ((ServerChunkCache) level.getChunkSource()).chunkMap.getPlayers(chunk.getPos(), false).forEach(e -> e.connection.send(getUpdatePacket()));
    }

    @Override
    public PowerCable getPowerCable() {
        return thisCable;
    }
}