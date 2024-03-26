
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package cz.blackdragoncz.lostdepths.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import cz.blackdragoncz.lostdepths.client.model.Modelthe_protector;
import cz.blackdragoncz.lostdepths.client.model.Modelmr_boomer;
import cz.blackdragoncz.lostdepths.client.model.Modelcustom_model;
import cz.blackdragoncz.lostdepths.client.model.Modelcrying_ghast;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class LostdepthsModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(Modelcrying_ghast.LAYER_LOCATION, Modelcrying_ghast::createBodyLayer);
		event.registerLayerDefinition(Modelthe_protector.LAYER_LOCATION, Modelthe_protector::createBodyLayer);
		event.registerLayerDefinition(Modelcustom_model.LAYER_LOCATION, Modelcustom_model::createBodyLayer);
		event.registerLayerDefinition(Modelmr_boomer.LAYER_LOCATION, Modelmr_boomer::createBodyLayer);
	}
}
