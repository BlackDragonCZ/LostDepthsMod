
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package cz.blackdragoncz.lostdepths.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;

import cz.blackdragoncz.lostdepths.client.renderer.TheProtectorRenderer;
import cz.blackdragoncz.lostdepths.client.renderer.NeuroblazeRenderer;
import cz.blackdragoncz.lostdepths.client.renderer.MaelstromRenderer;
import cz.blackdragoncz.lostdepths.client.renderer.LostDarkRenderer;
import cz.blackdragoncz.lostdepths.client.renderer.GuoonRenderer;
import cz.blackdragoncz.lostdepths.client.renderer.GalaxyDragonRenderer;
import cz.blackdragoncz.lostdepths.client.renderer.FlapperRenderer;
import cz.blackdragoncz.lostdepths.client.renderer.Dm12Renderer;
import cz.blackdragoncz.lostdepths.client.renderer.AstralclawRenderer;
import cz.blackdragoncz.lostdepths.client.renderer.ArachnotaRenderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LostdepthsModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(LostdepthsModEntities.LOST_DARK.get(), LostDarkRenderer::new);
		event.registerEntityRenderer(LostdepthsModEntities.LOST_DARK_PROJECTILE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(LostdepthsModEntities.MAELSTROM.get(), MaelstromRenderer::new);
		event.registerEntityRenderer(LostdepthsModEntities.GALAXY_DRAGON.get(), GalaxyDragonRenderer::new);
		event.registerEntityRenderer(LostdepthsModEntities.GUOON.get(), GuoonRenderer::new);
		event.registerEntityRenderer(LostdepthsModEntities.FLAPPER.get(), FlapperRenderer::new);
		event.registerEntityRenderer(LostdepthsModEntities.THE_PROTECTOR.get(), TheProtectorRenderer::new);
		event.registerEntityRenderer(LostdepthsModEntities.CANE_OF_VENOM_PROJECTILE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(LostdepthsModEntities.SHOOTER_H_PROJECTILE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(LostdepthsModEntities.FLUX_LANTERN_PROJECTILE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(LostdepthsModEntities.NEUROBLAZE.get(), NeuroblazeRenderer::new);
		event.registerEntityRenderer(LostdepthsModEntities.ASTRALCLAW.get(), AstralclawRenderer::new);
		event.registerEntityRenderer(LostdepthsModEntities.BLAZE_PROJECTILE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(LostdepthsModEntities.DM_1BULLET.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(LostdepthsModEntities.BEAM_BULLET.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(LostdepthsModEntities.DM_12.get(), Dm12Renderer::new);
		event.registerEntityRenderer(LostdepthsModEntities.ARACHNOTA_PROJECTILE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(LostdepthsModEntities.ARACHNOTA.get(), ArachnotaRenderer::new);
	}
}
