package cz.blackdragoncz.lostdepths.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;

public class PowerCable {

    private BlockEntity entity;
    private List<BlockEntity> forgeEnergies = new ArrayList<>();
    private Map<Direction, ICableInterface> sideCables = new HashMap<>();

    public PowerCable(BlockEntity entity) {
        this.entity = entity;
    }

    public BlockEntity getEntity() {
        return entity;
    }

    public void removeCable(BlockEntity cableEntity) {
        sideCables.entrySet().removeIf((entry) -> entry.getValue().getPowerCable() != null && entry.getValue().getPowerCable().getEntity() == cableEntity);
    }

    public void clearForgeEnergies() {
        forgeEnergies.clear();
    }

    public void clearSideCables() {
        sideCables.clear();
    }

    public void setSideCable(Direction direction, ICableInterface cable) {
        sideCables.put(direction, cable);
    }

    public Map<Direction, ICableInterface> getSideCables() {
        return sideCables;
    }

    public List<BlockEntity> getForgeEnergies() {
        return forgeEnergies;
    }

    public void addForgeEnergy(BlockEntity forgeEnergy) {
        this.forgeEnergies.add(forgeEnergy);
    }

    public void removeForgeEnergy(BlockEntity forgeEnergy) {
        this.forgeEnergies.remove(forgeEnergy);
    }

    public void collectEnergy(List<BlockEntity> energyEntities) {
        List<PowerCable> checkedCables = new ArrayList<>();
        checkedCables.add(this);

        Queue<PowerCable> queue = new LinkedList<>();
        queue.add(this);

        while (!queue.isEmpty()) {
            PowerCable cable = queue.poll();

            if (!checkedCables.contains(cable)) {
                checkedCables.add(cable);
            }

            for (ICableInterface cableInterface : cable.getSideCables().values()) {
                PowerCable sideCable = cableInterface.getPowerCable();

                if (sideCable == null)
                    continue;

                if (!checkedCables.contains(sideCable)) {
                    queue.add(sideCable);
                }
            }

            for (BlockEntity energyEntity : cable.getForgeEnergies()) {
                if (!energyEntities.contains(energyEntity)) {
                    energyEntities.add(energyEntity);
                }
            }
        }
    }
}
