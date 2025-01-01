
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package cz.blackdragoncz.lostdepths.init;

import cz.blackdragoncz.lostdepths.block.entity.*;
import cz.blackdragoncz.lostdepths.block.power.entity.NurostarCableBlockEntity;
import cz.blackdragoncz.lostdepths.block.power.entity.NurostarGeneratorBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;

import cz.blackdragoncz.lostdepths.LostdepthsMod;

public class LostdepthsModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, LostdepthsMod.MODID);
	public static final RegistryObject<BlockEntityType<FireriteOreBlockEntity>> FIRERITE_ORE = register("firerite_ore", LostdepthsModBlocks.FIRERITE_ORE, FireriteOreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<MelworiumOreBlockEntity>> MELWORIUM_ORE = register("melworium_ore", LostdepthsModBlocks.MELWORIUM_ORE, MelworiumOreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<MorfariteOreBlockEntity>> MORFARITE_ORE = register("morfarite_ore", LostdepthsModBlocks.MORFARITE_ORE, MorfariteOreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<GalacticWorkstationBlockEntity>> GALACTIC_WORKSTATION = register("workstation_1", LostdepthsModBlocks.GALACTIC_WORKSTATION, GalacticWorkstationBlockEntity::new);
	public static final RegistryObject<BlockEntityType<GalacticCompressorBlockEntity>> GALACTIC_COMPRESSOR = register("galactic_compressor", LostdepthsModBlocks.GALACTIC_COMPRESSOR, GalacticCompressorBlockEntity::new);
	public static final RegistryObject<BlockEntityType<AlloyWorkstationBlockEntity>> ALLOY_WORKSTATION = register("workstation_2", LostdepthsModBlocks.ALLOY_WORKSTATION, AlloyWorkstationBlockEntity::new);
	public static final RegistryObject<BlockEntityType<CryzuliteOreBlockEntity>> CRYZULITE_ORE = register("cryzulite_ore", LostdepthsModBlocks.CRYZULITE_ORE, CryzuliteOreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<ZerithiumOreBlockEntity>> ZERITHIUM_ORE = register("zerithium_ore", LostdepthsModBlocks.ZERITHIUM_ORE, ZerithiumOreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<ExtraTerestrialCompressorBlockEntity>> EXTRA_TERESTRIAL_COMPRESSOR = register("extra_terestrial_compressor", LostdepthsModBlocks.EXTRA_TERESTRIAL_COMPRESSOR, ExtraTerestrialCompressorBlockEntity::new);
	public static final RegistryObject<BlockEntityType<DevenergyBlockEntity>> DEVENERGY = register("devenergy", LostdepthsModBlocks.DEVENERGY, DevenergyBlockEntity::new);
	public static final RegistryObject<BlockEntityType<ModuleCreatorBlockEntity>> MODULE_CREATOR = register("module_creator", LostdepthsModBlocks.MODULE_CREATOR, ModuleCreatorBlockEntity::new);
	public static final RegistryObject<BlockEntityType<MetaMaterializerBlockEntity>> META_MATERIALIZER = register("meta_materializer", LostdepthsModBlocks.META_MATERIALIZER, MetaMaterializerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<MetaCollectorBlockEntity>> META_COLLECTOR = register("meta_collector", LostdepthsModBlocks.META_COLLECTOR, MetaCollectorBlockEntity::new);
	public static final RegistryObject<BlockEntityType<LightPuzzleABlockEntity>> LIGHT_PUZZLE_A = register("light_puzzle_a", LostdepthsModBlocks.LIGHT_PUZZLE_A, LightPuzzleABlockEntity::new);
	public static final RegistryObject<BlockEntityType<LightPuzzleControllerBlockEntity>> LIGHT_PUZZLE_CONTROLLER = register("light_puzzle_controller", LostdepthsModBlocks.LIGHT_PUZZLE_CONTROLLER, LightPuzzleControllerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<CrystalizerBlockEntity>> CRYSTALIZER = register("crystalizer", LostdepthsModBlocks.CRYSTALIZER, CrystalizerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<OreEmptyBlockEntity>> ORE_EMPTY = register("ore_empty", LostdepthsModBlocks.ORE_EMPTY, OreEmptyBlockEntity::new);
	public static final RegistryObject<BlockEntityType<LightPuzzleBBlockEntity>> LIGHT_PUZZLE_B = register("light_puzzle_b", LostdepthsModBlocks.LIGHT_PUZZLE_B, LightPuzzleBBlockEntity::new);
	public static final RegistryObject<BlockEntityType<SunderWoodSapBlockEntity>> SUNDER_WOOD_SAP = register("sunder_wood_sap", LostdepthsModBlocks.SUNDER_WOOD_SAP, SunderWoodSapBlockEntity::new);
	public static final RegistryObject<BlockEntityType<SunderWoodSapEmptyBlockEntity>> SUNDER_WOOD_SAP_EMPTY = register("sunder_wood_sap_empty", LostdepthsModBlocks.SUNDER_WOOD_SAP_EMPTY, SunderWoodSapEmptyBlockEntity::new);
	public static final RegistryObject<BlockEntityType<JammerAccessModuleBlockEntity>> JAMMER_ACCESS_MODULE = register("jammer_access_module", LostdepthsModBlocks.JAMMER_ACCESS_MODULE, JammerAccessModuleBlockEntity::new);
	public static final RegistryObject<BlockEntityType<JammerGateBlockEntity>> JAMMER_GATE = register("jammer_gate", LostdepthsModBlocks.JAMMER_GATE, JammerGateBlockEntity::new);
	public static final RegistryObject<BlockEntityType<JammerGateOffBlockEntity>> JAMMER_GATE_OFF = register("jammer_gate_off", LostdepthsModBlocks.JAMMER_GATE_OFF, JammerGateOffBlockEntity::new);
	public static final RegistryObject<BlockEntityType<ModulatorBlockEntity>> MODULATOR = register("modulator", LostdepthsModBlocks.MODULATOR, ModulatorBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlackHoleCompressorBlockEntity>> BLACK_HOLE_COMPRESSOR = register("black_hole_compressor", LostdepthsModBlocks.BLACK_HOLE_COMPRESSOR, BlackHoleCompressorBlockEntity::new);
	public static final RegistryObject<BlockEntityType<NurostarCableBlockEntity>> NUROSTAR_CABLE = register("nurostar_cable", LostdepthsModBlocks.NUROSTAR_CABLE, NurostarCableBlockEntity::new);
	public static final RegistryObject<BlockEntityType<NurostarGeneratorBlockEntity>> NUROSTAR_GENRATOR = register("nurostar_generator", LostdepthsModBlocks.NUROSTAR_GENERATOR, NurostarGeneratorBlockEntity::new);
	public static final RegistryObject<BlockEntityType<InfusedSignBlockEntity>> INFUSED_SIGN = REGISTRY.register("infused_sign", () -> BlockEntityType.Builder.of(InfusedSignBlockEntity::new, LostdepthsModBlocks.INFUSED_SIGN.get(), LostdepthsModBlocks.INFUSED_WALL_SIGN.get()).build(null));
	public static final RegistryObject<BlockEntityType<InfusedHangingSignBlockEntity>> INFUSED_HANGING_SIGN = REGISTRY.register("infused_hanging_sign", () -> BlockEntityType.Builder.of(InfusedHangingSignBlockEntity::new, LostdepthsModBlocks.INFUSED_HANGING_SIGN.get(), LostdepthsModBlocks.INFUSED_WALL_HANGING_SIGN.get()).build(null));
	//public static final RegistryObject<BlockEntityType<ResourceExtractorBlockEntity>> RESOURCE_EXTRACTOR = REGISTRY.register("resource_extractor", LostdepthsModBlocks.RESOURCE_EXTRACTOR, ResourceExtractorBlockEntity::new);

	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<T> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}
