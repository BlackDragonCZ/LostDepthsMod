package cz.blackdragoncz.lostdepths.client;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.client.renderer.block.AlloyWorkstationRenderer;
import cz.blackdragoncz.lostdepths.client.renderer.block.GalacticWorkstationRenderer;
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
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientSide {

    public static final ClientSide INSTANCE = new ClientSide();
    private static int elapsedTicks = 0;

    private static int securityClearance = 0;
    private static char groupClearance = '0';
    private static int clearanceTime = 600;

    private static ResourceLocation JEI = LostdepthsMod.rl("textures/gui/jei_handler.png");

    public void setup() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::registerEntityRenderers);
        MinecraftForge.EVENT_BUS.addListener(this::renderPlayer);
        MinecraftForge.EVENT_BUS.addListener(this::renderOverlay);

        final var forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(this::onClientTick);
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

    private Item getSecurityClearance(int clearance) {
        return switch (clearance) {
            case 1 -> LostdepthsModItems.SECURITY_PASS_1.get();
            case 2 -> LostdepthsModItems.SECURITY_PASS_2.get();
            case 3 -> LostdepthsModItems.SECURITY_PASS_3.get();
            case 4 -> LostdepthsModItems.SECURITY_PASS_4.get();
            case 5 -> LostdepthsModItems.SECURITY_PASS_5.get();
            case 6 -> LostdepthsModItems.SECURITY_PASS_6.get();
            case 7 -> LostdepthsModItems.SECURITY_PASS_A.get();
            default -> null;
        };
    }

    public void renderOverlay(RenderGuiEvent.Post event)
    {
        GuiGraphics g = event.getGuiGraphics();

        if (securityClearance == 0)
            return;

        Item clearanceItem = getSecurityClearance(securityClearance);

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
    }

}
