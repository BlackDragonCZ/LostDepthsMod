package cz.blackdragoncz.lostdepths.util;

import cz.blackdragoncz.lostdepths.entity.control.EntityMultiLivesTameable;
import cz.blackdragoncz.lostdepths.entity.control.EntityMultipleLives;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class CustomDamageResult {
    private final LivingEntity target;
    private final Entity attacker;
    private boolean validHit = true;
    private boolean successfulHit = true;
    private boolean targetLifeTaken = false;
    private boolean targetKilled = false;
    private float intendedDamage = 0.0F;
    private float preTargetHealth = 0.0F;
    private float postTargetHealth = 0.0F;
    private float damageDealt = 0.0F;

    private final List<Float> damageReductions = new ArrayList<>();
    private final List<Float> damageAmplifications = new ArrayList<>();
    private final List<String> damageClassifications = new ArrayList<>();

    public CustomDamageResult(Entity attacker, LivingEntity target) {
        this.target = target;
        this.preTargetHealth = target.getHealth();
        this.attacker = attacker;
    }

    public void addClassifications(List<String> damageTypes) {
        if (damageTypes != null) {
            this.damageClassifications.addAll(damageTypes);
        }
    }

    public void setIntendedDamage(float intended) {
        this.intendedDamage = intended;
    }

    public void setHitMissed() {
        this.successfulHit = false;
    }

    public void setHitInvalid() {
        this.validHit = false;
    }

    public void addReduction(float reduction) {
        this.damageReductions.add(reduction);
    }

    public void addAmplification(float amplification) {
        this.damageAmplifications.add(amplification);
    }

    public void finishHitData(float finalDamage, float postHealth) {
        this.damageDealt = finalDamage;
        this.postTargetHealth = postHealth;
        if (postHealth <= 0.0F) {
            takeLife();
        }
    }

    public void takeLife() {
        this.targetLifeTaken = true;
        if (this.target instanceof EntityMultipleLives) {
            EntityMultipleLives multiTarget = (EntityMultipleLives) this.target;
            if (multiTarget.onFinalLife()) {
                this.targetKilled = true;
            }
        }/* else if (this.target instanceof EntityMultiLivesTameable) {
            EntityMultiLivesTameable multiTarget = (EntityMultiLivesTameable) this.target;
            if (multiTarget.onFinalLife()) {
                this.targetKilled = true;
            }
        }*/ else {
            this.targetKilled = true;
        }
    }

    public LivingEntity getDamageTarget() {
        return this.target;
    }

    public Entity getAttacker() {
        return this.attacker;
    }

    public boolean didSuccessfulHit() {
        return this.successfulHit && this.validHit;
    }

    public boolean wasHitValid() {
        return this.validHit;
    }

    public boolean hitBlockedOrDodged() {
        return this.validHit && !didSuccessfulHit();
    }

    public boolean didTargetLoseLife() {
        return this.targetLifeTaken;
    }

    public boolean wasTargetKilled() {
        return this.targetKilled;
    }

    public float getIntendedDamage() {
        return this.intendedDamage;
    }

    public float getInitialTargetHealth() {
        return this.preTargetHealth;
    }

    public float getTargetEndHealth() {
        return this.postTargetHealth;
    }

    public float getDamageDealt() {
        return Math.max(this.damageDealt, 0.0F);
    }

    public boolean targetHealthChanged() {
        return this.preTargetHealth != this.postTargetHealth;
    }

    public int numberOfReductions() {
        return this.damageReductions.size();
    }

    public int numberOfAmplifications() {
        return this.damageAmplifications.size();
    }

    public float extraDamageDealt() {
        float extra = 0.0F;
        for (float f : this.damageAmplifications) {
            extra += f;
        }
        return extra;
    }

    public float damageReduced() {
        float reduced = 0.0F;
        for (float f : this.damageReductions) {
            reduced += f;
        }
        return reduced;
    }
}
