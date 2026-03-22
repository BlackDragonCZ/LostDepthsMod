package cz.blackdragoncz.lostdepths.init;

import cz.blackdragoncz.lostdepths.world.inventory.*;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.MenuType;

import cz.blackdragoncz.lostdepths.world.inventory.GalacticWorkstationMenu;
import cz.blackdragoncz.lostdepths.LostdepthsMod;

public class LostdepthsModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, LostdepthsMod.MODID);
	public static final RegistryObject<MenuType<GalacticWorkstationMenu>> GALACTIC_WORKSTATION_MENU = REGISTRY.register("wsgui_1", () -> IForgeMenuType.create(GalacticWorkstationMenu::new));
	public static final RegistryObject<MenuType<CrystalizerGUIMenu>> CRYSTALIZER_GUI = REGISTRY.register("crystalizer_gui", () -> IForgeMenuType.create(CrystalizerGUIMenu::new));
	public static final RegistryObject<MenuType<CompressorGUIMenu>> COMPRESSOR_GUI = REGISTRY.register("compressor_gui", () -> IForgeMenuType.create(CompressorGUIMenu::new));
	public static final RegistryObject<MenuType<AlloyWorkstationMenu>> ALLOY_WORKSTATION_MENU = REGISTRY.register("wsgui_2", () -> IForgeMenuType.create(AlloyWorkstationMenu::new));
	public static final RegistryObject<MenuType<ModuleCreatorGUIMenu>> MODULE_CREATOR_GUI = REGISTRY.register("module_creator_gui", () -> IForgeMenuType.create(ModuleCreatorGUIMenu::new));
	public static final RegistryObject<MenuType<PortableBeaconGUIMenu>> PORTABLE_BEACON_GUI = REGISTRY.register("portable_beacon_gui", () -> IForgeMenuType.create(PortableBeaconGUIMenu::new));
	public static final RegistryObject<MenuType<ModulatorGUIMenu>> MODULATOR_GUI = REGISTRY.register("modulator_gui", () -> IForgeMenuType.create(ModulatorGUIMenu::new));
	public static final RegistryObject<MenuType<NurostarGeneratorMenu>> NUROSTAR_GENERATOR_MENU = REGISTRY.register("nurostar_generator_menu", () -> IForgeMenuType.create(NurostarGeneratorMenu::new));
	public static final RegistryObject<MenuType<NurostarBatteryMenu>> NUROSTAR_BATTERY_MENU = REGISTRY.register("nurostar_battery_menu", () -> IForgeMenuType.create(NurostarBatteryMenu::new));
	public static final RegistryObject<MenuType<ResourceExtractorMenu>> RESOURCE_EXTRACTOR_MENU = REGISTRY.register("resource_extractor_menu", () -> IForgeMenuType.create(ResourceExtractorMenu::new));
	public static final RegistryObject<MenuType<FusionTableMenu>> FUSION_TABLE_MENU = REGISTRY.register("fusion_table_menu", () -> IForgeMenuType.create(FusionTableMenu::new));
	public static final RegistryObject<MenuType<ShipmentFillerMenu>> SHIPMENT_FILLER_MENU = REGISTRY.register("shipment_filler_menu", () -> IForgeMenuType.create(ShipmentFillerMenu::new));
	public static final RegistryObject<MenuType<NTDriveMenu>> NT_DRIVE_MENU = REGISTRY.register("nt_drive_menu", () -> IForgeMenuType.create(NTDriveMenu::new));
	public static final RegistryObject<MenuType<NTTerminalMenu>> NT_TERMINAL_MENU = REGISTRY.register("nt_terminal_menu", () -> IForgeMenuType.create(NTTerminalMenu::new));
	public static final RegistryObject<MenuType<NTCraftingTerminalMenu>> NT_CRAFTING_TERMINAL_MENU = REGISTRY.register("nt_crafting_terminal_menu", () -> IForgeMenuType.create(NTCraftingTerminalMenu::new));
}
