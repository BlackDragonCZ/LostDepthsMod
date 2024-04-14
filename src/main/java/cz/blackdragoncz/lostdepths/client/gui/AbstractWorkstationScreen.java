package cz.blackdragoncz.lostdepths.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import cz.blackdragoncz.lostdepths.block.entity.IEnergyAccessor;
import cz.blackdragoncz.lostdepths.world.inventory.AbstractWorkstationMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraftforge.energy.EnergyStorage;

import java.text.DecimalFormat;

public class AbstractWorkstationScreen<MENU extends AbstractWorkstationMenu> extends AbstractContainerScreen<MENU> {

    private static final ResourceLocation jei_handler = new ResourceLocation("lostdepths:textures/gui/jei_handler.png");

    private final ResourceLocation background;
    private final IEnergyAccessor energyAccessor;

    public AbstractWorkstationScreen(MENU pMenu, Inventory pPlayerInventory, Component pTitle, ResourceLocation background) {
        super(pMenu, pPlayerInventory, pTitle);
        this.background = background;
        this.energyAccessor = pMenu.getEnergyAccessor();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        if (this.energyAccessor != null) {
            if (this.energyAccessor.getEnergyStorage().getEnergyStored() < getMenu().getRequiredEnergyToCraft() && getMenu().hasFoundRecipe()) {
                ResultSlot slot = this.getMenu().getResultSlot();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                guiGraphics.fill(this.leftPos + slot.x, this.topPos + slot.y, this.leftPos + slot.x + 16, this.topPos + slot.y + 16, FastColor.ARGB32.color(100, 255, 0, 0));
                RenderSystem.disableBlend();
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(background, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        RenderSystem.disableBlend();

        if (this.energyAccessor != null) {
            EnergyStorage energyStorage = this.energyAccessor.getEnergyStorage();
            float energyValue = energyStorage.getEnergyStored() / (float)energyStorage.getMaxEnergyStored();;

            guiGraphics.blit(jei_handler, this.leftPos + 120, this.topPos + 10, 96, 144, 42, 14, 256, 256);
            guiGraphics.enableScissor(this.leftPos + 120, this.topPos + 10, this.leftPos + 120 + Math.round(42 * energyValue), this.topPos + 10 + 14);
            guiGraphics.blit(jei_handler, this.leftPos + 120, this.topPos + 10, 96, 160, 42, 14, 256, 256);
            guiGraphics.disableScissor();
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

    private static final DecimalFormat energyStringFormat = new DecimalFormat("#,###"); // #,###

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        if (this.energyAccessor != null) {
            EnergyStorage energyStorage = this.energyAccessor.getEnergyStorage();

            if (mouseX >= this.leftPos + 120 && mouseX < this.leftPos + 120 + 42 && mouseY >= this.topPos + 10 && mouseY < this.topPos + 10 + 14) {
                String builder = energyStringFormat.format(energyStorage.getEnergyStored()) +
                        "FE / " +
                        energyStringFormat.format(energyStorage.getMaxEnergyStored()) +
                        "FE";

                graphics.renderTooltip(this.font, Component.literal(builder), mouseX - this.leftPos, mouseY - this.topPos);
            }
        }
    }
}
