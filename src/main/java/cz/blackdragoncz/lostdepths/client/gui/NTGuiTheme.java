package cz.blackdragoncz.lostdepths.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import cz.blackdragoncz.lostdepths.LostdepthsMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * Shared drawing helpers so every NuroTech GUI is built from ld_gui_generic.png
 * (and the jei_handler.png widget atlas) and matches the Lost Depths theme.
 *
 * Method mirrors NurostarGenerator's screen: the bottom 20px strip of the
 * 176x226 texture is a reusable sprite bank holding one template slot at
 * UV (3, 226-19) that is stamped wherever a slot is needed, and a label strip
 * at UV (23, 226-19). Slots are 19px frames; the functional 18x18 slot sits
 * inset by 1px, so a frame for a functional slot at (fx,fy) is stamped at
 * (fx-1, fy-1).
 */
public final class NTGuiTheme {

    private NTGuiTheme() {}

    public static final ResourceLocation BG = LostdepthsMod.rl("textures/gui/ld_gui_generic.png");
    public static final ResourceLocation JEI = LostdepthsMod.rl("textures/gui/jei_handler.png");

    /** Texture is addressed as 176x226; the drawn window uses the top 206px. */
    public static final int TEX_W = 176;
    public static final int TEX_H = 226;

    /** Template slot frame size (19px); functional vanilla slot (18px) sits inset +1. */
    public static final int SLOT = 19;

    /** Template slot sprite UV inside the bottom sprite bank. */
    private static final int SLOT_U = 3;
    private static final int SLOT_V = TEX_H - SLOT; // 207

    /** A clean navy sample inside the blank panel, stretched to fill backgrounds. */
    private static final int FILL_U = 80;
    private static final int FILL_V = 44;

    /** Rows of real panel art (shine/bricks) to keep above the baked-inventory band. */
    private static final int PANEL_ART_H = 120;

    // Theme colors ------------------------------------------------------------
    /** Heading / title blue (same as NurostarGenerator). */
    public static final int HEADING = -16750849;
    /** Light lavender for body text, readable on the navy panel. */
    public static final int BODY = 0xFFB9C2E6;
    /** Dimmer lavender for secondary/hint text. */
    public static final int HINT = 0xFF8A93BE;

    // Special-slot tints (translucent, drawn over the themed slot) ------------
    public static final int TINT_CRYSTAL = 0x558B5CF6; // purple - crystal slots
    public static final int TINT_OUTPUT  = 0x55B8B800; // gold   - output/result slots
    public static final int TINT_PATTERN = 0x556060B0; // blue   - pattern slots

    /**
     * Draw the themed window background at any height: a flat navy body sampled
     * from the panel, with the real shine/brick panel art across the top.
     */
    public static void background(GuiGraphics g, int leftPos, int topPos, int width, int height) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        // Flat navy base stretched over the whole window.
        g.blit(BG, leftPos, topPos, width, height, FILL_U, FILL_V, 2, 2, TEX_W, TEX_H);
        // Real panel art (shine + bricks) across the machine area at the top.
        int artH = Math.min(PANEL_ART_H, height);
        g.blit(BG, leftPos, topPos, 0, 0, width, artH, TEX_W, TEX_H);
    }

    // Row 77 is the deepest row of the sheet with no brick edge crossing it, so stretching it downward leaves no vertical streaks, and because the
    // art is cut directly above it the join is seamless by construction. Tiling a taller band does not work: the diagonal shine means no vertical
    // repeat lines up.
    private static final int PANEL_STRETCH_V = 77;
    private static final int PANEL_TOP_H = PANEL_STRETCH_V + 1;
    private static final int RIM_SHADOW = 0xFF030B1D;
    private static final int RIM_EDGE = 0xFF000000;

    /**
     * Draw a full-height panel for a window with no slots. background() stops its art at 120px and leaves flat navy with no side or bottom rim below
     * that, which reads as a cropped window. Kept separate from background() so the slot-based screens are untouched.
     */
    public static void panel(GuiGraphics g, int leftPos, int topPos, int width, int height) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        int top = Math.min(PANEL_TOP_H, height);
        g.blit(BG, leftPos, topPos, 0, 0, width, top, TEX_W, TEX_H);

        int fill = height - top - 2;
        if (fill > 0)
            g.blit(BG, leftPos, topPos + top, width, fill, 0.0F, (float) PANEL_STRETCH_V, TEX_W, 1, TEX_W, TEX_H);

        // The sheet has no bottom rim to sample - the slot grid starts straight after the brick - so mirror the side rim shading.
        if (height >= 2) {
            g.fill(leftPos, topPos + height - 2, leftPos + width, topPos + height - 1, RIM_SHADOW);
            g.fill(leftPos, topPos + height - 1, leftPos + width, topPos + height, RIM_EDGE);
        }
    }

    /** Stamp one 19x19 slot frame for a functional slot whose top-left is (fx,fy). */
    public static void slot(GuiGraphics g, int leftPos, int topPos, int fx, int fy) {
        g.blit(BG, leftPos + fx - 1, topPos + fy - 1, SLOT_U, SLOT_V, SLOT, SLOT, TEX_W, TEX_H);
    }

    /** Stamp a cols x rows grid of slot frames, functional top-left origin (fx,fy), 18px pitch. */
    public static void slotGrid(GuiGraphics g, int leftPos, int topPos, int fx, int fy, int cols, int rows) {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                slot(g, leftPos, topPos, fx + c * 18, fy + r * 18);
    }

    /** Stamp the standard player inventory (3 rows + hotbar with 4px gap) at functional (fx,fy). */
    public static void playerInventory(GuiGraphics g, int leftPos, int topPos, int fx, int fy) {
        slotGrid(g, leftPos, topPos, fx, fy, 9, 3);
        slotGrid(g, leftPos, topPos, fx, fy + 3 * 18 + 4, 9, 1);
    }

    /** Overlay a translucent tint over a functional slot at (fx,fy) to mark it special. */
    public static void tint(GuiGraphics g, int leftPos, int topPos, int fx, int fy, int argb) {
        g.fill(leftPos + fx, topPos + fy, leftPos + fx + 18, topPos + fy + 18, argb);
    }
}
