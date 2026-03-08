package cz.blackdragoncz.lostdepths.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.block.entity.FusionTableBlockEntity;
import cz.blackdragoncz.lostdepths.network.FusionTablePlaceShapePacket;
import cz.blackdragoncz.lostdepths.world.inventory.FusionTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import cz.blackdragoncz.lostdepths.init.LostdepthsModSounds;
import net.minecraft.world.entity.player.Inventory;

public class FusionTableScreen extends AbstractContainerScreen<FusionTableMenu> {

    private static final ResourceLocation TEXTURE = LostdepthsMod.rl("textures/gui/fusion_table.png");

    private static final int SHAPE_SIZE = 12;
    private static final int CELL_GAP = 5;

    // Board grid position relative to GUI
    private static final int BOARD_X = 40;
    private static final int BOARD_Y = 16;

    // Upcoming shapes position
    private static final int UPCOMING_CURRENT_X = 21;
    private static final int UPCOMING_CURRENT_Y = 65;
    private static final int UPCOMING_NEXT_X = 21;
    private static final int UPCOMING_NEXT_Y = 50;

    // Progress bar
    private static final int PROGRESS_X = 39;
    private static final int PROGRESS_Y = 5;
    private static final int PROGRESS_SEGMENT_W = 14;
    private static final int PROGRESS_SEGMENT_H = 8;
    private static final int PROGRESS_GAP = 3;

    // Shape sprite positions on texture (below main GUI at y=166)
    private static final int[][] SHAPE_UV = {
            {0, 166},   // Shape 1: SQUARE
            {12, 166},  // Shape 2: VERTICAL_RECTANGLE
            {24, 166},  // Shape 3: DIAGONAL_RECTANGLE
            {36, 166},  // Shape 4: TRIANGLE
            {48, 166},  // Shape 5: HORIZONTAL_RECTANGLE
    };

    // Progress filled segment UV
    private static final int PROGRESS_UV_X = 0;
    private static final int PROGRESS_UV_Y = 178;

    // Hover highlight UV
    private static final int HOVER_UV_X = 0;
    private static final int HOVER_UV_Y = 186;
    private static final int HOVER_SIZE = 14;

    public FusionTableScreen(FusionTableMenu menu, Inventory inv, Component title) {
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

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Draw main background
        g.blit(TEXTURE, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (!menu.isPuzzleActive()) {
            RenderSystem.disableBlend();
            return;
        }

        // Draw board shapes
        for (int row = 0; row < FusionTableBlockEntity.BOARD_ROWS; row++) {
            for (int col = 0; col < FusionTableBlockEntity.BOARD_COLUMNS; col++) {
                int cellIndex = row * FusionTableBlockEntity.BOARD_COLUMNS + col;
                int cellX = leftPos + BOARD_X + col * (SHAPE_SIZE + CELL_GAP);
                int cellY = topPos + BOARD_Y + row * (SHAPE_SIZE + CELL_GAP);
                int shapeId = menu.getBoardCell(cellIndex);

                if (shapeId >= 1 && shapeId <= FusionTableBlockEntity.SHAPE_COUNT) {
                    int[] uv = SHAPE_UV[shapeId - 1];
                    g.blit(TEXTURE, cellX, cellY, uv[0], uv[1], SHAPE_SIZE, SHAPE_SIZE);
                }

                // Hover highlight on empty cells
                if (shapeId < 1 || shapeId > FusionTableBlockEntity.SHAPE_COUNT) {
                    if (isInRect(cellX, cellY, SHAPE_SIZE, SHAPE_SIZE, mouseX, mouseY)) {
                        int highlightX = leftPos + BOARD_X - 1 + col * (SHAPE_SIZE + CELL_GAP);
                        int highlightY = topPos + BOARD_Y - 1 + row * (SHAPE_SIZE + CELL_GAP);
                        g.blit(TEXTURE, highlightX, highlightY, HOVER_UV_X, HOVER_UV_Y, HOVER_SIZE, HOVER_SIZE);
                    }
                }
            }
        }

        // Draw upcoming shapes
        int upcoming0 = menu.getUpcoming(0);
        if (upcoming0 >= 1 && upcoming0 <= FusionTableBlockEntity.SHAPE_COUNT) {
            int[] uv = SHAPE_UV[upcoming0 - 1];
            g.blit(TEXTURE, leftPos + UPCOMING_CURRENT_X, topPos + UPCOMING_CURRENT_Y, uv[0], uv[1], SHAPE_SIZE, SHAPE_SIZE);
        }

        int upcoming1 = menu.getUpcoming(1);
        if (upcoming1 >= 1 && upcoming1 <= FusionTableBlockEntity.SHAPE_COUNT) {
            int[] uv = SHAPE_UV[upcoming1 - 1];
            g.blit(TEXTURE, leftPos + UPCOMING_NEXT_X, topPos + UPCOMING_NEXT_Y, uv[0], uv[1], SHAPE_SIZE, SHAPE_SIZE);
        }

        // Draw progress bar
        int progressCount = menu.getProgress();
        for (int i = 0; i < progressCount; i++) {
            int segX = leftPos + PROGRESS_X + i * (PROGRESS_SEGMENT_W + PROGRESS_GAP);
            g.blit(TEXTURE, segX, topPos + PROGRESS_Y, PROGRESS_UV_X, PROGRESS_UV_Y, PROGRESS_SEGMENT_W, PROGRESS_SEGMENT_H);
        }

        RenderSystem.disableBlend();
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        // Don't render default title/inventory labels for this GUI
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && menu.isPuzzleActive()) {
            for (int row = 0; row < FusionTableBlockEntity.BOARD_ROWS; row++) {
                for (int col = 0; col < FusionTableBlockEntity.BOARD_COLUMNS; col++) {
                    int cellIndex = row * FusionTableBlockEntity.BOARD_COLUMNS + col;
                    int cellX = leftPos + BOARD_X + col * (SHAPE_SIZE + CELL_GAP);
                    int cellY = topPos + BOARD_Y + row * (SHAPE_SIZE + CELL_GAP);

                    if (isInRect(cellX, cellY, SHAPE_SIZE, SHAPE_SIZE, (int) mouseX, (int) mouseY)) {
                        int shapeId = menu.getBoardCell(cellIndex);
                        // Only allow clicking empty cells
                        if (shapeId < 1 || shapeId > FusionTableBlockEntity.SHAPE_COUNT) {
                            LostdepthsMod.PACKET_HANDLER.sendToServer(
                                    new FusionTablePlaceShapePacket(menu.getBlockEntity().getBlockPos(), cellIndex)
                            );
                            // Play UI click sound
                            if (this.minecraft != null && this.minecraft.player != null) {
                                this.minecraft.player.playSound(LostdepthsModSounds.GENERIC_UI_5.get(), 0.5F, 1.0F);
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean isInRect(int x, int y, int w, int h, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
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
