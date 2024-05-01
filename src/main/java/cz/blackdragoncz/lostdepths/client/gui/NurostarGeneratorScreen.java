package cz.blackdragoncz.lostdepths.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.world.inventory.NurostarGeneratorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import java.text.DecimalFormat;
import java.util.Optional;

public class NurostarGeneratorScreen extends AbstractContainerScreen<NurostarGeneratorMenu> {

    private static ResourceLocation bg = LostdepthsMod.rl("textures/gui/ld_gui_generic.png");
    private static ResourceLocation JEI = LostdepthsMod.rl("textures/gui/jei_handler.png");
    public static final int SLOT_SIZE = 19;

    public NurostarGeneratorScreen(NurostarGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 226 - 20;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void drawSlot(GuiGraphics g, int x, int y) {
        g.blit(bg, leftPos + x, topPos + y, 3, 226 - 19, SLOT_SIZE, SLOT_SIZE, 176, 226);
    }

    @Override
    protected void renderBg(GuiGraphics g, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        g.blit(bg, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight + 20);

        RenderSystem.disableBlend();

        int leftSideWidth = this.imageWidth / 2;

        PoseStack poseStack = g.pose();
        poseStack.pushPose();
        poseStack.translate(leftPos + leftSideWidth / 2.0f, topPos + 125.0f / 2.0f, 0);
        poseStack.pushPose();
        poseStack.last().pose().rotate((float) Math.toRadians(-90), 0, 0, 1);
        g.blit(bg, -16, -6, 23, 226 - 19, 32, 12, 176, 226);
        poseStack.popPose();
        poseStack.popPose();



        //g.fill(leftPos + leftSideWidth, topPos, leftPos + leftSideWidth + 1, topPos + this.imageHeight, 0xFFFFFFFF);

        int twoSlotsWidth = (SLOT_SIZE * 2) + 5;
        int twoSlotsY = 85;
        drawSlot(g, leftSideWidth / 2 - twoSlotsWidth / 2, twoSlotsY);
        drawSlot(g, leftSideWidth / 2 - twoSlotsWidth / 2 + SLOT_SIZE + 5, twoSlotsY);
        drawSlot(g, leftSideWidth / 2 - SLOT_SIZE / 2, 20);

        int powerBarWidth = 30;
        int powerBarHeight = Math.round((42.0f / 14.0f) * powerBarWidth);


        int powerBarX = leftPos + leftSideWidth + leftSideWidth / 2 - powerBarWidth / 2;
        int powerBarY = topPos + 125 / 2 - powerBarHeight / 2;
        g.blit(JEI, powerBarX, powerBarY, powerBarWidth, powerBarHeight, 140, 144, 14, 42, 256, 256);

        EnergyStorage energyStorage = menu.getBlockEntity().getEnergyStorage();

        int powerBarTopCutout = Math.round(powerBarHeight * (energyStorage.getEnergyStored() / (float)energyStorage.getMaxEnergyStored()));
        g.enableScissor(powerBarX, powerBarY + powerBarHeight - powerBarTopCutout, powerBarX + powerBarWidth, powerBarY + powerBarHeight);
        g.blit(JEI, powerBarX, powerBarY, powerBarWidth, powerBarHeight, 156, 144, 14, 42, 256, 256);
        g.disableScissor();
    }

    private static final DecimalFormat energyStringFormat = new DecimalFormat("#,###"); // #,###

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        g.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, -16750849, false);
        g.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY + 40, -16750849, false);

        EnergyStorage energyStorage = menu.getBlockEntity().getEnergyStorage();
        int powerBarWidth = 30;
        int powerBarHeight = Math.round((42.0f / 14.0f) * powerBarWidth);
        int leftSideWidth = this.imageWidth / 2;
        int powerBarX = leftPos + leftSideWidth + leftSideWidth / 2 - powerBarWidth / 2;
        int powerBarY = topPos + 125 / 2 - powerBarHeight / 2;

        if (mouseX >= powerBarX && mouseX < powerBarX + powerBarWidth) {
            if (mouseY >= powerBarY && mouseY < powerBarY + powerBarHeight) {
                String builder = energyStringFormat.format(energyStorage.getEnergyStored()) +
                        "FE / " +
                        energyStringFormat.format(energyStorage.getMaxEnergyStored()) +
                        "FE";
                g.renderTooltip(this.font, Component.literal(builder), mouseX - this.leftPos, mouseY - this.topPos);
            }
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
