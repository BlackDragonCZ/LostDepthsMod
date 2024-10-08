
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
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
}
