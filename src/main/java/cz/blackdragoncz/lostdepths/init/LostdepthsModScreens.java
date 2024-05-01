
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package cz.blackdragoncz.lostdepths.init;

import cz.blackdragoncz.lostdepths.client.gui.*;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.gui.screens.MenuScreens;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LostdepthsModScreens {
	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(LostdepthsModMenus.GALACTIC_WORKSTATION_MENU.get(), GalacticWorkstationScreen::new);
			MenuScreens.register(LostdepthsModMenus.CRYSTALIZER_GUI.get(), CrystalizerGUIScreen::new);
			MenuScreens.register(LostdepthsModMenus.COMPRESSOR_GUI.get(), CompressorGUIScreen::new);
			MenuScreens.register(LostdepthsModMenus.ALLOY_WORKSTATION_MENU.get(), AlloyWorkstationScreen::new);
			MenuScreens.register(LostdepthsModMenus.MODULE_CREATOR_GUI.get(), ModuleCreatorGUIScreen::new);
			MenuScreens.register(LostdepthsModMenus.META_COLLECTOR_GUI.get(), MetaCollectorGUIScreen::new);
			MenuScreens.register(LostdepthsModMenus.PORTABLE_BEACON_GUI.get(), PortableBeaconGUIScreen::new);
			MenuScreens.register(LostdepthsModMenus.MODULATOR_GUI.get(), ModulatorGUIScreen::new);
			MenuScreens.register(LostdepthsModMenus.NUROSTAR_GENERATOR_MENU.get(), NurostarGeneratorScreen::new);
		});
	}
}
