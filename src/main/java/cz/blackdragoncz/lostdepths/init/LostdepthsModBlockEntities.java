
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package cz.blackdragoncz.lostdepths.init;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;

import cz.blackdragoncz.lostdepths.block.entity.ZerithiumOreBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.Workstation2BlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.GalacticWorkstationBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.SunderWoodSapEmptyBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.SunderWoodSapBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.OreEmptyBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.MorfariteOreBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.ModuleCreatorBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.ModulatorBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.MetaMaterializerBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.MetaCollectorBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.MelworiumOreBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.LightPuzzleControllerBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.LightPuzzleBBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.LightPuzzleABlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.JammerGateOffBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.JammerGateBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.JammerAccessModuleBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.GalacticCompressorBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.FireriteOreBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.ExtraTerestrialCompressorBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.DevenergyBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.CryzuliteOreBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.CrystalizerBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.BlackHoleCompressorBlockEntity;
import cz.blackdragoncz.lostdepths.LostdepthsMod;

public class LostdepthsModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, LostdepthsMod.MODID);
	public static final RegistryObject<BlockEntityType<FireriteOreBlockEntity>> FIRERITE_ORE = register("firerite_ore", LostdepthsModBlocks.FIRERITE_ORE, FireriteOreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<MelworiumOreBlockEntity>> MELWORIUM_ORE = register("melworium_ore", LostdepthsModBlocks.MELWORIUM_ORE, MelworiumOreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<MorfariteOreBlockEntity>> MORFARITE_ORE = register("morfarite_ore", LostdepthsModBlocks.MORFARITE_ORE, MorfariteOreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<GalacticWorkstationBlockEntity>> WORKSTATION_1 = register("workstation_1", LostdepthsModBlocks.WORKSTATION_1, GalacticWorkstationBlockEntity::new);
	public static final RegistryObject<BlockEntityType<GalacticCompressorBlockEntity>> GALACTIC_COMPRESSOR = register("galactic_compressor", LostdepthsModBlocks.GALACTIC_COMPRESSOR, GalacticCompressorBlockEntity::new);
	public static final RegistryObject<BlockEntityType<Workstation2BlockEntity>> WORKSTATION_2 = register("workstation_2", LostdepthsModBlocks.WORKSTATION_2, Workstation2BlockEntity::new);
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

	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<T> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}
