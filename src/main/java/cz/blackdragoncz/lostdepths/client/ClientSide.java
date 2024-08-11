package cz.blackdragoncz.lostdepths.client;

import cz.blackdragoncz.lostdepths.client.renderer.block.AlloyWorkstationRenderer;
import cz.blackdragoncz.lostdepths.client.renderer.block.GalacticWorkstationRenderer;
import cz.blackdragoncz.lostdepths.client.renderer.block.MetaCollectorBlockEntityRenderer;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.util.ICustomHoldPose;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientSide {

    public static final ClientSide INSTANCE = new ClientSide();
    private static int elapsedTicks = 0;

    public void setup() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::registerEntityRenderers);
        MinecraftForge.EVENT_BUS.addListener(this::renderPlayer);

        final var forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(this::onClientTick);
    }

    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.type != TickEvent.Type.CLIENT || event.side != LogicalSide.CLIENT) {
            return;
        }

        elapsedTicks++;
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
