package cz.blackdragoncz.lostdepths.init;

import cz.blackdragoncz.lostdepths.block.creative.*;
import cz.blackdragoncz.lostdepths.block.decor.*;
import cz.blackdragoncz.lostdepths.block.fluid.*;
import cz.blackdragoncz.lostdepths.block.grower.BasicTreeGrower;
import cz.blackdragoncz.lostdepths.block.ore.*;
import cz.blackdragoncz.lostdepths.block.plant.*;
import cz.blackdragoncz.lostdepths.block.power.*;
import cz.blackdragoncz.lostdepths.block.machine.*;
import cz.blackdragoncz.lostdepths.block.puzzle.*;
import cz.blackdragoncz.lostdepths.block.security.*;
import cz.blackdragoncz.lostdepths.block.structure.*;
import cz.blackdragoncz.lostdepths.block.villager.*;
import cz.blackdragoncz.lostdepths.world.LostDepthsTreeFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

import cz.blackdragoncz.lostdepths.LostdepthsMod;

public class LostdepthsModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, LostdepthsMod.MODID);
	public static final RegistryObject<Block> INFUSED_IRON_BRICKS = REGISTRY.register("infused_iron_bricks", () -> new InfusedIronBricksBlock());
	public static final RegistryObject<Block> INFUSED_IRON_PILLAR = REGISTRY.register("infused_iron_pillar", () -> new InfusedIronPillarBlock());
	public static final RegistryObject<Block> INFUSED_IRON_BRICK_STAIRS = REGISTRY.register("infused_iron_brick_stairs", () -> new InfusedIronBrickStairsBlock());
	public static final RegistryObject<Block> INFUSED_IRON_BRICK_SLAB = REGISTRY.register("infused_iron_brick_slab", () -> new InfusedIronBrickSlabBlock());
	public static final RegistryObject<Block> SPACE_ROCK = REGISTRY.register("space_rock", () -> new SpaceRockBlock());
	public static final RegistryObject<Block> SPACE_ROCK_DIRT = REGISTRY.register("space_rock_dirt", () -> new SpaceRockDirtBlock());
	public static final RegistryObject<Block> FERRO_LOG = REGISTRY.register("ferro_log", () -> new FerroLogBlock());
	public static final RegistryObject<Block> FERRO_LEAVES = REGISTRY.register("ferro_leaves", () -> new FerroLeavesBlock());
	public static final RegistryObject<Block> FIRERITE_ORE = REGISTRY.register("firerite_ore", () -> new FireriteOreBlock());
	public static final RegistryObject<Block> MELWORIUM_ORE = REGISTRY.register("melworium_ore", () -> new MelworiumOreBlock());
	public static final RegistryObject<Block> MORFARITE_ORE = REGISTRY.register("morfarite_ore", () -> new MorfariteOreBlock());
	public static final RegistryObject<Block> FACTORY_BLOCK = REGISTRY.register("factory_block", () -> new FactoryBlockBlock());
	public static final RegistryObject<Block> SHIPMENT_FILLER_BLOCK = REGISTRY.register("shipment_filler_block", () -> new ShipmentFillerBlock());
	public static final RegistryObject<Block> ITEM_CRATE = REGISTRY.register("item_crate", () -> new ItemCrateBlock());
	public static final RegistryObject<Block> O_FACTORY_BLOCK = REGISTRY.register("o_factory_block", () -> new OFactoryBlockBlock());
	public static final RegistryObject<Block> COSMIC_CARPET = REGISTRY.register("cosmic_carpet", () -> new CosmicCarpetBlock());
	public static final RegistryObject<Block> GALACTIC_WORKSTATION = REGISTRY.register("workstation_1", GalacticWorkstationBlock::new);
	public static final RegistryObject<Block> GALACTIC_COMPRESSOR = REGISTRY.register("galactic_compressor", () -> new GalacticCompressorBlock());
	public static final RegistryObject<Block> DRUIDS_FLOWER = REGISTRY.register("druids_flower", () -> new DruidsFlowerBlock());
	public static final RegistryObject<Block> QUANTUM_TRANSPORTER = REGISTRY.register("quantum_transporter", () -> new QuantumTransporterBlock());
	public static final RegistryObject<Block> INFUSEDIRON_PILLAR_CROSS = REGISTRY.register("infusediron_pillar_cross", () -> new InfusedironPillarCrossBlock());
	public static final RegistryObject<Block> INFUSED_IRON_CROSS_GLOW = REGISTRY.register("infused_iron_cross_glow", () -> new InfusedIronCrossGlowBlock());
	public static final RegistryObject<Block> NEO_GLASS = REGISTRY.register("neo_glass", () -> new NeoGlassBlock());
	public static final RegistryObject<Block> ALLOY_WORKSTATION = REGISTRY.register("workstation_2", () -> new AlloyWorkstationBlock());
	public static final RegistryObject<Block> CRYZULITE_ORE = REGISTRY.register("cryzulite_ore", () -> new CryzuliteOreBlock());
	public static final RegistryObject<Block> ZERITHIUM_ORE = REGISTRY.register("zerithium_ore", () -> new ZerithiumOreBlock());
	public static final RegistryObject<Block> INFUSED_IRON_BRICKS_A = REGISTRY.register("infused_iron_bricks_a", () -> new InfusedIronBricksABlock());
	public static final RegistryObject<Block> NEOSTEEL_POWER_BEAM = REGISTRY.register("neosteel_power_beam", () -> new NeosteelPowerBeamBlock());
	public static final RegistryObject<Block> FACTORY_PILLAR = REGISTRY.register("factory_pillar", () -> new FactoryPillarBlock());
	public static final RegistryObject<Block> SERPENTINE_ORE_UNPOWERED = REGISTRY.register("serpentine_ore_unpowered", () -> new SerpentineOreUnpoweredBlock());
	public static final RegistryObject<Block> EXTRA_TERESTRIAL_COMPRESSOR = REGISTRY.register("extra_terestrial_compressor", () -> new ExtraTerestrialCompressorBlock());
	public static final RegistryObject<Block> DEVENERGY = REGISTRY.register("devenergy", () -> new DevenergyBlock());
	public static final RegistryObject<Block> TREASUREBRICKS = REGISTRY.register("treasurebricks", () -> new TreasurebricksBlock());
	public static final RegistryObject<Block> TREASUREPILLAR = REGISTRY.register("treasurepillar", () -> new TreasurepillarBlock());
	public static final RegistryObject<Block> TREASURESTAIRS = REGISTRY.register("treasurestairs", () -> new TreasurestairsBlock());
	public static final RegistryObject<Block> TREASURESLABS = REGISTRY.register("treasureslabs", () -> new TreasureslabsBlock());
	public static final RegistryObject<Block> TREASURECROSS = REGISTRY.register("treasurecross", () -> new TreasurecrossBlock());
	public static final RegistryObject<Block> TREASURE_GLASS = REGISTRY.register("treasure_glass", () -> new TreasureGlassBlock());
	public static final RegistryObject<Block> TREASURE_CROSS_GLOW = REGISTRY.register("treasure_cross_glow", () -> new TreasureCrossGlowBlock());
	public static final RegistryObject<Block> CELESTIAL_CHEST = REGISTRY.register("celestial_chest", () -> new CelestialChestBlock());
	public static final RegistryObject<Block> TREASURE_POWER_BEAM = REGISTRY.register("treasure_power_beam", () -> new TreasurePowerBeamBlock());
	public static final RegistryObject<Block> TREASURE_NEO_SLAB = REGISTRY.register("treasure_neo_slab", () -> new TreasureNeoSlabBlock());
	public static final RegistryObject<Block> TREASURE_FACTORY_PILLAR = REGISTRY.register("treasure_factory_pillar", () -> new TreasureFactoryPillarBlock());
	public static final RegistryObject<Block> CHAIN_CROSS = REGISTRY.register("chain_cross", () -> new ChainCrossBlock());
	public static final RegistryObject<Block> CHAIN_NORMAL = REGISTRY.register("chain_normal", () -> new ChainNormalBlock());
	public static final RegistryObject<Block> FACTORY_FLOOR = REGISTRY.register("factory_floor", () -> new FactoryFloorBlock());
	public static final RegistryObject<Block> TREASURE_DARK_BRICKS = REGISTRY.register("treasure_dark_bricks", () -> new TreasureDarkBricksBlock());
	public static final RegistryObject<Block> TREASURE_DARK_TILES = REGISTRY.register("treasure_dark_tiles", () -> new TreasureDarkTilesBlock());
	public static final RegistryObject<Block> SECURITY_CLEARANCE_GATE_1 = REGISTRY.register("security_clearance_1", () -> new SecurityClearanceBlock(1, 'a'));
	public static final RegistryObject<Block> SECURITYCLEARANCEGATE_2 = REGISTRY.register("security_clearance_2", () -> new SecurityClearanceBlock(2, 'a'));
	public static final RegistryObject<Block> SECURITY_CLEARANCE_3 = REGISTRY.register("security_clearance_3", () -> new SecurityClearanceBlock(3, 'a'));
	public static final RegistryObject<Block> TREASURE_GLOWSTEEL = REGISTRY.register("treasure_glowsteel", () -> new TreasureGlowsteelBlock());
	public static final RegistryObject<Block> MODULE_CREATOR = REGISTRY.register("module_creator", () -> new ModuleCreatorBlock());
	public static final RegistryObject<Block> META_MATERIALIZER = REGISTRY.register("meta_materializer", () -> new MetaMaterializerBlock());
	public static final RegistryObject<Block> NEOSTEEL_LANTERN = REGISTRY.register("neosteel_lantern", () -> new NeosteelLanternBlock());
	public static final RegistryObject<Block> SECURITY_CLEARANCE_4 = REGISTRY.register("security_clearance_4", () -> new SecurityClearanceBlock(4, 'a'));
	public static final RegistryObject<Block> META_COLLECTOR = REGISTRY.register("meta_collector", () -> new MetaCollectorBlock());
	public static final RegistryObject<Block> LIGHT_PUZZLE_A = REGISTRY.register("light_puzzle_a", () -> new LightPuzzleABlock());
	public static final RegistryObject<Block> LIGHT_PUZZLE_CONTROLLER = REGISTRY.register("light_puzzle_controller", () -> new LightPuzzleControllerBlock());
	public static final RegistryObject<Block> CRYSTALIZER = REGISTRY.register("crystalizer", () -> new CrystalizerBlock());
	public static final RegistryObject<Block> ORE_EMPTY = REGISTRY.register("ore_empty", () -> new OreEmptyBlock());
	public static final RegistryObject<Block> LASER_GATE = REGISTRY.register("laser_gate", () -> new LaserGateBlock(1, 'a'));
	public static final RegistryObject<Block> LASER_GATE_VERTICAL = REGISTRY.register("laser_gate_vertical", () -> new LaserGateBlock(1, 'a'));
	public static final RegistryObject<Block> ENDER_LOOT_BOX = REGISTRY.register("ender_loot_box", () -> new EnderLootBoxBlock());
	public static final RegistryObject<Block> CELESTIAL_CHEST_OPEN = REGISTRY.register("celestial_chest_open", () -> new CelestialChestOpenBlock());
	public static final RegistryObject<Block> RUINED_WORKBENCH = REGISTRY.register("ruined_workbench", () -> new RuinedWorkbenchBlock());
	public static final RegistryObject<Block> RUINED_ARCH = REGISTRY.register("ruined_arch", () -> new RuinedArchBlock());
	public static final RegistryObject<Block> LASER_GATE_2 = REGISTRY.register("laser_gate_2", () -> new LaserGateBlock(2, 'a'));
	public static final RegistryObject<Block> LASER_GATE_3 = REGISTRY.register("laser_gate_3", () -> new LaserGateBlock(3, 'a'));
	public static final RegistryObject<Block> LASER_GATE_4 = REGISTRY.register("laser_gate_4", () -> new LaserGateBlock(4, 'a'));
	public static final RegistryObject<Block> SERPENTINE_ORE = REGISTRY.register("serpentine_ore", () -> new SerpentineOreBlock());
	public static final RegistryObject<Block> MANUFACTORY_GENERATOR = REGISTRY.register("manufactory_generator", () -> new ManufactoryGeneratorBlock());
	public static final RegistryObject<Block> LIGHT_PUZZLE_B = REGISTRY.register("light_puzzle_b", () -> new LightPuzzleBBlock());
	public static final RegistryObject<Block> NEOSTEEL_LANTERN_2 = REGISTRY.register("neosteel_lantern_2", () -> new NeosteelLantern2Block());
	public static final RegistryObject<Block> INFUSED_DARK_BRICKS = REGISTRY.register("infused_dark_bricks", () -> new InfusedDarkBricksBlock());
	public static final RegistryObject<Block> INFUSED_DARK_TILES = REGISTRY.register("infused_dark_tiles", () -> new InfusedDarkTilesBlock());
	public static final RegistryObject<Block> PRINT_TECH = REGISTRY.register("print_tech", () -> new PrintTechBlock());
	public static final RegistryObject<Block> POWER_TERMINAL = REGISTRY.register("power_terminal", CreativePillarBlock::new);
	public static final RegistryObject<Block> ATMOS_TECH = REGISTRY.register("atmos_tech", () -> new AtmosTechBlock());
	public static final RegistryObject<Block> CLOVINITE_ORE = REGISTRY.register("clovinite_ore", () -> new CloviniteOreBlock());
	public static final RegistryObject<Block> SUNDER_WOOD = REGISTRY.register("sunder_wood", () -> new SunderWoodBlock());
	public static final RegistryObject<Block> SUNDER_WOOD_SAP = REGISTRY.register("sunder_wood_sap", () -> new SunderWoodSapBlock());
	public static final RegistryObject<Block> SUNDER_LEAVES = REGISTRY.register("sunder_leaves", () -> new SunderLeavesBlock());
	public static final RegistryObject<Block> SUNDER_PLANKS = REGISTRY.register("sunder_planks", () -> new SunderPlanksBlock());
	public static final RegistryObject<Block> SUNDER_WOOD_SAP_EMPTY = REGISTRY.register("sunder_wood_sap_empty", () -> new SunderWoodSapEmptyBlock());
	public static final RegistryObject<Block> PARKOUR_A = REGISTRY.register("parkour_a", () -> new ParkourABlock());
	public static final RegistryObject<Block> PARKOUR_B = REGISTRY.register("parkour_b", () -> new ParkourBBlock());
	public static final RegistryObject<Block> PARKOUR_C_1 = REGISTRY.register("parkour_c_1", () -> new ParkourC1Block());
	public static final RegistryObject<Block> PARKOUR_C_2 = REGISTRY.register("parkour_c_2", () -> new ParkourC2Block());
	public static final RegistryObject<Block> CONCENTRATED_ACID = REGISTRY.register("concentrated_acid", () -> new ConcentratedAcidBlock());
	public static final RegistryObject<Block> INFUSED_IRON_BRICKS_WALL = REGISTRY.register("infused_iron_bricks_wall", () -> new InfusedIronBricksWallBlock());
	public static final RegistryObject<Block> INFUSED_IRON_BRICK_WALLS = REGISTRY.register("infused_iron_brick_walls", () -> new InfusedIronBrickWallsBlock());
	public static final RegistryObject<Block> SUNDER_STAIRS = REGISTRY.register("sunder_stairs", () -> new SunderStairsBlock());
	public static final RegistryObject<Block> SUNDER_FENCE = REGISTRY.register("sunder_fence", () -> new SunderFenceBlock());
	public static final RegistryObject<Block> SUNDER_SLAB = REGISTRY.register("sunder_slab", () -> new SunderSlabBlock());
	public static final RegistryObject<Block> BLACK_MARKET = REGISTRY.register("black_market", () -> new BlackMarketBlock());
	public static final RegistryObject<Block> PARKOUR_D = REGISTRY.register("parkour_d", () -> new ParkourDBlock());
	public static final RegistryObject<Block> PARKOUR_E = REGISTRY.register("parkour_e", () -> new ParkourEBlock());
	public static final RegistryObject<Block> PARKOUR_F = REGISTRY.register("parkour_f", () -> new ParkourFBlock());
	public static final RegistryObject<Block> HARD_CRYSTAL_B = REGISTRY.register("hard_crystal_b", () -> new HardCrystalBBlock());
	public static final RegistryObject<Block> HARD_CRYSTAL_R = REGISTRY.register("hard_crystal_r", () -> new HardCrystalRBlock());
	public static final RegistryObject<Block> JAMMER_A = REGISTRY.register("jammer_a", () -> new JammerABlock());
	public static final RegistryObject<Block> JAMMER_B = REGISTRY.register("jammer_b", () -> new JammerBBlock());
	public static final RegistryObject<Block> JAMMER_C = REGISTRY.register("jammer_c", () -> new JammerCBlock());
	public static final RegistryObject<Block> JAMMER_D = REGISTRY.register("jammer_d", () -> new JammerDBlock());
	public static final RegistryObject<Block> NITRO_LOG = REGISTRY.register("nitro_log", () -> new NitroLogBlock());
	public static final RegistryObject<Block> CHEM_TECH = REGISTRY.register("chem_tech", ChemTechBlock::new);
	public static final RegistryObject<Block> JAMMER_ACCESS_MODULE = REGISTRY.register("jammer_access_module", () -> new JammerAccessModuleBlock());
	public static final RegistryObject<Block> JAMMER_GATE = REGISTRY.register("jammer_gate", () -> new JammerGateBlock());
	public static final RegistryObject<Block> JAMMER_GATE_OFF = REGISTRY.register("jammer_gate_off", () -> new JammerGateOffBlock());
	public static final RegistryObject<Block> QUANTUM_LAB_GATE = REGISTRY.register("quantum_lab_gate", CreativeStaticBlock::new);
	public static final RegistryObject<Block> QUANTUM_LAB_GATE_LASER = REGISTRY.register("quantum_lab_gate_laser", CreativeTransparentBlock::new);
	public static final RegistryObject<Block> QUANTUM_LAB_FLOOR = REGISTRY.register("quantum_lab_floor", CreativeStaticBlock::new);
	public static final RegistryObject<Block> DRILL_HEAD = REGISTRY.register("drill_head", CreativePillarBlock::new);
	public static final RegistryObject<Block> DRILL_SHAFT = REGISTRY.register("drill_shaft", CreativePillarBlock::new);
	public static final RegistryObject<Block> DRILL_CONSOLE = REGISTRY.register("drill_console", CreativePillarBlock::new);
	public static final RegistryObject<Block> TREASURE_NEO_SLAB_BLOCK = REGISTRY.register("treasure_neo_slab_block", () -> new TreasureNeoSlabBlockBlock());
	public static final RegistryObject<Block> CHIP_FORMATTER_A = REGISTRY.register("chip_formatter_a", CreativePillarBlock::new);
	public static final RegistryObject<Block> CHIP_FORMATTER_B = REGISTRY.register("chip_formatter_b", CreativePillarBlock::new);
	public static final RegistryObject<Block> VENT_BLCOK = REGISTRY.register("vent_blcok", CreativeStaticBlock::new);
	public static final RegistryObject<Block> RESEARCH_GLASS = REGISTRY.register("research_glass", ResearchGlassBlock::new);
	public static final RegistryObject<Block> ARENA_BARRIER_A = REGISTRY.register("arena_barrier_a", CreativeTransparentBlock::new);
	public static final RegistryObject<Block> ARENA_BARRIER_B = REGISTRY.register("arena_barrier_b", CreativeTransparentBlock::new);
	public static final RegistryObject<Block> ARENA_BARRIER_C = REGISTRY.register("arena_barrier_c", CreativeTransparentBlock::new);
	public static final RegistryObject<Block> ARENA_BARRIER_D = REGISTRY.register("arena_barrier_d", CreativeTransparentBlock::new);
	public static final RegistryObject<Block> ARENA_PILLAR_SIDE = REGISTRY.register("arena_pillar_side", CreativePillarBlock::new);
	public static final RegistryObject<Block> POWER_COLLIDER = REGISTRY.register("power_collider", CreativeStaticBlock::new);
	public static final RegistryObject<Block> ARENA_PILLAR = REGISTRY.register("arena_pillar", CreativePillarBlock::new);
	public static final RegistryObject<Block> ARENA_PILLAR_B = REGISTRY.register("arena_pillar_b", CreativeStaticBlock::new);
	public static final RegistryObject<Block> ARENA_LAMP = REGISTRY.register("arena_lamp", CreativeGlowingBlock::new);
	public static final RegistryObject<Block> ARENA_SLABS = REGISTRY.register("arena_slabs", CreativePillarBlock::new);
	public static final RegistryObject<Block> BLIGHT_IDOL = REGISTRY.register("blight_idol", CreativeStaticBlock::new);
	public static final RegistryObject<Block> LUMIO_IDOL = REGISTRY.register("lumio_idol", CreativeStaticBlock::new);
	public static final RegistryObject<Block> ARENA_CONVERTER = REGISTRY.register("arena_converter", CreativeStaticBlock::new);
	public static final RegistryObject<Block> FIELD_CONTROLLER = REGISTRY.register("field_controller", CreativePillarBlock::new);
	public static final RegistryObject<Block> DARK_STEEL_GLOW_LIGHT = REGISTRY.register("dark_steel_glow_light", CreativeGlowingBlock::new);
	public static final RegistryObject<Block> DARK_STEEL_GLOW_LIGHT_A = REGISTRY.register("dark_steel_glow_light_a", CreativeGlowingBlock::new);
	public static final RegistryObject<Block> DARK_STEEL_GLOW_LIGHT_B = REGISTRY.register("dark_steel_glow_light_b", CreativeGlowingBlock::new);
	public static final RegistryObject<Block> DARK_STEEL_GLOW_LIGHT_C = REGISTRY.register("dark_steel_glow_light_c", CreativeGlowingBlock::new);
	public static final RegistryObject<Block> DARK_STEEL_GLOW_LIGHT_D = REGISTRY.register("dark_steel_glow_light_d", CreativeGlowingBlock::new);
	public static final RegistryObject<Block> PIPESPLUS = REGISTRY.register("pipesplus", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_PLUS_A = REGISTRY.register("pipes_plus_a", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_T = REGISTRY.register("pipes_t", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_TA = REGISTRY.register("pipes_ta", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_I = REGISTRY.register("pipes_i", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_IA = REGISTRY.register("pipes_ia", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_WEST = REGISTRY.register("pipes_west", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_WEST_A = REGISTRY.register("pipes_west_a", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_SOUTH = REGISTRY.register("pipes_south", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_SOUTH_A = REGISTRY.register("pipes_south_a", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_NORTH = REGISTRY.register("pipes_north", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_NORTH_A = REGISTRY.register("pipes_north_a", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_EAST = REGISTRY.register("pipes_east", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_EAST_A = REGISTRY.register("pipes_east_a", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_T_WEST = REGISTRY.register("pipes_t_west", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_T_WEST_A = REGISTRY.register("pipes_t_west_a", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_T_SOUTH = REGISTRY.register("pipes_t_south", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_T_SOUTH_A = REGISTRY.register("pipes_t_south_a", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_T_NORTH = REGISTRY.register("pipes_t_north", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_T_NORTH_A = REGISTRY.register("pipes_t_north_a", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_T_EAST = REGISTRY.register("pipes_t_east", CreativeStaticBlock::new);
	public static final RegistryObject<Block> PIPES_T_EAST_A = REGISTRY.register("pipes_t_east_a", CreativeStaticBlock::new);
	public static final RegistryObject<Block> POWER_BLOCK_BLUE = REGISTRY.register("power_block_blue", CreativeStaticBlock::new);
	public static final RegistryObject<Block> POWER_BLOCK_YELLOW = REGISTRY.register("power_block_yellow", CreativeStaticBlock::new);
	public static final RegistryObject<Block> POWER_BLOCK_PURPLE = REGISTRY.register("power_block_purple", CreativeStaticBlock::new);
	public static final RegistryObject<Block> POWER_BLOCK_GREEN = REGISTRY.register("power_block_green", CreativeStaticBlock::new);
	public static final RegistryObject<Block> ARENA_STEEL_BLACK = REGISTRY.register("arena_steel_black", CreativeStaticBlock::new);
	public static final RegistryObject<Block> ARENA_LAMP_RAZOR_A = REGISTRY.register("arena_lamp_razor_a", CreativeGlowingBlock::new);
	public static final RegistryObject<Block> ARENA_LAMP_RAZOR_B = REGISTRY.register("arena_lamp_razor_b", CreativeGlowingBlock::new);
	public static final RegistryObject<Block> ARENA_LAMP_RAZOR_C = REGISTRY.register("arena_lamp_razor_c", CreativeGlowingBlock::new);
	public static final RegistryObject<Block> ARENA_LAMP_DARK = REGISTRY.register("arena_lamp_dark", CreativeGlowingBlock::new);
	public static final RegistryObject<Block> ARENA_GLASS_BLUE = REGISTRY.register("arena_glass_blue", CreativeTransparentBlock::new);
	public static final RegistryObject<Block> ARENA_SLABS_SEA = REGISTRY.register("arena_slabs_sea", CreativeStaticBlock::new);
	public static final RegistryObject<Block> ARENA_PILLAR_SEA = REGISTRY.register("arena_pillar_sea", CreativePillarBlock::new);
	public static final RegistryObject<Block> ARENA_ANI_SLABS_SEA = REGISTRY.register("arena_ani_slabs_sea", CreativeStaticBlock::new);
	public static final RegistryObject<Block> ARENA_BARRIER = REGISTRY.register("arena_barrier", CreativeTransparentBlock::new);
	public static final RegistryObject<Block> CELESTIAL_BARRIER = REGISTRY.register("celestial_barrier", CreativeTransparentBlock::new);
	public static final RegistryObject<Block> POWER_CONDUIT = REGISTRY.register("power_conduit", CreativePillarBlock::new);
	public static final RegistryObject<Block> LABYRINTH_METAL_BLUE = REGISTRY.register("labyrinth_metal_blue", CreativeStaticBlock::new);
	public static final RegistryObject<Block> LABYRINTH_LAMP_A = REGISTRY.register("labyrinth_lamp_a", CreativeGlowingBlock::new);
	public static final RegistryObject<Block> LABYRINTH_LAMP_B = REGISTRY.register("labyrinth_lamp_b", CreativeGlowingBlock::new);
	public static final RegistryObject<Block> LABYRINTH_LAMP_C = REGISTRY.register("labyrinth_lamp_c", CreativeGlowingBlock::new);
	public static final RegistryObject<Block> ASTROSTEEL = REGISTRY.register("astrosteel", CreativeStaticBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL = REGISTRY.register("champion_steel", CreativeStaticBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL_FLOOR = REGISTRY.register("champion_steel_floor", CreativeStaticBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL_BEAM = REGISTRY.register("champion_steel_beam", CreativePillarBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL_LIGHT = REGISTRY.register("champion_steel_light", CreativeGlowingBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL_WALL = REGISTRY.register("champion_steel_wall", CreativeStaticBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL_GLASS = REGISTRY.register("champion_steel_glass", CreativeTransparentBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL_GLASS_DARK = REGISTRY.register("champion_steel_glass_dark", CreativeTransparentBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL_HEAT_OFF = REGISTRY.register("champion_steel_heat_off", CreativeStaticBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL_HEAT_ON = REGISTRY.register("champion_steel_heat_on", CreativeGlowingBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL_WIRING_A = REGISTRY.register("champion_steel_wiring_a", CreativeStaticBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL_WIRING_B = REGISTRY.register("champion_steel_wiring_b", CreativeStaticBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL_WIRING_C = REGISTRY.register("champion_steel_wiring_c", CreativeStaticBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL_WIRING_D = REGISTRY.register("champion_steel_wiring_d", CreativeStaticBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL_WIRING_E = REGISTRY.register("champion_steel_wiring_e", CreativeStaticBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL_WIRING_F = REGISTRY.register("champion_steel_wiring_f", CreativeStaticBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL_WIRING_G = REGISTRY.register("champion_steel_wiring_g", CreativeStaticBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL_WIRING_H = REGISTRY.register("champion_steel_wiring_h", CreativeStaticBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL_WIRING_I = REGISTRY.register("champion_steel_wiring_i", CreativeStaticBlock::new);
	public static final RegistryObject<Block> CHAMPION_STEEL_WIRING_J = REGISTRY.register("champion_steel_wiring_j", CreativeStaticBlock::new);
	public static final RegistryObject<Block> ITEM_CHALLENGE_COLLECTOR = REGISTRY.register("item_challenge_collector", CreativeStaticBlock::new);
	public static final RegistryObject<Block> ITEM_CHALLENGE_BLANK = REGISTRY.register("item_challenge_blank", CreativeStaticBlock::new);
	public static final RegistryObject<Block> NEON_BRICKS = REGISTRY.register("neon_bricks", CreativeStaticBlock::new);
	public static final RegistryObject<Block> NEON_BRICKS_A = REGISTRY.register("neon_bricks_a", CreativeStaticBlock::new);
	public static final RegistryObject<Block> NEON_BRICKS_B = REGISTRY.register("neon_bricks_b", CreativeStaticBlock::new);
	public static final RegistryObject<Block> NEON_BRICKS_C = REGISTRY.register("neon_bricks_c", CreativeStaticBlock::new);
	public static final RegistryObject<Block> NEON_BRICKS_D = REGISTRY.register("neon_bricks_d", CreativeStaticBlock::new);
	public static final RegistryObject<Block> NEON_BRICKS_E = REGISTRY.register("neon_bricks_e", CreativeStaticBlock::new);
	public static final RegistryObject<Block> NEON_BRICKS_F = REGISTRY.register("neon_bricks_f", CreativeStaticBlock::new);
	public static final RegistryObject<Block> NEON_BRICKS_G = REGISTRY.register("neon_bricks_g", CreativeStaticBlock::new);
	public static final RegistryObject<Block> RING_SLIDE_BUTTON = REGISTRY.register("ring_slide_button", CreativeStaticBlock::new);
	public static final RegistryObject<Block> LIGHT_RECEIVER = REGISTRY.register("light_receiver", CreativeStaticBlock::new);
	public static final RegistryObject<Block> TIMELINE_STABILIZER = REGISTRY.register("timeline_stabilizer", CreativePillarBlock::new);
	public static final RegistryObject<Block> MELODIC_SEQUENCER = REGISTRY.register("melodic_sequencer", CreativePillarBlock::new);
	public static final RegistryObject<Block> ALIGNMENT_TILE = REGISTRY.register("alignment_tile", CreativeStaticBlock::new);
	public static final RegistryObject<Block> ALIGNMENT_TILE_ON = REGISTRY.register("alignment_tile_on", CreativeGlowingBlock::new);
	public static final RegistryObject<Block> ALIGNMENT_DIAL = REGISTRY.register("alignment_dial", CreativeStaticBlock::new);
	public static final RegistryObject<Block> CONNECT_GAME = REGISTRY.register("connect_game", CreativeStaticBlock::new);
	public static final RegistryObject<Block> CONTROL_SCREEN = REGISTRY.register("control_screen", CreativeRotateTransparentBlock::new);
	public static final RegistryObject<Block> CONTROL_SCREEN_KEYBOARD = REGISTRY.register("control_screen_keyboard", CreativeStaticBlock::new);
	public static final RegistryObject<Block> CONTROL_KEYBOARD = REGISTRY.register("control_keyboard", CreativeRotateStaticBlock::new);
	public static final RegistryObject<Block> KEYBOARD_SCREEN = REGISTRY.register("keyboard_screen", CreativeRotateStaticBlock::new);
	public static final RegistryObject<Block> GRAVITOR_BLOCK = REGISTRY.register("gravitor_block", () -> new GravitorBlockBlock());
	public static final RegistryObject<Block> SECURITY_CLEARANCE_5 = REGISTRY.register("security_clearance_5", () -> new SecurityClearanceBlock(5, 'a'));
	public static final RegistryObject<Block> SECURITY_CLEARANCE_6 = REGISTRY.register("security_clearance_6", () -> new SecurityClearanceBlock(6, 'a'));
	public static final RegistryObject<Block> LASER_GATE_5 = REGISTRY.register("laser_gate_5", () -> new LaserGateBlock(5, 'a'));
	public static final RegistryObject<Block> LASER_GATE_6 = REGISTRY.register("laser_gate_6", () -> new LaserGateBlock(6, 'a'));
	public static final RegistryObject<Block> TERMINAL_MONITOR = REGISTRY.register("terminal_monitor", () -> new TerminalMonitorBlock());
	public static final RegistryObject<Block> TERMINAL_CONSOLE_1 = REGISTRY.register("terminal_console_1", () -> new TerminalConsole1Block());
	public static final RegistryObject<Block> TERMINAL_CONSOLE_2 = REGISTRY.register("terminal_console_2", () -> new TerminalConsole2Block());
	public static final RegistryObject<Block> TERMINAL_CONSOLE_3 = REGISTRY.register("terminal_console_3", () -> new TerminalConsole3Block());
	public static final RegistryObject<Block> STAR_CHEST = REGISTRY.register("star_chest", () -> new StarChestBlock());
	public static final RegistryObject<Block> STAR_CHEST_OPEN = REGISTRY.register("star_chest_open", () -> new StarChestOpenBlock());
	public static final RegistryObject<Block> DRACONIC_CHEST_OPEN = REGISTRY.register("draconic_chest_open", () -> new DraconicChestOpenBlock());
	public static final RegistryObject<Block> DRACONIC_CHEST = REGISTRY.register("draconic_chest", () -> new AbstractLockedChestBlock(new ResourceLocation("lostdepths:chests/celestial_chest"), LostdepthsModBlocks.DRACONIC_CHEST_OPEN.get()) {
		@Override
		protected Item getKeyItem() {
			return LostdepthsModItems.DRACONIC_KEY.get();
		}
	});
	public static final RegistryObject<Block> DRACONIC_FLAG = REGISTRY.register("draconic_flag", () -> new DraconicFlagBlock());
	public static final RegistryObject<Block> ULTRA_RESISTIVE_GLASS = REGISTRY.register("ultra_resistive_glass", () -> new UltraResistiveGlassBlock());
	public static final RegistryObject<Block> MYRITE_ORE = REGISTRY.register("myrite_ore", () -> new MyriteOreBlock());
	public static final RegistryObject<Block> MYRITE_ORE_ACTIVE = REGISTRY.register("myrite_ore_active", () -> new MyriteOreActiveBlock());
	public static final RegistryObject<Block> LUCIENT_ORE = REGISTRY.register("lucient_ore", () -> new LucientOreBlock());
	public static final RegistryObject<Block> LUCIENT_ORE_ACTIVE = REGISTRY.register("lucient_ore_active", () -> new LucientOreActiveBlock());
	public static final RegistryObject<Block> HYPERIUM_ORE = REGISTRY.register("hyperium_ore", () -> new HyperiumOreBlock());
	public static final RegistryObject<Block> PSYCHERIUM_ORE = REGISTRY.register("psycherium_ore", () -> new PsycheriumOreBlock());
	public static final RegistryObject<Block> VARLLERIUM_ORE = REGISTRY.register("varllerium_ore", () -> new VarlleriumOreBlock());
	public static final RegistryObject<Block> BIOLLITERITE_ORE = REGISTRY.register("biolliterite_ore", () -> new BiolliteriteOreBlock());
	public static final RegistryObject<Block> NECROTONITE_ORE = REGISTRY.register("necrotonite_ore", () -> new NecrotoniteOreBlock());
	public static final RegistryObject<Block> NOXHERTIUM_ORE = REGISTRY.register("noxhertium_ore", () -> new NoxhertiumOreBlock());
	public static final RegistryObject<Block> COGNITIUM_ORE = REGISTRY.register("cognitium_ore", () -> new CognitiumOreBlock());
	public static final RegistryObject<Block> MULTIVERSITE_ORE = REGISTRY.register("multiversite_ore", () -> new MultiversiteOreBlock());
	public static final RegistryObject<Block> PHOTOTENZYTE_ORE = REGISTRY.register("phototenzyte_ore", () -> new PhototenzyteOreBlock());
	public static final RegistryObject<Block> PHOTOTENZYTE_ORE_ACTIVE = REGISTRY.register("phototenzyte_ore_active", () -> new PhototenzyteOreActiveBlock());
	public static final RegistryObject<Block> DEEP_SPACE_ROCK = REGISTRY.register("deep_space_rock", () -> new DeepSpaceRockBlock());
	public static final RegistryObject<Block> FUNGAL_SPACE_ROCK = REGISTRY.register("fungal_space_rock", () -> new FungalSpaceRockBlock());
	public static final RegistryObject<Block> CELESTIAL_LOGS_BLUE = REGISTRY.register("celestial_logs_blue", () -> new CelestialLogsBlueBlock());
	public static final RegistryObject<Block> CELESTIAL_LOGS_RED = REGISTRY.register("celestial_logs_red", () -> new CelestialLogsRedBlock());
	public static final RegistryObject<Block> CELESTIAL_LEAVES_BLUE = REGISTRY.register("celestial_leaves_blue", () -> new CelestialLeavesBlueBlock());
	public static final RegistryObject<Block> CELESTIAL_LEAVES_RED = REGISTRY.register("celestial_leaves_red", () -> new CelestialLeavesRedBlock());
	public static final RegistryObject<Block> MODULATOR = REGISTRY.register("modulator", () -> new ModulatorBlock());
	public static final RegistryObject<Block> BLACK_HOLE_COMPRESSOR = REGISTRY.register("black_hole_compressor", () -> new BlackHoleCompressorBlock());
	public static final RegistryObject<Block> NUROSTAR_GENERATOR = REGISTRY.register("nurostar_generator", NurostarGeneratorBlock::new);
	public static final RegistryObject<Block> NUROSTAR_CABLE = REGISTRY.register("nurostar_cable", NurostarCableBlock::new);
	public static final RegistryObject<Block> INFUSED_SIGN = REGISTRY.register("infused_sign", InfusedSignBlock::new);
	public static final RegistryObject<Block> INFUSED_WALL_SIGN = REGISTRY.register("infused_wall_sign", InfusedWallSignBlock::new);
	public static final RegistryObject<Block> INFUSED_HANGING_SIGN = REGISTRY.register("infused_hanging_sign", InfusedHangingSignBlock::new);
	public static final RegistryObject<Block> INFUSED_WALL_HANGING_SIGN = REGISTRY.register("infused_wall_hanging_sign", InfusedWallHangingSignBlock::new);
	public static final RegistryObject<Block> FUSION_TABLE = REGISTRY.register("fusion_table", FusionTableBlock::new);
	public static final RegistryObject<Block> INFUSED_DISPLAY_PLATE = REGISTRY.register("infused_display_plate", InfusedDisplayPlateBlock::new);
	public static final RegistryObject<Block> POSITIVE_SAPLING = REGISTRY.register("positive_sapling", () -> new SaplingBlock(new BasicTreeGrower(LostDepthsTreeFeatures.CELESTIAL_TREE_BLUE), BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));
	public static final RegistryObject<Block> NEGATIVE_SAPLING = REGISTRY.register("negative_sapling", () -> new SaplingBlock(new BasicTreeGrower(LostDepthsTreeFeatures.CELESTIAL_TREE_RED), BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));
	public static final RegistryObject<Block> NUROSTAR_BATTERY = REGISTRY.register("nurostar_battery", NurostarBatteryBlock::new);
	public static final RegistryObject<Block> NUROSTAR_LARGE_BATTERY = REGISTRY.register("nurostar_large_battery", NurostarLargeBatteryBlock::new);
	public static final RegistryObject<Block> WORMHOLE_DISRUPTOR = REGISTRY.register("wormhole_disruptor", WormholeDisruptorBlock::new);
	public static final RegistryObject<Block> SECURITY_CLEARANCE_A = REGISTRY.register("security_clearance_a", () -> new SecurityClearanceBlock(1, 'b'));
	public static final RegistryObject<Block> LASER_GATE_A = REGISTRY.register("laser_gate_a", () -> new LaserGateBlock(1, 'b'));
	//public static final RegistryObject<Block> RESOURCE_EXTRACTOR = REGISTRY.register("resource_extractor", ResourceExtractor::new);
}
