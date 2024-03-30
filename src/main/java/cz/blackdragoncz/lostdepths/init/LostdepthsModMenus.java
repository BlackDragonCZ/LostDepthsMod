
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package cz.blackdragoncz.lostdepths.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.MenuType;

import cz.blackdragoncz.lostdepths.world.inventory.WSGUI2Menu;
import cz.blackdragoncz.lostdepths.world.inventory.WSGUI1Menu;
import cz.blackdragoncz.lostdepths.world.inventory.PortableBeaconGUIMenu;
import cz.blackdragoncz.lostdepths.world.inventory.ModuleCreatorGUIMenu;
import cz.blackdragoncz.lostdepths.world.inventory.ModulatorGUIMenu;
import cz.blackdragoncz.lostdepths.world.inventory.MetaCollectorGUIMenu;
import cz.blackdragoncz.lostdepths.world.inventory.CrystalizerGUIMenu;
import cz.blackdragoncz.lostdepths.world.inventory.CompressorGUIMenu;
import cz.blackdragoncz.lostdepths.LostdepthsMod;

public class LostdepthsModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, LostdepthsMod.MODID);
	public static final RegistryObject<MenuType<WSGUI1Menu>> WSGUI_1 = REGISTRY.register("wsgui_1", () -> IForgeMenuType.create(WSGUI1Menu::new));
	public static final RegistryObject<MenuType<CrystalizerGUIMenu>> CRYSTALIZER_GUI = REGISTRY.register("crystalizer_gui", () -> IForgeMenuType.create(CrystalizerGUIMenu::new));
	public static final RegistryObject<MenuType<CompressorGUIMenu>> COMPRESSOR_GUI = REGISTRY.register("compressor_gui", () -> IForgeMenuType.create(CompressorGUIMenu::new));
	public static final RegistryObject<MenuType<WSGUI2Menu>> WSGUI_2 = REGISTRY.register("wsgui_2", () -> IForgeMenuType.create(WSGUI2Menu::new));
	public static final RegistryObject<MenuType<ModuleCreatorGUIMenu>> MODULE_CREATOR_GUI = REGISTRY.register("module_creator_gui", () -> IForgeMenuType.create(ModuleCreatorGUIMenu::new));
	public static final RegistryObject<MenuType<MetaCollectorGUIMenu>> META_COLLECTOR_GUI = REGISTRY.register("meta_collector_gui", () -> IForgeMenuType.create(MetaCollectorGUIMenu::new));
	public static final RegistryObject<MenuType<PortableBeaconGUIMenu>> PORTABLE_BEACON_GUI = REGISTRY.register("portable_beacon_gui", () -> IForgeMenuType.create(PortableBeaconGUIMenu::new));
	public static final RegistryObject<MenuType<ModulatorGUIMenu>> MODULATOR_GUI = REGISTRY.register("modulator_gui", () -> IForgeMenuType.create(ModulatorGUIMenu::new));
}
