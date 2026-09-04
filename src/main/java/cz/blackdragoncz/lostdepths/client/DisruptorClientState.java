package cz.blackdragoncz.lostdepths.client;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.block.creative.entity.RealityDisruptorBlockEntity.Camera;
import cz.blackdragoncz.lostdepths.block.creative.entity.RealityDisruptorBlockEntity.Debug;
import net.minecraft.client.CameraType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

// Client half of the disruptors. The server only says what to force; everything here is a client option the server cannot set.
// F1 and F5 are re-asserted every tick rather than intercepted: InputEvent.Key is not cancellable in 1.20.1, so a player
// pressing either gets one frame back before it snaps. Originals are stored on entry and restored on exit.
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LostdepthsMod.MODID)
public final class DisruptorClientState {

    private static final String DEBUG_REPLACEMENT = "no, lmao";

    private static boolean realityActive;
    private static boolean forceHideGui;
    private static Debug debugMode = Debug.NORMAL;
    private static Camera forcedCamera = Camera.FREE;
    private static String forcedShader = "";

    private static boolean gravatorActive;

    private static boolean saved;
    private static boolean guiForced;
    private static boolean cameraForced;
    private static boolean savedHideGui;
    private static CameraType savedCamera = CameraType.FIRST_PERSON;
    private static String appliedShader = "";
    private static DebugAccess debugAccess;

    private DisruptorClientState() {
    }

    public static void setReality(boolean active, boolean hideGui, Debug debug, Camera camera, String shader) {
        boolean leaving = realityActive && !active;
        realityActive = active;
        forceHideGui = hideGui;
        debugMode = debug;
        forcedCamera = camera;
        forcedShader = shader == null ? "" : shader;
        // Restore the moment the packet lands rather than on the next tick, so the HUD returning is what tells the player they are out.
        if (leaving)
            restore(Minecraft.getInstance());
    }

    // F3 is a whole overlay rather than an option, so it is cancelled at the render hook instead of re-asserted like F1/F5.
    // Nothing is left behind when the player walks out: vanilla just renders again on the next frame.
    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        if (!realityActive || debugMode == Debug.NORMAL)
            return;
        NamedGuiOverlay overlay = event.getOverlay();
        if (overlay != VanillaGuiOverlay.DEBUG_TEXT.type() && overlay != VanillaGuiOverlay.FPS_GRAPH.type())
            return;

        Minecraft mc = Minecraft.getInstance();
        // This hook runs every frame - ForgeGui.renderHUDText also carries demo and mod HUD lines, and only checks
        // renderDebug further down. Stay out of the way until the player actually presses F3.
        if (mc.options == null || !mc.options.renderDebug)
            return;

        event.setCanceled(true);
        if (overlay != VanillaGuiOverlay.DEBUG_TEXT.type())
            return;

        GuiGraphics g = event.getGuiGraphics();
        if (debugMode == Debug.FIXED) {
            g.drawString(mc.font, DEBUG_REPLACEMENT, 4, 4, 0xFFFFFF, true);
            return;
        }

        Entity camera = mc.getCameraEntity();
        if (camera == null)
            return;
        if (debugAccess == null)
            debugAccess = new DebugAccess(mc);
        drawColumn(g, mc, debugAccess.game(camera), true);
        drawColumn(g, mc, debugAccess.system(), false);
    }

    // Vanilla's own lines, each drawn obfuscated. Backing rectangles copy drawGameInformation so it still reads as F3.
    private static void drawColumn(GuiGraphics g, Minecraft mc, List<String> lines, boolean left) {
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null || line.isEmpty())
                continue;
            int height = mc.font.lineHeight;
            int width = mc.font.width(line);
            int y = 2 + height * i;
            int x = left ? 2 : g.guiWidth() - 2 - width;
            g.fill(x - 1, y - 1, x + width + 1, y + height - 1, -1873784752);
            g.drawString(mc.font, "§k" + line, x, y, 14737632, false);
        }
    }

    // getGameInformation and getSystemInformation are protected, so a throwaway subclass reaches them with no reflection.
    // block and liquid are the two picks render() performs before them, and getGameInformation dereferences both.
    private static final class DebugAccess extends DebugScreenOverlay {

        private DebugAccess(Minecraft mc) {
            super(mc);
        }

        private List<String> game(Entity camera) {
            this.block = camera.pick(20.0D, 0.0F, false);
            this.liquid = camera.pick(20.0D, 0.0F, true);
            return getGameInformation();
        }

        private List<String> system() {
            return getSystemInformation();
        }
    }

    public static void setGravator(boolean active) {
        gravatorActive = active;
    }

    public static boolean isGravatorActive() {
        return gravatorActive;
    }

    // The HUD is gone while the GUI is forced (GameRenderer skips the whole gui render), so the icon only shows when it can.
    public static boolean showRealityIcon() {
        return realityActive && !forceHideGui;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options == null)
            return;

        if (!realityActive) {
            if (saved)
                restore(mc);
            return;
        }

        if (!saved) {
            savedHideGui = mc.options.hideGui;
            savedCamera = mc.options.getCameraType();
            saved = true;
        }

        if (forceHideGui) {
            mc.options.hideGui = true;
            guiForced = true;
        }

        if (forcedCamera != Camera.FREE) {
            cameraForced = true;
            setCamera(mc, switch (forcedCamera) {
                case FIRST -> CameraType.FIRST_PERSON;
                case THIRD_BACK -> CameraType.THIRD_PERSON_BACK;
                default -> CameraType.THIRD_PERSON_FRONT;
            });
        }

        if (!forcedShader.equals(appliedShader))
            applyShader(mc, forcedShader);
    }

    private static void setCamera(Minecraft mc, CameraType wanted) {
        CameraType current = mc.options.getCameraType();
        if (current == wanted)
            return;
        mc.options.setCameraType(wanted);
        if (current.isFirstPerson() != wanted.isFirstPerson()) {
            // Same call vanilla makes when F5 cycles, or a post effect can stick. It clears postEffect, so reapply ours after.
            mc.gameRenderer.checkEntityPostEffect(wanted.isFirstPerson() ? mc.getCameraEntity() : null);
            appliedShader = "";
        }
    }

    private static void applyShader(Minecraft mc, String shader) {
        if (shader.isEmpty())
            mc.gameRenderer.shutdownEffect();
        else
            mc.gameRenderer.loadEffect(new ResourceLocation("shaders/post/" + shader + ".json"));
        appliedShader = shader;
    }

    // Only undo what was actually forced: a block set gui=false must not cancel a player's own F1.
    private static void restore(Minecraft mc) {
        if (mc.options != null) {
            if (guiForced)
                mc.options.hideGui = savedHideGui;
            if (cameraForced)
                setCamera(mc, savedCamera);
        }
        if (!appliedShader.isEmpty()) {
            mc.gameRenderer.shutdownEffect();
            appliedShader = "";
        }
        guiForced = false;
        cameraForced = false;
        saved = false;
    }
}
