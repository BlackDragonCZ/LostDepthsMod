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
import java.util.List;

public class ResourceExtractorScreen extends AbstractContainerScreen<ResourceExtractorMenu> {

    private static final ResourceLocation BG = LostdepthsMod.rl("textures/gui/ld_gui_generic.png");
    private static final ResourceLocation JEI = LostdepthsMod.rl("textures/gui/jei_handler.png");
    private static final int SLOT_SIZE = 19;

    // Status indicator colors
    private static final int COLOR_GREEN = 0xFF00CC00;
    private static final int COLOR_ORANGE = 0xFFFF8800;
    private static final int COLOR_RED = 0xFFCC0000;

    public ResourceExtractorScreen(ResourceExtractorMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 206;
        this.inventoryLabelY = this.imageHeight - 94;
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
        drawSlot(g, 26, 82);
        // Solution slots
        drawSlot(g, 7, 55);
        drawSlot(g, 25, 55);
        drawSlot(g, 43, 55);
        // Output slots (2x2)
        drawSlot(g, 84, 27);
        drawSlot(g, 102, 27);
        drawSlot(g, 84, 45);
        drawSlot(g, 102, 45);

        RenderSystem.disableBlend();

        // Progress arrow (using synced ContainerData)
        int maxProg = menu.getMaxProgress();
        int prog = menu.getProgress();
        if (maxProg > 0 && prog > 0) {
            int arrowWidth = (int) (24.0f * prog / maxProg);
            g.fill(leftPos + 131, topPos + 29, leftPos + 131 + arrowWidth, topPos + 31, 0xFF55FF55);
        }

        // Status indicator
        int status = menu.getMachineStatus();
        int statusColor = switch (status) {
            case ResourceExtractorBlockEntity.STATUS_GREEN -> COLOR_GREEN;
            case ResourceExtractorBlockEntity.STATUS_ORANGE -> COLOR_ORANGE;
            default -> COLOR_RED;
        };
        int statusX = leftPos + 66;
        int statusY = topPos + 36;
        int statusSize = 8;
        // Outer border
        g.fill(statusX - 1, statusY - 1, statusX + statusSize + 1, statusY + statusSize + 1, 0xFF333333);
        // Inner fill
        g.fill(statusX, statusY, statusX + statusSize, statusY + statusSize, statusColor);

        // Energy bar
        int powerBarWidth = 20;
        int powerBarHeight = 60;
        int powerBarX = leftPos + 133;
        int powerBarY = topPos + 45;

        g.blit(JEI, powerBarX, powerBarY, powerBarWidth, powerBarHeight, 140, 144, 14, 42, 256, 256);

        ResourceExtractorBlockEntity be = menu.getBlockEntity();
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
        g.drawString(this.font, Component.literal("Tool"), 49, 86, 0x555555, false);
        g.drawString(this.font, Component.literal("Solutions"), 20, 43, 0x555555, false);
        g.drawString(this.font, Component.literal("Output"), 88, 14, 0x555555, false);

        // Status tooltip
        int statusX = 66;
        int statusY = 36;
        int statusSize = 8;
        if (mouseX >= leftPos + statusX - 1 && mouseX < leftPos + statusX + statusSize + 1
                && mouseY >= topPos + statusY - 1 && mouseY < topPos + statusY + statusSize + 1) {
            int status = menu.getMachineStatus();
            Component statusText = switch (status) {
                case ResourceExtractorBlockEntity.STATUS_GREEN -> Component.literal("Status: Active").withStyle(s -> s.withColor(0x00CC00));
                case ResourceExtractorBlockEntity.STATUS_ORANGE -> Component.literal("Status: Missing requirements").withStyle(s -> s.withColor(0xFF8800));
                default -> Component.literal("Status: Disabled by redstone").withStyle(s -> s.withColor(0xCC0000));
            };
            g.renderTooltip(this.font, statusText, mouseX - this.leftPos, mouseY - this.topPos);
        }

        // Energy tooltip
        ResourceExtractorBlockEntity be = menu.getBlockEntity();
        if (be != null) {
            int powerBarWidth = 20;
            int powerBarHeight = 60;
            int powerBarX = leftPos + 133;
            int powerBarY = topPos + 45;
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
