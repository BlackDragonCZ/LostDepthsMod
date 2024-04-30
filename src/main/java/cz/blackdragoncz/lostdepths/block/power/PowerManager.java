package cz.blackdragoncz.lostdepths.block.power;

import cz.blackdragoncz.lostdepths.energy.PowerCable;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PowerManager {

    public static PowerManager INSTANCE = new PowerManager();

    private List<PowerCable> cables = new ArrayList<>();

    public PowerCable addCable(BlockEntity entity) {
        PowerCable cable = new PowerCable(entity);
        cables.add(cable);
        return cable;
    }

    public boolean removeCable(@NotNull BlockEntity entity) {
        return cables.remove(entity);
    }
}
