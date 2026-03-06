package cz.blackdragoncz.lostdepths.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.world.inventory.NurostarBatteryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.energy.EnergyStorage;

import java.text.DecimalFormat;

public class NurostarBatteryScreen extends AbstractContainerScreen<NurostarBatteryMenu> {

    private static final ResourceLocation BG = LostdepthsMod.rl("textures/gui/ld_gui_generic.png");
    private static final ResourceLocation JEI = LostdepthsMod.rl("textures/gui/jei_handler.png");
    private static final int SLOT_SIZE = 19;

    public NurostarBatteryScreen(NurostarBatteryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 206;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(g);
        super.render(g, mouseX, mouseY, partialTicks);
        this.renderTooltip(g, mouseX, mouseY);
    }

    private void drawSlot(GuiGraphics g, int x, int y) {
        g.blit(BG, leftPos + x, topPos + y, 3, 226 - 19, SLOT_SIZE, SLOT_SIZE, 176, 226);
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        g.blit(BG, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight + 20);

        int leftSideWidth = this.imageWidth / 2;

        // Charge input slot (left)
        drawSlot(g, leftSideWidth / 2 - SLOT_SIZE - 2, 54);
        // Drain output slot (right)
        drawSlot(g, leftSideWidth / 2 + 2, 54);

        // Arrow: charge direction indicator (battery -> item) on left slot
        g.blit(JEI, leftPos + leftSideWidth / 2 - SLOT_SIZE - 2 + 4, topPos + 44, 176, 0, 11, 8, 256, 256);
        // Arrow: drain direction indicator (item -> battery) on right slot
        g.blit(JEI, leftPos + leftSideWidth / 2 + 6, topPos + 44, 176, 0, 11, 8, 256, 256);

        RenderSystem.disableBlend();

        // Power bar — centered on right half
        int powerBarWidth = 30;
        int powerBarHeight = Math.round((42.0f / 14.0f) * powerBarWidth);
        int powerBarX = leftPos + leftSideWidth + leftSideWidth / 2 - powerBarWidth / 2;
        int powerBarY = topPos + 50 - powerBarHeight / 2;

        // Background bar
        g.blit(JEI, powerBarX, powerBarY, powerBarWidth, powerBarHeight, 140, 144, 14, 42, 256, 256);

        // Filled bar
        EnergyStorage energyStorage = menu.getBlockEntity().getEnergyStorage();
        float fillPct = energyStorage.getMaxEnergyStored() > 0
                ? (float) energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored()
                : 0;
        int fillHeight = Math.round(powerBarHeight * fillPct);

        g.enableScissor(powerBarX, powerBarY + powerBarHeight - fillHeight, powerBarX + powerBarWidth, powerBarY + powerBarHeight);
        g.blit(JEI, powerBarX, powerBarY, powerBarWidth, powerBarHeight, 156, 144, 14, 42, 256, 256);
        g.disableScissor();
    }

    private static final DecimalFormat ENERGY_FORMAT = new DecimalFormat("#,###");

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        g.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, -16750849, false);
        g.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY + 20, -16750849, false);

        int leftSideWidth = this.imageWidth / 2;

        // Labels under slots
        g.drawString(this.font, Component.literal("Charge"), leftSideWidth / 2 - SLOT_SIZE - 2, 76, 0x555555, false);
        g.drawString(this.font, Component.literal("Drain"), leftSideWidth / 2 + 4, 76, 0x555555, false);

        // Energy tooltip on hover
        EnergyStorage energyStorage = menu.getBlockEntity().getEnergyStorage();
        int powerBarWidth = 30;
        int powerBarHeight = Math.round((42.0f / 14.0f) * powerBarWidth);
        int powerBarX = leftPos + leftSideWidth + leftSideWidth / 2 - powerBarWidth / 2;
        int powerBarY = topPos + 50 - powerBarHeight / 2;

        if (mouseX >= powerBarX && mouseX < powerBarX + powerBarWidth
                && mouseY >= powerBarY && mouseY < powerBarY + powerBarHeight) {
            String text = ENERGY_FORMAT.format(energyStorage.getEnergyStored()) +
                    "FE / " +
                    ENERGY_FORMAT.format(energyStorage.getMaxEnergyStored()) +
                    "FE";
            g.renderTooltip(this.font, Component.literal(text), mouseX - this.leftPos, mouseY - this.topPos);
        }
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            this.minecraft.player.closeContainer();
            return true;
        }
        return super.keyPressed(key, b, c);
    }
}
