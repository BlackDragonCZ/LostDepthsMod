package cz.blackdragoncz.lostdepths.client.renderer.block;

import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public abstract class BlockEntityRenderBase<T extends BlockEntity> implements BlockEntityRenderer<T> {

    protected static final Logger LOGGER = LogUtils.getLogger();

    protected AbstractTexture getTexture(ResourceLocation location)
    {
        TextureManager manager = Minecraft.getInstance().getTextureManager();
        return manager.getTexture(location);
    }

    protected void endIfNeeded(MultiBufferSource renderer, @Nullable RenderType renderType) {
        //If we are not on fabulous, and we are a multi buffer source, then we manually end the render type to ensure it can render
        // behind translucent things properly. If we are on fabulous we can skip this as it will be ended at the proper time
        //Note: Ideally we wouldn't have to do this so that if two TERs happen to render consecutively with the same render type
        // then they can batch themselves properly together, but such is the limitations of MC's rendering system
        if (!Minecraft.useShaderTransparency() && renderer instanceof MultiBufferSource.BufferSource source) {
            if (renderType == null) {
                source.endLastBatch();
            } else {
                source.endBatch();
            }
        }
    }

    public static boolean isChunkLoaded(@Nullable LevelReader world, int chunkX, int chunkZ) {
        if (world == null) {
            return false;
        } else if (world instanceof LevelAccessor accessor && accessor.getChunkSource() instanceof ServerChunkCache serverChunkCache) {
            CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> future = serverChunkCache.getChunkFuture(chunkX, chunkZ, ChunkStatus.FULL, false);
            return future.isDone() && future.getNow(ChunkHolder.UNLOADED_CHUNK).left().isPresent();
        }
        return world.getChunk(chunkX, chunkZ, ChunkStatus.FULL, false) != null;
    }

    public static boolean isChunkLoaded(@Nullable LevelReader world, @NotNull BlockPos pos) {
        return isChunkLoaded(world, pos.getX() >> 4, pos.getZ() >> 4);
    }

    public static boolean isBlockLoaded(@Nullable BlockGetter world, @NotNull BlockPos pos) {
        if (world == null) {
            return false;
        } else if (world instanceof LevelReader reader) {
            if (reader instanceof Level level && !level.isInWorldBounds(pos)) {
                return false;
            }
            //TODO: If any cases come up where things are behaving oddly due to the change from reader.hasChunkAt(pos)
            // re-evaluate this and if the specific case is being handled properly
            return isChunkLoaded(reader, pos);
        }
        return true;
    }

    public static Optional<BlockState> getBlockState(@Nullable BlockGetter world, @NotNull BlockPos pos) {
        if (!isBlockLoaded(world, pos)) {
            //If the world is null, or it is a world reader and the block is not loaded, return empty
            return Optional.empty();
        }
        return Optional.of(world.getBlockState(pos));
    }

    public static float getRotation(Direction direction) {
        return switch (direction) {
            case NORTH -> 0;
            case EAST -> 270;

            case SOUTH -> 180;
            case WEST -> 90;
            default -> 0;
        };
    }

}
