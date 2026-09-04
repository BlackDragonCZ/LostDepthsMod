package cz.blackdragoncz.lostdepths.client;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.client.renderer.block.AlloyWorkstationRenderer;
import cz.blackdragoncz.lostdepths.client.renderer.block.GalacticWorkstationRenderer;
import cz.blackdragoncz.lostdepths.client.renderer.block.InfusedDisplayPlateRenderer;
import cz.blackdragoncz.lostdepths.client.renderer.block.MetaCollectorBlockEntityRenderer;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.util.ICustomHoldPose;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import cz.blackdragoncz.lostdepths.client.renderer.block.HologramProjectorRenderer;

public class ClientSide {

    public static final ClientSide INSTANCE = new ClientSide();
    private static int elapsedTicks = 0;

    private static int securityClearance = 0;
    private static char groupClearance = '0';
    private static int clearanceTime = 600;

    private static boolean warpDisrupted = false;

    private static ResourceLocation JEI = LostdepthsMod.rl("textures/gui/jei_handler.png");
    private static ResourceLocation ANTI_WARP = LostdepthsMod.rl("textures/mob_effect/anti_warp.png");
    // Sheet of 18x18 icons in the bottom-left of a 256x256 texture, grid origin (0,198), 8 columns x 3 rows.
    private static ResourceLocation POTION_ICONS = LostdepthsMod.rl("textures/mob_effect/potion_icons.png");

