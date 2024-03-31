package cz.blackdragoncz.lostdepths.client.gui.element;

import cz.blackdragoncz.lostdepths.client.gui.IGuiWrapper;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.network.chat.Component;

public abstract class GuiElement extends AbstractWidget implements ContainerEventHandler {

    protected int relativeX;
    protected int relativeY;
    private IGuiWrapper guiObj;

    public GuiElement(IGuiWrapper gui, int x, int y, int width, int height) {
        this(gui, x, y, width, height, Component.empty());
    }

    public GuiElement(IGuiWrapper gui, int x, int y, int width, int height, Component text) {
        super(gui.getGuiLeft() + x, gui.getGuiTop() + y, width, height, text);
        this.relativeX = x;
        this.relativeY = y;
        this.guiObj = gui;
    }

    public int getRelativeX() {
        return relativeX;
    }

    public int getRelativeY() {
        return relativeY;
    }
}
