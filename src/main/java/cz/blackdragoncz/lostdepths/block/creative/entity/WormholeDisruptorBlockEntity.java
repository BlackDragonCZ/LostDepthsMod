package cz.blackdragoncz.lostdepths.block.creative.entity;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.warp.WormholeDisruptorManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class WormholeDisruptorBlockEntity extends BlockEntity {

    private int range = 32;
    private int tickCounter = 0;

    public WormholeDisruptorBlockEntity(BlockPos pos, BlockState state) {
        super(LostdepthsModBlockEntities.WORMHOLE_DISRUPTOR.get(), pos, state);
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = Math.max(1, Math.min(256, range));
        setChanged();
    }

    public void serverTick() {
        if (level == null || level.isClientSide) return;

        tickCounter++;
        if (tickCounter < 10) return; // Check every 0.5 seconds
        tickCounter = 0;

        ServerLevel serverLevel = (ServerLevel) level;
        BlockPos pos = getBlockPos();
        AABB area = new AABB(pos).inflate(range);

        List<ServerPlayer> players = serverLevel.getEntitiesOfClass(ServerPlayer.class, area);
        for (ServerPlayer player : players) {
            if (player.blockPosition().closerThan(pos, range)) {
                WormholeDisruptorManager.addAffectedPlayer(player, pos);
            }
        }
    }

    public void onRemoved() {
        if (level != null && !level.isClientSide) {
            WormholeDisruptorManager.removeDisruptor(getBlockPos());
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("wormhole_range", range);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("wormhole_range")) {
            range = tag.getInt("wormhole_range");
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
