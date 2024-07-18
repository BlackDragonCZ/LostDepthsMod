package cz.blackdragoncz.lostdepths.util;

import net.minecraft.world.entity.LivingEntity;

public interface IBasicAI {
    default void initBasicTasks(LivingEntity entityIn) {
        //entityIn.
    }
}
