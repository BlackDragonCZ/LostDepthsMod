package cz.blackdragoncz.lostdepths.block.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class MultiblockHelper {

    /**
     * Rotates a BlockPos offset from NORTH-facing basis to the given facing direction.
     */
    public static BlockPos rotateOffset(BlockPos offset, Direction facing) {
        return switch (facing) {
            case NORTH -> offset;
            case SOUTH -> new BlockPos(-offset.getX(), offset.getY(), -offset.getZ());
            case EAST -> new BlockPos(-offset.getZ(), offset.getY(), offset.getX());
            case WEST -> new BlockPos(offset.getZ(), offset.getY(), -offset.getX());
            default -> offset;
        };
    }

    /**
     * Returns all world positions for dummy blocks given the controller position, facing, and base offsets.
     */
    public static List<BlockPos> getPartPositions(BlockPos controllerPos, Direction facing, List<BlockPos> baseOffsets) {
        List<BlockPos> positions = new ArrayList<>();
        for (BlockPos offset : baseOffsets) {
            positions.add(controllerPos.offset(rotateOffset(offset, facing)));
        }
        return positions;
    }

    /**
     * Checks if all required positions are free (air or replaceable).
     * Returns true if placement is allowed.
     */
    public static boolean canPlace(Level level, BlockPos controllerPos, Direction facing, List<BlockPos> baseOffsets) {
        for (BlockPos pos : getPartPositions(controllerPos, facing, baseOffsets)) {
            BlockState state = level.getBlockState(pos);
            if (!state.canBeReplaced()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Attempts placement. If not enough space, sends a message to the player and returns false.
     */
    public static boolean tryPlace(Level level, BlockPos controllerPos, Direction facing, List<BlockPos> baseOffsets, Player player, Block dummyBlock) {
        if (!canPlace(level, controllerPos, facing, baseOffsets)) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("message.lostdepths.not_enough_space"), true);
            }
            return false;
        }

        for (BlockPos pos : getPartPositions(controllerPos, facing, baseOffsets)) {
            level.setBlock(pos, dummyBlock.defaultBlockState(), 3);
            if (level.getBlockEntity(pos) instanceof MultiblockDummyBlockEntity dummy) {
                dummy.setControllerPos(controllerPos);
            }
        }
        return true;
    }

    /**
     * Removes all dummy blocks associated with this controller.
     */
    public static void removeParts(Level level, BlockPos controllerPos, Direction facing, List<BlockPos> baseOffsets) {
        for (BlockPos pos : getPartPositions(controllerPos, facing, baseOffsets)) {
            if (level.getBlockState(pos).getBlock() instanceof MultiblockDummyBlock) {
                level.removeBlock(pos, false);
            }
        }
    }
}
