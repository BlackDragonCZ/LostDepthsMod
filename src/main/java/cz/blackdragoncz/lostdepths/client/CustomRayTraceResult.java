package cz.blackdragoncz.lostdepths.client;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class CustomRayTraceResult {
    private boolean modified;
    private Vec3 blockResultPosition = null;
    private Vec3 blockGrabbedPosition = null;
    private final Set<Entity> entityResult = new HashSet<>();

    public CustomRayTraceResult() {}

    public CustomRayTraceResult(Vec3 pos, Entity entity) {
        setResultPos(pos);
        addResultEntity(entity);
    }

    public CustomRayTraceResult(Vec3 pos, Vec3 grabbed, Entity entity) {
        setResultPos(pos);
        addResultEntity(entity);
        setGrabbedVector(grabbed);
    }

    public boolean isModified() {
        return this.modified;
    }

    public void setResultPos(Vec3 pos) {
        this.modified = true;
        this.blockResultPosition = pos;
    }

    public BlockPos getResultPos() {
        return new BlockPos((int) Math.floor(this.blockResultPosition.x), (int) Math.floor(this.blockResultPosition.y), (int) Math.floor(this.blockResultPosition.z));
    }

    public Vec3 getResultVector() {
        return this.blockResultPosition;
    }

    public void addResultEntity(Entity entity) {
        this.modified = true;
        this.entityResult.add(entity);
    }

    public Entity getResultEntity() {
        return this.entityResult.isEmpty() ? null : this.entityResult.iterator().next();
    }

    public Set<Entity> getResultEntities() {
        return this.entityResult;
    }

    public void setGrabbedVector(Vec3 pos) {
        this.modified = true;
        this.blockGrabbedPosition = pos;
    }

    public Vec3 getGrabbedVector() {
        return this.blockGrabbedPosition;
    }
}
