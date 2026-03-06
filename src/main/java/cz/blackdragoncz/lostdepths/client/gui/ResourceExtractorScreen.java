package cz.blackdragoncz.lostdepths.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.block.entity.ResourceExtractorBlockEntity;
import cz.blackdragoncz.lostdepths.world.inventory.ResourceExtractorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.energy.EnergyStorage;

import java.text.DecimalFormat;

public class ResourceExtractorScreen extends AbstractContainerScreen<ResourceExtractorMenu> {

    private static final ResourceLocation BG = LostdepthsMod.rl("textures/gui/ld_gui_generic.png");
    private static final ResourceLocation JEI = LostdepthsMod.rl("textures/gui/jei_handler.png");
    private static final int SLOT_SIZE = 19;

    public ResourceExtractorScreen(ResourceExtractorMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
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

        // Pickaxe slot
        drawSlot(g, 43, 19);
        // Solution slots
        drawSlot(g, 25, 49);
        drawSlot(g, 43, 49);
        drawSlot(g, 61, 49);
        // Output slots (2x2)
        drawSlot(g, 115, 29);
        drawSlot(g, 133, 29);
        drawSlot(g, 115, 47);
        drawSlot(g, 133, 47);

        RenderSystem.disableBlend();

        // Progress arrow between solution and output
        ResourceExtractorBlockEntity be = menu.getBlockEntity();
        if (be != null) {
            int maxProg = be.getMaxProgress();
            int prog = be.getProgress();
            if (maxProg > 0 && prog > 0) {
                int arrowWidth = (int) (24.0f * prog / maxProg);
                g.fill(leftPos + 85, topPos + 41, leftPos + 85 + arrowWidth, topPos + 43, 0xFF55FF55);
            }
        }

        // Power bar (bottom right area)
        int powerBarWidth = 20;
        int powerBarHeight = Math.round((42.0f / 14.0f) * powerBarWidth);
        int powerBarX = leftPos + 152;
        int powerBarY = topPos + 10;

        g.blit(JEI, powerBarX, powerBarY, powerBarWidth, powerBarHeight, 140, 144, 14, 42, 256, 256);

        if (be != null) {
            EnergyStorage es = be.getEnergyStorage();
            float fillPct = es.getMaxEnergyStored() > 0 ? (float) es.getEnergyStored() / es.getMaxEnergyStored() : 0;
            int fillHeight = Math.round(powerBarHeight * fillPct);
            g.enableScissor(powerBarX, powerBarY + powerBarHeight - fillHeight, powerBarX + powerBarWidth, powerBarY + powerBarHeight);
            g.blit(JEI, powerBarX, powerBarY, powerBarWidth, powerBarHeight, 156, 144, 14, 42, 256, 256);
            g.disableScissor();
        }
    }

    private static final DecimalFormat ENERGY_FMT = new DecimalFormat("#,###");

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        g.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, -16750849, false);
        g.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -16750849, false);

        // Labels
        g.drawString(this.font, Component.literal("Tool"), 44, 12, 0x555555, false);
        g.drawString(this.font, Component.literal("Solutions"), 22, 42, 0x555555, false);
        g.drawString(this.font, Component.literal("Output"), 117, 22, 0x555555, false);

        // Energy tooltip
        ResourceExtractorBlockEntity be = menu.getBlockEntity();
        if (be != null) {
            int powerBarWidth = 20;
            int powerBarHeight = Math.round((42.0f / 14.0f) * powerBarWidth);
            int powerBarX = leftPos + 152;
            int powerBarY = topPos + 10;
            if (mouseX >= powerBarX && mouseX < powerBarX + powerBarWidth
                    && mouseY >= powerBarY && mouseY < powerBarY + powerBarHeight) {
                EnergyStorage es = be.getEnergyStorage();
                String text = ENERGY_FMT.format(es.getEnergyStored()) + "FE / " + ENERGY_FMT.format(es.getMaxEnergyStored()) + "FE";
                g.renderTooltip(this.font, Component.literal(text), mouseX - this.leftPos, mouseY - this.topPos);
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
