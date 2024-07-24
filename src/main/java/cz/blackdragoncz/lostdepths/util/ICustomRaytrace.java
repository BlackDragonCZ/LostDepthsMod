package cz.blackdragoncz.lostdepths.util;

import cz.blackdragoncz.lostdepths.client.CustomRayTraceResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public interface ICustomRaytrace {
    Predicate<Entity> ANTI_IMMATERIAL = entity -> !(entity instanceof EntityImmaterial);

    default CustomRayTraceResult simpleBlockTrace(Level world, LivingEntity entityFrom, int distance) {
        return (CustomRayTraceResult) doRayTrace(world, entityFrom, distance, null, 0, 1, false, null, 1, 1, 0.3F, null);
    }

    default CustomRayTraceResult forwardTrace(Level world, Entity entityFrom, int distance) {
        return (CustomRayTraceResult) doRayTrace(world, entityFrom, distance, null, 0, 0, true, null, 1, 1, 0.3F, null);
    }

    default CustomRayTraceResult immaterialTrace(Level world, LivingEntity entityFrom, int distance) {
        return (CustomRayTraceResult) doRayTrace(world, entityFrom, distance, null, 0, 2, false, null, 1, 1, 0.3F, null);
    }

    default CustomRayTraceResult entityTrace(Level world, LivingEntity entityFrom, int distance, Class<? extends Entity> entityclass) {
        return (CustomRayTraceResult) doRayTrace(world, entityFrom, distance, null, 0, 1, false, entityclass, 1, 1, 0.3F, ANTI_IMMATERIAL);
    }

    default CustomRayTraceResult entitiesTrace(Level world, LivingEntity entityFrom, int distance, Class<? extends Entity> entityclass, int count) {
        return (CustomRayTraceResult) doRayTrace(world, entityFrom, distance, null, 0, 1, false, entityclass, count, 1, 0.3F, ANTI_IMMATERIAL);
    }

    default CustomRayTraceResult standardFXTrace(Level world, LivingEntity entityFrom, int distance, ParticleOptions trailFX, Class<? extends Entity> stopEntityType) {
        return (CustomRayTraceResult) doRayTrace(world, entityFrom, distance, trailFX, 3, 1, false, stopEntityType, 1, 1, 0.3F, ANTI_IMMATERIAL);
    }

    default CustomRayTraceResult forcedDistanceTrace(Level world, Entity entityFrom, int distance) {
        return (CustomRayTraceResult) doRayTrace(world, entityFrom, distance, null, 0, 1, true, null, 1, 1, 0.3F, null);
    }

    default CustomRayTraceResult complexTrace(Level world, LivingEntity entityFrom, int distance, ParticleOptions trailFX, int trailDelay, int blockType, boolean forceResult, Class<? extends Entity> stopEntityType, int lookYOffset, float expand) {
        return (CustomRayTraceResult) doRayTrace(world, entityFrom, distance, trailFX, trailDelay, blockType, forceResult, stopEntityType, 1, lookYOffset, expand, ANTI_IMMATERIAL);
    }

    default CustomRayTraceResult predicateTrace(Level world, LivingEntity entityFrom, int distance, ParticleOptions trailFX, int trailDelay, int blockType, boolean forceResult, Class<? extends Entity> stopEntityType, int lookYOffset, float expand, Predicate<Entity> entityPredicate) {
        return (CustomRayTraceResult) doRayTrace(world, entityFrom, distance, trailFX, trailDelay, blockType, forceResult, stopEntityType, 1, lookYOffset, expand, entityPredicate);
    }

    default ICustomRaytrace doRayTrace(Level world, Entity entityFrom, int distance, ParticleOptions trailFX, int trailDelay, int stopBlock, boolean forceResult, Class<? extends Entity> stopEntityType, int maxEntity, int lookYOffset, float inaccuracy, Predicate<Entity> entityPredicate) {
        Vec3 modifiedLookPosition, vec = entityFrom.getLookAngle();

        if (lookYOffset != 0) {
            modifiedLookPosition = new Vec3(entityFrom.getX(), entityFrom.getY() + 1.5D, entityFrom.getZ());
        } else {
            modifiedLookPosition = new Vec3(entityFrom.getX(), entityFrom.getY(), entityFrom.getZ());
        }

        CustomRayTraceResult result = new CustomRayTraceResult();

        for (int i = 0; i <= distance; i++) {
            Vec3 nextPos = modifiedLookPosition.add(vec.x * i, vec.y * i, vec.z * i);
            AABB blockBox = new AABB(nextPos.x - 0.1D, nextPos.y - 0.1D, nextPos.z - 0.1D, nextPos.x + 0.1D, nextPos.y + 0.1D, nextPos.z + 0.1D).inflate(inaccuracy);

            if (trailFX != null && i >= trailDelay && !world.isClientSide) {
                Vec3 centerVec = new Vec3(blockBox.minX + (blockBox.maxX - blockBox.minX) * 0.5D, blockBox.minY + (blockBox.maxY - blockBox.minY) * 0.5D, blockBox.minZ + (blockBox.maxZ - blockBox.minZ) * 0.5D);
                ((ServerLevel) world).sendParticles(trailFX, centerVec.x, centerVec.y, centerVec.z, 3, 0.0D, 0.0D, 0.0D, 0.0D);
            }

            if (stopEntityType != null) {
                for (Entity nearEntity : world.getEntitiesOfClass(stopEntityType, blockBox)) {
                    if (!nearEntity.getUUID().equals(entityFrom.getUUID()) && (entityPredicate == null || entityPredicate.test(nearEntity))) {
                        result.addResultEntity(nearEntity);
                        if (maxEntity > 0 && result.getResultEntities().size() >= maxEntity) {
                            result.setResultPos(nextPos);
                            return (ICustomRaytrace) result;
                        }
                    }
                }
            }

            BlockPos nextBlockPos = new BlockPos(new BlockPos((int) Math.floor(nextPos.x), (int) Math.floor(nextPos.y), (int) Math.floor(nextPos.z)));

            BlockState stateResult = world.getBlockState(nextBlockPos);
            FluidState fluidStateResult = world.getFluidState(nextBlockPos);
            if (stopBlock > 0 && !world.isEmptyBlock(nextBlockPos) && (stateResult.isSolid() || fluidStateResult.isSource() || stopBlock == 2)) {
                Vec3 referencePos = modifiedLookPosition.add(vec.x * (i - 1), vec.y * (i - 1), vec.z * (i - 1));
                result.setResultPos(referencePos);
                result.setGrabbedVector(nextPos);
                return (ICustomRaytrace) result;
            }
        }

        if (forceResult) {
            Vec3 forcedVec = modifiedLookPosition.add(vec.x * distance, vec.y * distance, vec.z * distance);
            result.setResultPos(forcedVec);
            return (ICustomRaytrace) result;
        }
        return result.isModified() ? (ICustomRaytrace) result : null;
    }
}
