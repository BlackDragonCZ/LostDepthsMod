package cz.blackdragoncz.lostdepths.entity.control;

import cz.blackdragoncz.lostdepths.util.IBasicAI;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import java.util.Iterator;

public class EntityMultipleLives extends Mob implements IBasicAI {
    private boolean performeddeathAction = false;
    private static final EntityDataAccessor<Integer> LIFECOUNT = SynchedEntityData.defineId(EntityMultipleLives.class, EntityDataSerializers.INT);

    public EntityMultipleLives(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals(){
        initBasicTasks(this);
    }

    @Override
    protected void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(LIFECOUNT, 0);
    }

    public boolean didDeathAction(){
        return this.performeddeathAction;
    }

    public int getLivesCount(){
        return this.entityData.get(LIFECOUNT);
    }

    public void setLivesCount(int f){
        this.entityData.set(LIFECOUNT, f);
    }

    public void removeLife(){
        this.entityData.set(LIFECOUNT, getLivesCount() + 1);
        updateLifeAction();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag){
        super.addAdditionalSaveData(tag);
        tag.putInt("LivesUsed", getLivesCount());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag){
        super.readAdditionalSaveData(tag);
        setLivesCount(tag.getInt("LivesUsed"));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer){
        return true;
    }

    public void removeNumLives(int lives){
        for (int i = 0; i < lives && !didDeathAction(); i++){
            this.setHealth(0.0f);
        }
    }

    public boolean onFinalLife(){
        return getLivesCount() == numberOfLives();
    }

    protected int numberOfLives(){
        return 10;
    }

    public int remainingLives() {
        return numberOfLives() - getLivesCount();
    }

    protected void doDamage(){
        if (!this.level().isClientSide){
            ((ServerLevel)this.level()).getChunkSource().broadcast(this, new ClientboundAnimatePacket(this, 1));
        } else {
            this.hurtMarked = true;
        }
    }

    protected boolean nothingInRadius(int i){
        Iterator<EntityMultipleLives> iterator = this.level().getEntitiesOfClass(EntityMultipleLives.class, this.getBoundingBox().inflate(i)).iterator();
        if (iterator.hasNext()){
            EntityMultipleLives nearCreature = iterator.next();
            return false;
        }
        return true;
    }

    protected void updateLifeAction(){}

    public void trueDeathAction(){}
}