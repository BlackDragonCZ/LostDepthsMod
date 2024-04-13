package cz.blackdragoncz.lostdepths.client.gui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ContainerWrapper implements Container {

    private final Container container;

    public ContainerWrapper(Container container) {
        this.container = container;
    }

    @Override
    public int getContainerSize() {
        return container.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return container.isEmpty();
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return container.getItem(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return container.removeItem(pSlot, pAmount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return container.removeItemNoUpdate(pSlot);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        container.setItem(pSlot, pStack);
    }

    @Override
    public void setChanged() {
        container.setChanged();
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return container.stillValid(pPlayer);
    }

    @Override
    public void clearContent() {
        container.clearContent();
    }
}
