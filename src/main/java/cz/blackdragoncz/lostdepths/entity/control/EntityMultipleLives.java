package cz.blackdragoncz.lostdepths.entity.control;

import cz.blackdragoncz.lostdepths.util.IBasicAI;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class EntityMultipleLives extends Mob implements IBasicAI {
    protected EntityMultipleLives(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
}
