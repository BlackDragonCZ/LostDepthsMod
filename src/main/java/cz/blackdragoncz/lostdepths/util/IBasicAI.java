package cz.blackdragoncz.lostdepths.util;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

public interface IBasicAI {
    default void initBasicTasks(Mob entityIn) {
        entityIn.goalSelector.addGoal(1, new MeleeAttackGoal((PathfinderMob) entityIn, 1.2, true));
        entityIn.goalSelector.addGoal(4, new RandomLookAroundGoal(entityIn));
        entityIn.goalSelector.addGoal(2, new HurtByTargetGoal((PathfinderMob) entityIn));
        entityIn.goalSelector.addGoal(5, new NearestAttackableTargetGoal<>(entityIn, Player.class, true));
    }
}


/*
this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false) {
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.8));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, true, true));
 */