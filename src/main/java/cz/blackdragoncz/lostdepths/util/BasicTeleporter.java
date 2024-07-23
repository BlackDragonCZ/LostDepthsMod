package cz.blackdragoncz.lostdepths.util;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

import java.util.Optional;

public class BasicTeleporter implements ITeleporter {

    private final Long2ObjectMap<PortalPosition> destinationCoordinateCaChe = new Long2ObjectOpenHashMap<>(4096);
    private final ServerLevel world;
    private final double goToX;
    private final double goToY;
    private final double goToZ;
    private final boolean strict;

    public BasicTeleporter(ServerLevel world, double xpos, double ypos, double zpos) {
        this(world, xpos, ypos, zpos, false);
    }

    public BasicTeleporter(ServerLevel world, double xpos, double ypos, double zpos, boolean strictSet) {
        this.world = world;
        this.goToX = xpos;
        this.goToY = ypos;
        this.goToZ = zpos;
        this.strict = strictSet;
    }

    private static class PortalPosition {
        final BlockPos pos;
        final long lastUpdateTime;

        PortalPosition(BlockPos pos, Long lastUpdateTime) {
            this.pos = pos;
            this.lastUpdateTime = lastUpdateTime;
        }
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        return repositionEntity.apply(false);
    }

    @Override
    public Optional<PortalInfo> getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        if (destWorld.dimension() == Level.OVERWORLD) {
            return Optional.of(new PortalInfo(new Vec3(goToX, goToY, goToZ), Vec3.ZERO, entity.getYRot(), entity.getXRot()));
        } else if (this.strict) {
            return Optional.of(new PortalInfo(new Vec3(goToX, goToY, goToZ), Vec3.ZERO, entity.getYRot(), entity.getXRot()));
        } else {
            if (entity instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer) entity;
                BlockPos bed = player.getRespawnPosition();
                if (bed == null) {
                    bed = destWorld.getSharedSpawnPos();
                }
                int y = destWorld.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, bed.getX(), bed.getZ());
                return Optional.of(new PortalInfo(new Vec3(bed.getX(), y, bed.getZ()), Vec3.ZERO, player.getYRot(), player.getXRot()));
            }
            return Optional.empty();
        }
    }

    @Override
    public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
        return false;
    }

    public void tick(long worldTime) {
        if (worldTime % 100L == 0L) {
            long i = worldTime - 300L;
            ObjectIterator<PortalPosition> objectiterator = this.destinationCoordinateCaChe.values().iterator();
            while (objectiterator.hasNext()) {
                PortalPosition portalShape = objectiterator.next();
                if (portalShape == null || portalShape.lastUpdateTime < i)
                    objectiterator.remove();
            }
        }
    }
}
