package cz.blackdragoncz.lostdepths.block.creative.entity;

import cz.blackdragoncz.lostdepths.disruptor.GravatorDisruptorManager;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Locale;

// Creative-only RP block: grounds everyone in range. NORMAL revokes flight abilities and levitation, which covers this mod
// and most angel rings; STRICT also clamps upward motion, the only thing that stops velocity-driven jetpacks.
public class GravatorDisruptorBlockEntity extends BlockEntity {

    public enum Mode {
        NORMAL, STRICT;

        public static Mode byName(String name) {
            return "strict".equals(name) ? STRICT : NORMAL;
        }
    }

    private int range = 5;
    private Mode mode = Mode.NORMAL;
    private int tickCounter;

    public GravatorDisruptorBlockEntity(BlockPos pos, BlockState state) {
        super(LostdepthsModBlockEntities.GRAVATOR_DISRUPTOR.get(), pos, state);
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = Math.max(1, Math.min(256, range));
        setChanged();
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        setChanged();
    }

    public void serverTick() {
        if (level == null || level.isClientSide)
            return;
        // NORMAL is an inert state: the block is placed and configurable but grounds nobody, so it never registers.
        if (mode == Mode.NORMAL)
            return;
        if (++tickCounter < 5)
            return;
        tickCounter = 0;

        BlockPos pos = getBlockPos();
        for (ServerPlayer player : ((ServerLevel) level).getEntitiesOfClass(ServerPlayer.class, new AABB(pos).inflate(range))) {
            double distSq = player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            if (distSq <= (double) range * range)
                GravatorDisruptorManager.offer(player, pos, mode);
        }
    }

    public void onRemoved() {
        if (level != null && !level.isClientSide)
            GravatorDisruptorManager.removeDisruptor(getBlockPos());
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("gravator_range", range);
        tag.putString("gravator_mode", mode.name());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("gravator_range"))
            range = tag.getInt("gravator_range");
        if (tag.contains("gravator_mode"))
            mode = Mode.byName(tag.getString("gravator_mode").toLowerCase(Locale.ROOT));
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
