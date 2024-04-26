package cz.blackdragoncz.lostdepths.block.power.entity;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.EnumSet;

public class NurostarCableBlockEntity extends BlockEntity {
    public NurostarCableBlockEntity(BlockPos pos, BlockState state) {
        super(LostdepthsModBlockEntities.NUROSTAR_CABLE.get(), pos, state);
    }

    public final EnumSet<Direction> energySides = EnumSet.noneOf(Direction.class);

    protected MutableBoolean netInsertionGuard = new MutableBoolean(false);
    protected int startIndex = 0;

    public boolean isActive() {
        if (getLevel() instanceof ServerLevel serverLevel) {
            return serverLevel.getChunkSource().isPositionTicking(ChunkPos.asLong(getBlockPos()));
        }
        return false;
    }

    private static byte getSideMask(Direction side) {
        return (byte) (1 << side.ordinal());
    }

}