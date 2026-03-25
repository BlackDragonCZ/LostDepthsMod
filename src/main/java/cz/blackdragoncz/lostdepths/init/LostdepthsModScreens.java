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
			MenuScreens.register(LostdepthsModMenus.PORTABLE_BEACON_GUI.get(), PortableBeaconGUIScreen::new);
			MenuScreens.register(LostdepthsModMenus.MODULATOR_GUI.get(), ModulatorGUIScreen::new);
			MenuScreens.register(LostdepthsModMenus.NUROSTAR_GENERATOR_MENU.get(), NurostarGeneratorScreen::new);
			MenuScreens.register(LostdepthsModMenus.NUROSTAR_BATTERY_MENU.get(), NurostarBatteryScreen::new);
			MenuScreens.register(LostdepthsModMenus.RESOURCE_EXTRACTOR_MENU.get(), ResourceExtractorScreen::new);
			MenuScreens.register(LostdepthsModMenus.FUSION_TABLE_MENU.get(), FusionTableScreen::new);
			MenuScreens.register(LostdepthsModMenus.SHIPMENT_FILLER_MENU.get(), ShipmentFillerScreen::new);
			MenuScreens.register(LostdepthsModMenus.NT_DRIVE_MENU.get(), NTDriveScreen::new);
			MenuScreens.register(LostdepthsModMenus.NT_TERMINAL_MENU.get(), NTTerminalScreen::new);
			MenuScreens.register(LostdepthsModMenus.NT_CRAFTING_TERMINAL_MENU.get(), NTCraftingTerminalScreen::new);
			MenuScreens.register(LostdepthsModMenus.NT_PATTERN_ENCODER_MENU.get(), NTPatternEncoderScreen::new);
			MenuScreens.register(LostdepthsModMenus.NT_PATTERN_PROVIDER_MENU.get(), NTPatternProviderScreen::new);
		});
	}
}
