package cz.blackdragoncz.lostdepths.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IGuiWrapper extends ContainerEventHandler {

    default void displayTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, Component... components) {
        this.displayTooltips(guiGraphics, mouseX, mouseY, List.of(components));
    }

    default void displayTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, List<Component> components) {
        guiGraphics.renderComponentTooltip(getFont(), components, mouseX, mouseY);
    }

    @NotNull
    default ItemStack getCarriedItem() {
        return ItemStack.EMPTY;
    }

    int getGuiLeft();

    int getGuiTop();

    int getXSize();

    int getYSize();

    default boolean currentlyQuickCrafting() {
        return false;
    }
    Font getFont();

    default void renderItem(GuiGraphics guiGraphics, @NotNull ItemStack stack, int xAxis, int yAxis) {
        renderItem(guiGraphics, stack, xAxis, yAxis, 1);
    }

    default void renderItem(GuiGraphics guiGraphics, @NotNull ItemStack stack, int xAxis, int yAxis, float scale) {
        GuiUtils.renderItem(guiGraphics, stack, xAxis, yAxis, scale, getFont(), null, false);
    }

    default void renderItemTooltip(GuiGraphics guiGraphics, @NotNull ItemStack stack, int xAxis, int yAxis) {
        guiGraphics.renderTooltip(getFont(), stack, xAxis, yAxis);
    }

    default void renderItemTooltipWithExtra(GuiGraphics guiGraphics, @NotNull ItemStack stack, int xAxis, int yAxis, List<Component> toAppend) {
        if (toAppend.isEmpty()) {
            renderItemTooltip(guiGraphics, stack, xAxis, yAxis);
        } else {
            List<Component> tooltip = new ArrayList<>(Screen.getTooltipFromItem(Minecraft.getInstance(), stack));
            tooltip.addAll(toAppend);
            guiGraphics.renderTooltip(getFont(), tooltip, stack.getTooltipImage(), stack, xAxis, yAxis);
        }
    }

    default void renderItemWithOverlay(GuiGraphics guiGraphics, @NotNull ItemStack stack, int xAxis, int yAxis, float scale, @Nullable String text) {
        GuiUtils.renderItem(guiGraphics, stack, xAxis, yAxis, scale, getFont(), text, true);
    }

}