    public void setup() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::registerEntityRenderers);
        modEventBus.addListener(this::onClientSetup);
        MinecraftForge.EVENT_BUS.addListener(this::renderPlayer);
        MinecraftForge.EVENT_BUS.addListener(this::renderOverlay);

        final var forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(this::onClientTick);
    }

    public void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(LostdepthsModItems.INFUSED_WRITTEN_BOOK.get(),
                    LostdepthsMod.rl("contract_signed"),
                    (stack, level, entity, seed) -> {
                        CompoundTag tag = stack.getTag();
                        return tag != null && tag.contains("contract_signer") ? 1.0F : 0.0F;
                    });
        });
    }

    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.type != TickEvent.Type.CLIENT || event.side != LogicalSide.CLIENT) {
            return;
        }

        elapsedTicks++;

        if (clearanceTime > 0)
        {
            clearanceTime--;
        } else {
            securityClearance = 0;
        }
    }

    public static void setSecurityClearance(int clearance, char groupclearance) {
        securityClearance = clearance;
        groupClearance = groupclearance;

        clearanceTime = 600;
    }

    public static void setWarpDisrupted(boolean disrupted) {
        warpDisrupted = disrupted;
    }

    public static boolean isWarpDisrupted() {
        return warpDisrupted;
    }

    private Item getSecurityClearance(int clearance, char groupClearance) {
        return switch (groupClearance) {
            case 'a' ->
            switch (clearance) {
                case 1 -> LostdepthsModItems.SECURITY_PASS_1.get();
                case 2 -> LostdepthsModItems.SECURITY_PASS_2.get();
                case 3 -> LostdepthsModItems.SECURITY_PASS_3.get();
                case 4 -> LostdepthsModItems.SECURITY_PASS_4.get();
                case 5 -> LostdepthsModItems.SECURITY_PASS_5.get();
                case 6 -> LostdepthsModItems.SECURITY_PASS_6.get();
                default -> null;
            };
            case 'b' ->
                switch (clearance) {
                    case 1 -> LostdepthsModItems.SECURITY_PASS_A.get();
                    case 2 -> LostdepthsModItems.SECURITY_PASS_A.get(); //TODO: replace dynamic case system
                    default -> null;
                };
            default -> null;
        };
    }

    // sheetW/sheetH must be the full texture size: anti_warp is a lone 18x18 file, potion_icons is a 256x256 atlas.
    private static void drawIndicator(GuiGraphics g, int slot, ResourceLocation texture, int u, int v, int sheetW, int sheetH) {
        int size = 18;
        int x = g.guiWidth() / 4 - size / 2 + slot * (size + 4);
        int y = g.guiHeight() - size - 4;
        float pulse = (float) (0.6 + 0.4 * Math.sin(elapsedTicks * 0.15));
        com.mojang.blaze3d.systems.RenderSystem.setShaderColor(1, 1, 1, pulse);
        com.mojang.blaze3d.systems.RenderSystem.enableBlend();
        g.blit(texture, x, y, u, v, size, size, sheetW, sheetH);
        com.mojang.blaze3d.systems.RenderSystem.setShaderColor(1, 1, 1, 1);
        com.mojang.blaze3d.systems.RenderSystem.disableBlend();
    }

    public void renderOverlay(RenderGuiEvent.Post event)
    {
        GuiGraphics g = event.getGuiGraphics();

        // Disruption indicators, stacked left to right from the same anchor.
        int slot = 0;
        if (warpDisrupted)
            drawIndicator(g, slot++, ANTI_WARP, 0, 0, 18, 18);
        // Reality only draws when it is not hiding the GUI - GameRenderer:964 skips the whole GUI render in that case.
        if (DisruptorClientState.showRealityIcon())
            drawIndicator(g, slot++, POTION_ICONS, 0, 216, 256, 256);
        if (DisruptorClientState.isGravatorActive())
            drawIndicator(g, slot, POTION_ICONS, 54, 198, 256, 256);

        // Security clearance indicator (right side)
        if (securityClearance == 0)
            return;

        Item clearanceItem = getSecurityClearance(securityClearance, groupClearance);

        if (clearanceItem == null)
            return;

        int screenPositionCenterX = g.guiWidth() - g.guiWidth() / 4;

        int barWidth = 98;
        int barHeight = 9;
        int segmentWidth = 14;
        int segmentCount = barWidth / segmentWidth;
        float timePercent = (float) clearanceTime / 600.0f;
        int currentSegmentCount = (int) Math.ceil(timePercent * segmentCount);

        int barLeftPos = screenPositionCenterX - barWidth / 2;
        int barRightPos = screenPositionCenterX + barWidth / 2;
        g.blit(JEI, barLeftPos, g.guiHeight() - barHeight - 1, 154, 227, barWidth, barHeight, 256, 256);
        g.blit(JEI, barLeftPos, g.guiHeight() - barHeight - 1, 154, 239, currentSegmentCount * segmentWidth, barHeight, 256, 256);


        g.blit(JEI, barLeftPos - 7, g.guiHeight() - 11, 135, 237, 7, 11, 256, 256); // left bracket
        g.blit(JEI, barRightPos, g.guiHeight() - 11, 145, 237, 7, 11, 256, 256); // right bracket

        g.renderItem(new ItemStack(clearanceItem), screenPositionCenterX - 8, g.guiHeight() - 11 - 8 - 8);
    }

    public static int getElapsedTicks() {
        return elapsedTicks;
    }

    public void renderPlayer(RenderPlayerEvent.Pre event)
    {
        if (isCustomHoldPose(event.getEntity(), InteractionHand.MAIN_HAND))
        {
            event.getRenderer().getModel().rightArmPose = HumanoidModel.ArmPose.SPYGLASS;
        }

        if (isCustomHoldPose(event.getEntity(), InteractionHand.OFF_HAND))
        {
            event.getRenderer().getModel().leftArmPose = HumanoidModel.ArmPose.SPYGLASS;
        }
    }

    private boolean isCustomHoldPose(Player player, InteractionHand hand) {
        return player.getItemInHand(hand).getItem() instanceof ICustomHoldPose;
    }

    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        BlockEntityRenderers.register(LostdepthsModBlockEntities.GALACTIC_WORKSTATION.get(), GalacticWorkstationRenderer::new);
        BlockEntityRenderers.register(LostdepthsModBlockEntities.ALLOY_WORKSTATION.get(), AlloyWorkstationRenderer::new);
        BlockEntityRenderers.register(LostdepthsModBlockEntities.INFUSED_SIGN.get(), SignRenderer::new);
        BlockEntityRenderers.register(LostdepthsModBlockEntities.INFUSED_HANGING_SIGN.get(), HangingSignRenderer::new);
        BlockEntityRenderers.register(LostdepthsModBlockEntities.META_COLLECTOR.get(), MetaCollectorBlockEntityRenderer::new);
        BlockEntityRenderers.register(LostdepthsModBlockEntities.INFUSED_DISPLAY_PLATE.get(), InfusedDisplayPlateRenderer::new);
        BlockEntityRenderers.register(LostdepthsModBlockEntities.HOLOGRAM_PROJECTOR.get(), HologramProjectorRenderer::new);
    }

}
