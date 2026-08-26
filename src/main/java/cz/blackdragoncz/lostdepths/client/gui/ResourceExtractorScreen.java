package cz.blackdragoncz.lostdepths.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.block.entity.ResourceExtractorBlockEntity;
import cz.blackdragoncz.lostdepths.world.inventory.ResourceExtractorMenu;
import cz.blackdragoncz.lostdepths.util.EnergyFormat;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.energy.EnergyStorage;

import java.util.List;

public class ResourceExtractorScreen extends AbstractContainerScreen<ResourceExtractorMenu> {

    private static final ResourceLocation BG = LostdepthsMod.rl("textures/gui/ld_gui_generic.png");
    private static final ResourceLocation JEI = LostdepthsMod.rl("textures/gui/jei_handler.png");
    private static final int SLOT_SIZE = 19;

    // Status indicator colors
    private static final int COLOR_GREEN = 0xFF00CC00;
    private static final int COLOR_ORANGE = 0xFFFF8800;
    private static final int COLOR_RED = 0xFFCC0000;

    // Source sprite is 42x14 in jei_handler.png, drawn at half size.
    private static final int PROGRESS_X = 133;
    private static final int PROGRESS_Y = 31;
    private static final int PROGRESS_W = 21;
    private static final int PROGRESS_H = 7;

    private float progressFraction() {
        int max = menu.getMaxProgress();
        if (max <= 0) return 0f;
        return Math.min(1f, (float) menu.getProgress() / max);
    }

    private static int statusColor(int status) {
        return switch (status) {
            case ResourceExtractorBlockEntity.STATUS_ACTIVE -> COLOR_GREEN;
            case ResourceExtractorBlockEntity.STATUS_MISSING, ResourceExtractorBlockEntity.STATUS_FULL -> COLOR_ORANGE;
            default -> COLOR_RED;
        };
    }

    private static Component statusText(int status) {
        return switch (status) {
            case ResourceExtractorBlockEntity.STATUS_ACTIVE ->
                    Component.literal("Status: Active").withStyle(s -> s.withColor(0x00CC00));
            case ResourceExtractorBlockEntity.STATUS_MISSING ->
                    Component.literal("Status: Missing requirement").withStyle(s -> s.withColor(0xFF8800));
            case ResourceExtractorBlockEntity.STATUS_FULL ->
                    Component.literal("Status: Full storage").withStyle(s -> s.withColor(0xFF8800));
            case ResourceExtractorBlockEntity.STATUS_NO_ORE ->
                    Component.literal("Status: No valid ore below").withStyle(s -> s.withColor(0xCC0000));
            default ->
                    Component.literal("Status: Disabled by redstone").withStyle(s -> s.withColor(0xCC0000));
        };
    }

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

        // Blue arrow pair from the widget atlas.
        g.blit(JEI, leftPos + PROGRESS_X, topPos + PROGRESS_Y, PROGRESS_W, PROGRESS_H, 96, 144, 42, 14, 256, 256);
        int filled = Math.round(PROGRESS_W * progressFraction());
        if (filled > 0) {
            g.enableScissor(leftPos + PROGRESS_X, topPos + PROGRESS_Y,
                    leftPos + PROGRESS_X + filled, topPos + PROGRESS_Y + PROGRESS_H);
            g.blit(JEI, leftPos + PROGRESS_X, topPos + PROGRESS_Y, PROGRESS_W, PROGRESS_H, 96, 160, 42, 14, 256, 256);
            g.disableScissor();
        }

        RenderSystem.disableBlend();

        // Status indicator
        int statusColor = statusColor(menu.getMachineStatus());
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
            g.renderTooltip(this.font, statusText(menu.getMachineStatus()), mouseX - this.leftPos, mouseY - this.topPos);
        }

        // Progress tooltip
        if (mouseX >= leftPos + PROGRESS_X && mouseX < leftPos + PROGRESS_X + PROGRESS_W
                && mouseY >= topPos + PROGRESS_Y && mouseY < topPos + PROGRESS_Y + PROGRESS_H) {
            String text = "Progress: " + Math.round(progressFraction() * 100) + "%";
            g.renderTooltip(this.font, Component.literal(text), mouseX - this.leftPos, mouseY - this.topPos);
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
                String text = EnergyFormat.stored(es.getEnergyStored(), es.getMaxEnergyStored());
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
