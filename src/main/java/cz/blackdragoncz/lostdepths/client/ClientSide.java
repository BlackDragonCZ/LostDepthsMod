package cz.blackdragoncz.lostdepths.client;

import cz.blackdragoncz.lostdepths.client.renderer.block.AlloyWorkstationRenderer;
import cz.blackdragoncz.lostdepths.client.renderer.block.GalacticWorkstationRenderer;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientSide {

    public static final ClientSide INSTANCE = new ClientSide();

    public void setup() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::registerEntityRenderers);
    }

    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        BlockEntityRenderers.register(LostdepthsModBlockEntities.GALACTIC_WORKSTATION.get(), GalacticWorkstationRenderer::new);
        BlockEntityRenderers.register(LostdepthsModBlockEntities.ALLOY_WORKSTATION.get(), AlloyWorkstationRenderer::new);
        BlockEntityRenderers.register(LostdepthsModBlockEntities.INFUSED_SIGN.get(), SignRenderer::new);
        BlockEntityRenderers.register(LostdepthsModBlockEntities.INFUSED_HANGING_SIGN.get(), HangingSignRenderer::new);
    }

}
