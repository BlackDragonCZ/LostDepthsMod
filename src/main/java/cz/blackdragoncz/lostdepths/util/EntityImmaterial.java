package cz.blackdragoncz.lostdepths.util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class EntityImmaterial extends Mob {
    public EntityImmaterial(EntityType<? extends Mob> type, Level worldIn) {
        super(type, worldIn);
        this.noPhysics = true;
        this.setInvisible(true);
    }

    @Override
    public void aiStep(){
        super.aiStep();
        if (!this.level().isClientSide && !this.isRemoved()){
            this.checkDespawn();
            this.setHealth(Float.MAX_VALUE);
        }
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.0d).add(Attributes.MAX_HEALTH, 10000.0d);
    }

    @Override
    public boolean isPushable(){
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source){
        return true;
    }

    @Override
    public boolean canChangeDimensions(){
        return true;
    }

    @Override
    protected boolean isAffectedByFluids(){
        return false;
    }
}
