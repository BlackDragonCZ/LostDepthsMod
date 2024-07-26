package cz.blackdragoncz.lostdepths.entity.control;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.commands.data.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;

public class EntityMultiLivesTameable /*extends TamableAnimal */{
/*
    private static final EntityDataAccessor<Integer> LIFECOUNT_TAMEABLE = new SynchedEntityData().define(EntityMultiLivesTameable.class, EntityDataSerializers.INT);
    private boolean performedDeathAction = false;

    public EntityMultiLivesTameable(EntityType<? extends TamableAnimal> entityType, Level worldIn) {
        super(entityType, worldIn);
    }

    public boolean didDeathAction() {
        return this.performedDeathAction;
    }

    public void deathActionComplete() {
        this.performedDeathAction = true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LIFECOUNT_TAMEABLE, 0);
    }

    public int getLivesCount() {
        return this.entityData.get(LIFECOUNT_TAMEABLE);
    }

    public void setLivesCount(int f) {
        this.entityData.set(LIFECOUNT_TAMEABLE, f);
    }

    public void takewayLife() {
        this.entityData.set(LIFECOUNT_TAMEABLE, getLivesCount() + 1);
        updateLifeAction();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("LivesUsed", getLivesCount());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setLivesCount(tag.getInt("LivesUsed"));
    }

    @Override
    public boolean canSpawnSprintParticle() {
        return (getDifficulty() != Difficulty.PEACEFUL);
    }

    public boolean onFinalLife() {
        return (getLivesCount() == numberOfLives());
    }

    protected int numberOfLives() {
        return 10;
    }

    protected void updateLifeAction() {}

    public void trueDeathAction() {}

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob mate) {
        return null;
    }

    @Override
    public void die(DamageSource cause) {
        if (onFinalLife()) {
            super.die(cause);
        }
    }*/
}
