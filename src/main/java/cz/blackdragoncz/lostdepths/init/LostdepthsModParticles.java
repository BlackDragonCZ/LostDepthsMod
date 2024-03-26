
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package cz.blackdragoncz.lostdepths.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.api.distmarker.Dist;

import cz.blackdragoncz.lostdepths.client.particle.VenomparticleParticle;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LostdepthsModParticles {
	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(LostdepthsModParticleTypes.VENOMPARTICLE.get(), VenomparticleParticle::provider);
	}
}
