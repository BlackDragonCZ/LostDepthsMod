package cz.blackdragoncz.lostdepths.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.item.ShipmentBoxItem;
import cz.blackdragoncz.lostdepths.world.inventory.ShipmentFillerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Optional;

public class ShipmentFillerScreen extends AbstractContainerScreen<ShipmentFillerMenu> {

    private static final ResourceLocation TEXTURE = LostdepthsMod.rl("textures/screens/shipment_filler.png");

    // Progress bar: vertical bar between input and box slots
    private static final int PROGRESS_X = 27;
    private static final int PROGRESS_Y = 6;
    private static final int PROGRESS_WIDTH = 8;
    private static final int PROGRESS_MAX_HEIGHT = 73;

    // Progress bar UV on texture (below main GUI)
    private static final int PROGRESS_UV_X = 0;
    private static final int PROGRESS_UV_Y = 166;

    public ShipmentFillerScreen(ShipmentFillerMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(g);
        super.render(g, mouseX, mouseY, partialTicks);
        this.renderTooltip(g, mouseX, mouseY);

        // Custom weight tooltip when hovering over the progress bar
        int barX = leftPos + PROGRESS_X;
        int barY = topPos + PROGRESS_Y;
        if (mouseX >= barX && mouseX <= barX + PROGRESS_WIDTH
                && mouseY >= barY && mouseY <= barY + PROGRESS_MAX_HEIGHT) {
            int weight = menu.getWeight();
            g.renderTooltip(this.font,
                    List.of(Component.literal("Weight: " + weight + "/" + ShipmentBoxItem.MAX_WEIGHT + " lbs")),
                    Optional.empty(), mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Draw main background
        g.blit(TEXTURE, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        // Draw vertical progress bar (fills bottom to top)
        int weight = menu.getWeight();
        if (weight > 0) {
            int filledHeight = (int) ((weight / (float) ShipmentBoxItem.MAX_WEIGHT) * PROGRESS_MAX_HEIGHT);
            if (filledHeight > PROGRESS_MAX_HEIGHT) filledHeight = PROGRESS_MAX_HEIGHT;

            // Draw from bottom of progress area upward
            int drawY = topPos + PROGRESS_Y + PROGRESS_MAX_HEIGHT - filledHeight;
            g.blit(TEXTURE, leftPos + PROGRESS_X, drawY,
                    PROGRESS_UV_X, PROGRESS_UV_Y + PROGRESS_MAX_HEIGHT - filledHeight,
                    PROGRESS_WIDTH, filledHeight);
        }

        RenderSystem.disableBlend();
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        // Don't render default title/inventory labels
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
