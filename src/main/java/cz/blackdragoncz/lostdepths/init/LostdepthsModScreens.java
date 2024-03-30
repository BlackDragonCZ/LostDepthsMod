
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package cz.blackdragoncz.lostdepths.init;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.gui.screens.MenuScreens;

import cz.blackdragoncz.lostdepths.client.gui.WSGUI2Screen;
import cz.blackdragoncz.lostdepths.client.gui.WSGUI1Screen;
import cz.blackdragoncz.lostdepths.client.gui.PortableBeaconGUIScreen;
import cz.blackdragoncz.lostdepths.client.gui.ModuleCreatorGUIScreen;
import cz.blackdragoncz.lostdepths.client.gui.ModulatorGUIScreen;
import cz.blackdragoncz.lostdepths.client.gui.MetaCollectorGUIScreen;
import cz.blackdragoncz.lostdepths.client.gui.CrystalizerGUIScreen;
import cz.blackdragoncz.lostdepths.client.gui.CompressorGUIScreen;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LostdepthsModScreens {
	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(LostdepthsModMenus.WSGUI_1.get(), WSGUI1Screen::new);
			MenuScreens.register(LostdepthsModMenus.CRYSTALIZER_GUI.get(), CrystalizerGUIScreen::new);
			MenuScreens.register(LostdepthsModMenus.COMPRESSOR_GUI.get(), CompressorGUIScreen::new);
			MenuScreens.register(LostdepthsModMenus.WSGUI_2.get(), WSGUI2Screen::new);
			MenuScreens.register(LostdepthsModMenus.MODULE_CREATOR_GUI.get(), ModuleCreatorGUIScreen::new);
			MenuScreens.register(LostdepthsModMenus.META_COLLECTOR_GUI.get(), MetaCollectorGUIScreen::new);
			MenuScreens.register(LostdepthsModMenus.PORTABLE_BEACON_GUI.get(), PortableBeaconGUIScreen::new);
			MenuScreens.register(LostdepthsModMenus.MODULATOR_GUI.get(), ModulatorGUIScreen::new);
		});
	}
}
