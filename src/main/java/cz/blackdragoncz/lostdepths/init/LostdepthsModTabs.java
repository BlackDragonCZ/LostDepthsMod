
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package cz.blackdragoncz.lostdepths.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import cz.blackdragoncz.lostdepths.LostdepthsMod;

public class LostdepthsModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LostdepthsMod.MODID);
	public static final RegistryObject<CreativeModeTab> LD_MAIN = REGISTRY.register("ld_main",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.lostdepths.ld_main")).icon(() -> new ItemStack(LostdepthsModBlocks.INFUSED_IRON_BRICKS.get())).displayItems((parameters, tabData) -> {
				tabData.accept(LostdepthsModBlocks.INFUSED_IRON_BRICKS.get().asItem());
				tabData.accept(LostdepthsModBlocks.INFUSED_IRON_PILLAR.get().asItem());
				tabData.accept(LostdepthsModBlocks.INFUSED_IRON_BRICK_STAIRS.get().asItem());
				tabData.accept(LostdepthsModBlocks.INFUSED_IRON_BRICK_SLAB.get().asItem());
				tabData.accept(LostdepthsModBlocks.SPACE_ROCK.get().asItem());
				tabData.accept(LostdepthsModBlocks.SPACE_ROCK_DIRT.get().asItem());
				tabData.accept(LostdepthsModBlocks.FERRO_LOG.get().asItem());
				tabData.accept(LostdepthsModBlocks.FERRO_LEAVES.get().asItem());
				tabData.accept(LostdepthsModBlocks.FIRERITE_ORE.get().asItem());
				tabData.accept(LostdepthsModBlocks.MELWORIUM_ORE.get().asItem());
				tabData.accept(LostdepthsModBlocks.MORFARITE_ORE.get().asItem());
				tabData.accept(LostdepthsModBlocks.FACTORY_BLOCK.get().asItem());
				tabData.accept(LostdepthsModBlocks.O_FACTORY_BLOCK.get().asItem());
				tabData.accept(LostdepthsModBlocks.COSMIC_CARPET.get().asItem());
				tabData.accept(LostdepthsModBlocks.WORKSTATION_1.get().asItem());
				tabData.accept(LostdepthsModBlocks.GALACTIC_COMPRESSOR.get().asItem());
				tabData.accept(LostdepthsModBlocks.DRUIDS_FLOWER.get().asItem());
				tabData.accept(LostdepthsModBlocks.QUANTUM_TRANSPORTER.get().asItem());
				tabData.accept(LostdepthsModItems.SECURITY_PASS_1.get());
				tabData.accept(LostdepthsModItems.SECURITY_PASS_2.get());
				tabData.accept(LostdepthsModItems.SECURITY_PASS_3.get());
				tabData.accept(LostdepthsModBlocks.INFUSEDIRON_PILLAR_CROSS.get().asItem());
				tabData.accept(LostdepthsModBlocks.INFUSED_IRON_CROSS_GLOW.get().asItem());
				tabData.accept(LostdepthsModBlocks.NEO_GLASS.get().asItem());
				tabData.accept(LostdepthsModBlocks.WORKSTATION_2.get().asItem());
				tabData.accept(LostdepthsModBlocks.CRYZULITE_ORE.get().asItem());
				tabData.accept(LostdepthsModBlocks.ZERITHIUM_ORE.get().asItem());
				tabData.accept(LostdepthsModBlocks.INFUSED_IRON_BRICKS_A.get().asItem());
				tabData.accept(LostdepthsModBlocks.NEOSTEEL_POWER_BEAM.get().asItem());
				tabData.accept(LostdepthsModBlocks.FACTORY_PILLAR.get().asItem());
				tabData.accept(LostdepthsModBlocks.SERPENTINE_ORE_UNPOWERED.get().asItem());
				tabData.accept(LostdepthsModBlocks.EXTRA_TERESTRIAL_COMPRESSOR.get().asItem());
				tabData.accept(LostdepthsModBlocks.CELESTIAL_CHEST.get().asItem());
				tabData.accept(LostdepthsModBlocks.NEOSTEEL_LANTERN_2.get().asItem());
				tabData.accept(LostdepthsModBlocks.INFUSED_DARK_BRICKS.get().asItem());
				tabData.accept(LostdepthsModBlocks.INFUSED_DARK_TILES.get().asItem());
				tabData.accept(LostdepthsModBlocks.PRINT_TECH.get().asItem());
				tabData.accept(LostdepthsModItems.SECURITY_PASS_4.get());
				tabData.accept(LostdepthsModItems.SECURITY_PASS_5.get());
				tabData.accept(LostdepthsModItems.SECURITY_PASS_6.get());
				tabData.accept(LostdepthsModBlocks.ATMOS_TECH.get().asItem());
				tabData.accept(LostdepthsModBlocks.CLOVINITE_ORE.get().asItem());
				tabData.accept(LostdepthsModBlocks.SUNDER_WOOD.get().asItem());
				tabData.accept(LostdepthsModBlocks.SUNDER_WOOD_SAP.get().asItem());
				tabData.accept(LostdepthsModBlocks.SUNDER_LEAVES.get().asItem());
				tabData.accept(LostdepthsModBlocks.SUNDER_PLANKS.get().asItem());
				tabData.accept(LostdepthsModItems.CONCENTRATED_ACID_BUCKET.get());
				tabData.accept(LostdepthsModBlocks.INFUSED_IRON_BRICKS_WALL.get().asItem());
				tabData.accept(LostdepthsModBlocks.INFUSED_IRON_BRICK_WALLS.get().asItem());
				tabData.accept(LostdepthsModBlocks.SUNDER_STAIRS.get().asItem());
				tabData.accept(LostdepthsModBlocks.SUNDER_FENCE.get().asItem());
				tabData.accept(LostdepthsModBlocks.SUNDER_SLAB.get().asItem());
				tabData.accept(LostdepthsModBlocks.BLACK_MARKET.get().asItem());
				tabData.accept(LostdepthsModBlocks.HARD_CRYSTAL_B.get().asItem());
				tabData.accept(LostdepthsModBlocks.HARD_CRYSTAL_R.get().asItem());
				tabData.accept(LostdepthsModBlocks.NITRO_LOG.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHEM_TECH.get().asItem());
				tabData.accept(LostdepthsModBlocks.GRAVITOR_BLOCK.get().asItem());
				tabData.accept(LostdepthsModItems.INFUSED_GOLEM_ESSENCE.get());
				tabData.accept(LostdepthsModBlocks.STAR_CHEST.get().asItem());
				tabData.accept(LostdepthsModBlocks.DRACONIC_CHEST.get().asItem());
				tabData.accept(LostdepthsModBlocks.DRACONIC_FLAG.get().asItem());
				tabData.accept(LostdepthsModBlocks.ULTRA_RESISTIVE_GLASS.get().asItem());
				tabData.accept(LostdepthsModBlocks.LUCIENT_ORE.get().asItem());
				tabData.accept(LostdepthsModBlocks.HYPERIUM_ORE.get().asItem());
				tabData.accept(LostdepthsModBlocks.PSYCHERIUM_ORE.get().asItem());
				tabData.accept(LostdepthsModBlocks.VARLLERIUM_ORE.get().asItem());
				tabData.accept(LostdepthsModBlocks.BIOLLITERITE_ORE.get().asItem());
				tabData.accept(LostdepthsModBlocks.NECROTONITE_ORE.get().asItem());
				tabData.accept(LostdepthsModBlocks.NOXHERTIUM_ORE.get().asItem());
				tabData.accept(LostdepthsModBlocks.COGNITIUM_ORE.get().asItem());
				tabData.accept(LostdepthsModBlocks.MULTIVERSITE_ORE.get().asItem());
				tabData.accept(LostdepthsModBlocks.PHOTOTENZYTE_ORE.get().asItem());
				tabData.accept(LostdepthsModBlocks.PHOTOTENZYTE_ORE_ACTIVE.get().asItem());
				tabData.accept(LostdepthsModBlocks.DEEP_SPACE_ROCK.get().asItem());
				tabData.accept(LostdepthsModBlocks.FUNGAL_SPACE_ROCK.get().asItem());
				tabData.accept(LostdepthsModBlocks.CELESTIAL_LOGS_BLUE.get().asItem());
				tabData.accept(LostdepthsModBlocks.CELESTIAL_LOGS_RED.get().asItem());
				tabData.accept(LostdepthsModBlocks.CELESTIAL_LEAVES_BLUE.get().asItem());
				tabData.accept(LostdepthsModBlocks.CELESTIAL_LEAVES_RED.get().asItem());
				tabData.accept(LostdepthsModBlocks.MODULATOR.get().asItem());
			})

					.build());
	public static final RegistryObject<CreativeModeTab> LD_INGOTS = REGISTRY.register("ld_ingots",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.lostdepths.ld_ingots")).icon(() -> new ItemStack(LostdepthsModItems.CONDENSED_MORFARITE.get())).displayItems((parameters, tabData) -> {
				tabData.accept(LostdepthsModItems.ENERGYZED_ALLOY.get());
				tabData.accept(LostdepthsModItems.RAW_MORFARITE.get());
				tabData.accept(LostdepthsModItems.RAW_FIRERITE.get());
				tabData.accept(LostdepthsModItems.RAW_MELWORITE.get());
				tabData.accept(LostdepthsModItems.RAW_CRYZULITE.get());
				tabData.accept(LostdepthsModItems.RAW_ZERITHIUM.get());
				tabData.accept(LostdepthsModItems.MORFARITE_INGOT.get());
				tabData.accept(LostdepthsModItems.FIRERITE_INGOT.get());
				tabData.accept(LostdepthsModItems.MELWORITE_INGOT.get());
				tabData.accept(LostdepthsModItems.CRYZULITE_INGOT.get());
				tabData.accept(LostdepthsModItems.ZERITHIUM_INGOT.get());
				tabData.accept(LostdepthsModItems.CONDENSED_MORFARITE.get());
				tabData.accept(LostdepthsModItems.CONDENSED_FIRERITE.get());
				tabData.accept(LostdepthsModItems.CONDENSED_MELWORITE.get());
				tabData.accept(LostdepthsModItems.CONDENSED_CRYZULITE.get());
				tabData.accept(LostdepthsModItems.CONDENSED_ZERITHIUM.get());
				tabData.accept(LostdepthsModItems.CONDENSED_COSMERITE.get());
				tabData.accept(LostdepthsModItems.TITANIUM_METALLOY_SCRAP.get());
				tabData.accept(LostdepthsModItems.TITANIUM_METALLOY.get());
				tabData.accept(LostdepthsModItems.SERPENTINE_CRYSTAL.get());
				tabData.accept(LostdepthsModItems.SERPENTINE_BAR.get());
				tabData.accept(LostdepthsModItems.MALICIUM_INGOT.get());
				tabData.accept(LostdepthsModItems.CONDENSED_MALICIUM.get());
				tabData.accept(LostdepthsModItems.IONITE_CRYSTAL.get());
				tabData.accept(LostdepthsModItems.IONITE_BAR.get());
				tabData.accept(LostdepthsModItems.NEOSTEEL_NUGGETS.get());
				tabData.accept(LostdepthsModItems.NEOSTEEL_INGOT.get());
				tabData.accept(LostdepthsModItems.ENRAGED_IRON.get());
				tabData.accept(LostdepthsModItems.SOLARIUM_SKY.get());
				tabData.accept(LostdepthsModItems.CELESTIAL_IRON.get());
				tabData.accept(LostdepthsModItems.CELESTIAL_DIAMOND.get());
				tabData.accept(LostdepthsModItems.CELESTIAL_REDSTONE.get());
				tabData.accept(LostdepthsModItems.ETHERCUT_METAL.get());
				tabData.accept(LostdepthsModItems.SPACECUT_METAL.get());
				tabData.accept(LostdepthsModItems.VOIDCUT_METAL.get());
				tabData.accept(LostdepthsModItems.TERRACUT_METAL.get());
				tabData.accept(LostdepthsModItems.LIGHTCUT_METAL.get());
				tabData.accept(LostdepthsModItems.STARCUT_METAL.get());
				tabData.accept(LostdepthsModItems.BIOCUT_METAL.get());
				tabData.accept(LostdepthsModItems.SYNTHESIZED_ASTROMETAL.get());
				tabData.accept(LostdepthsModItems.FROZEN_INGOT.get());
				tabData.accept(LostdepthsModItems.SPECTRAL_INGOT.get());
				tabData.accept(LostdepthsModItems.SOLARIUM_CORE.get());
				tabData.accept(LostdepthsModItems.SLIPPERY_INGOT.get());
				tabData.accept(LostdepthsModItems.CONDUCTIVE_BAR.get());
				tabData.accept(LostdepthsModItems.CONDENSED_ETHERIUM.get());
				tabData.accept(LostdepthsModItems.CONDENSED_KYVORIUM.get());
				tabData.accept(LostdepthsModItems.CONDENSED_POLARIUM.get());
				tabData.accept(LostdepthsModItems.ETHERIUM_INGOT.get());
				tabData.accept(LostdepthsModItems.KYVORIUM_INGOT.get());
				tabData.accept(LostdepthsModItems.POLARIUM_INGOT.get());
				tabData.accept(LostdepthsModItems.MYRITE_CRYSTAL.get());
				tabData.accept(LostdepthsModItems.MYRITE_BAR.get());
				tabData.accept(LostdepthsModItems.PURE_LUCIENT.get());
				tabData.accept(LostdepthsModItems.LUCIENT_BAR.get());
				tabData.accept(LostdepthsModItems.POLY_IONITE.get());
				tabData.accept(LostdepthsModItems.RAW_HYPERIUM.get());
				tabData.accept(LostdepthsModItems.RAW_PSYCHERIUM.get());
				tabData.accept(LostdepthsModItems.RAW_VARLLERIUM.get());
				tabData.accept(LostdepthsModItems.RAW_BIOLLITERITE.get());
				tabData.accept(LostdepthsModItems.RAW_NECROTONITE.get());
				tabData.accept(LostdepthsModItems.RAW_NOXHERTIUM.get());
				tabData.accept(LostdepthsModItems.RAW_COGNITIUM.get());
				tabData.accept(LostdepthsModItems.HYPERIUM_INGOT.get());
				tabData.accept(LostdepthsModItems.PSYCHERIUM_INGOT.get());
				tabData.accept(LostdepthsModItems.VARLLERIUM_INGOT.get());
				tabData.accept(LostdepthsModItems.BIOLLITERITE_INGOT.get());
				tabData.accept(LostdepthsModItems.NECROTONITE_INGOT.get());
				tabData.accept(LostdepthsModItems.NOXHERTIUM_INGOT.get());
				tabData.accept(LostdepthsModItems.COGNITIUM_INGOT.get());
				tabData.accept(LostdepthsModItems.CONDENSED_HYPERIUM.get());
				tabData.accept(LostdepthsModItems.CONDENSED_PSYCHERIUM.get());
				tabData.accept(LostdepthsModItems.CONDENSED_VARLLERIUM.get());
				tabData.accept(LostdepthsModItems.CONDENSED_BIOLLITERITE.get());
				tabData.accept(LostdepthsModItems.CONDENSED_NECROTONITE.get());
				tabData.accept(LostdepthsModItems.CONDENSED_NOXHERTIUM.get());
				tabData.accept(LostdepthsModItems.CONDENSED_COGNITIUM.get());
				tabData.accept(LostdepthsModItems.PHOTOTENZYTE_BAR.get());
			})

					.build());
	public static final RegistryObject<CreativeModeTab> LD_TOOLS = REGISTRY.register("ld_tools",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.lostdepths.ld_tools")).icon(() -> new ItemStack(LostdepthsModItems.FORGEFIRE_PICKAXE.get())).displayItems((parameters, tabData) -> {
				tabData.accept(LostdepthsModItems.CELESTIAL_ARMOR_CHESTPLATE.get());
				tabData.accept(LostdepthsModItems.CELESTIAL_ARMOR_LEGGINGS.get());
				tabData.accept(LostdepthsModItems.CELESTIAL_ARMOR_BOOTS.get());
				tabData.accept(LostdepthsModItems.THE_DESTROYER.get());
				tabData.accept(LostdepthsModItems.BLADE_OF_FORGOTTEN.get());
				tabData.accept(LostdepthsModItems.FORGEFIRE_PICKAXE.get());
				tabData.accept(LostdepthsModItems.FORGEFIRE_AXE.get());
				tabData.accept(LostdepthsModItems.ROD_OF_TRANSFORMATION.get());
				tabData.accept(LostdepthsModItems.VENOM_KNIFE.get());
				tabData.accept(LostdepthsModItems.CRYSTALIZED_PICKAXE.get());
				tabData.accept(LostdepthsModItems.SOUL_KEY.get());
				tabData.accept(LostdepthsModItems.GALAXY_DRAGON_STAFF.get());
				tabData.accept(LostdepthsModItems.CELESTIAL_PICKAXE.get());
				tabData.accept(LostdepthsModItems.OMNI_PICKAXE.get());
				tabData.accept(LostdepthsModItems.HEXBREAKER.get());
				tabData.accept(LostdepthsModItems.NIGHTMARE_PICKAXE.get());
				tabData.accept(LostdepthsModItems.PORTABLE_BEACON.get());
				tabData.accept(LostdepthsModItems.BANE_OF_VENOMS.get());
				tabData.accept(LostdepthsModItems.IRONHEART_ELIXIR.get());
				tabData.accept(LostdepthsModItems.THERMOSAW.get());
				tabData.accept(LostdepthsModItems.GUARDIAN_ELIXIR.get());
				tabData.accept(LostdepthsModItems.CANE_OF_VENOM.get());
				tabData.accept(LostdepthsModItems.FLUX_LANTERN.get());
				tabData.accept(LostdepthsModItems.STAR_ARMOR_CHESTPLATE.get());
				tabData.accept(LostdepthsModItems.STAR_ARMOR_LEGGINGS.get());
				tabData.accept(LostdepthsModItems.STAR_ARMOR_BOOTS.get());
				tabData.accept(LostdepthsModItems.STAR_KEY.get());
				tabData.accept(LostdepthsModItems.DRACONIC_KEY.get());
				tabData.accept(LostdepthsModItems.DRACONIC_ARMOR_CHESTPLATE.get());
				tabData.accept(LostdepthsModItems.DRACONIC_ARMOR_LEGGINGS.get());
				tabData.accept(LostdepthsModItems.DRACONIC_ARMOR_BOOTS.get());
				tabData.accept(LostdepthsModItems.PRIME_DRACONIC_ARMOR_CHESTPLATE.get());
				tabData.accept(LostdepthsModItems.PRIME_DRACONIC_ARMOR_LEGGINGS.get());
				tabData.accept(LostdepthsModItems.PRIME_DRACONIC_ARMOR_BOOTS.get());
				tabData.accept(LostdepthsModItems.DRACONIC_TRIDENT.get());
				tabData.accept(LostdepthsModItems.PRIME_DRACONIC_TRIDENT.get());
				tabData.accept(LostdepthsModItems.PRIME_DRACONIC_PICKAXE.get());
				tabData.accept(LostdepthsModItems.PRIME_DRACONIC_AXE.get());
				tabData.accept(LostdepthsModItems.PRIME_DRACONIC_SHOVEL.get());
				tabData.accept(LostdepthsModItems.PRIME_DRACONIC_SWORD.get());
				tabData.accept(LostdepthsModItems.PRIME_DRACONIC_HAMMER.get());
				tabData.accept(LostdepthsModItems.PRIME_DRACONIC_STAFF.get());
				tabData.accept(LostdepthsModItems.PRIME_DRACONIC_WINGS.get());
				tabData.accept(LostdepthsModItems.PHANTOM_BLADE.get());
			})

					.build());
	public static final RegistryObject<CreativeModeTab> DEV_TAB = REGISTRY.register("dev_tab",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.lostdepths.dev_tab")).icon(() -> new ItemStack(LostdepthsModItems.QUESTION_ICON.get())).displayItems((parameters, tabData) -> {
				tabData.accept(LostdepthsModBlocks.DEVENERGY.get().asItem());
				tabData.accept(LostdepthsModBlocks.TREASUREBRICKS.get().asItem());
				tabData.accept(LostdepthsModBlocks.TREASUREPILLAR.get().asItem());
				tabData.accept(LostdepthsModBlocks.TREASURESTAIRS.get().asItem());
				tabData.accept(LostdepthsModBlocks.TREASURESLABS.get().asItem());
				tabData.accept(LostdepthsModBlocks.TREASURECROSS.get().asItem());
				tabData.accept(LostdepthsModItems.DEVSTICK.get());
				tabData.accept(LostdepthsModBlocks.TREASURE_GLASS.get().asItem());
				tabData.accept(LostdepthsModBlocks.TREASURE_CROSS_GLOW.get().asItem());
				tabData.accept(LostdepthsModBlocks.TREASURE_POWER_BEAM.get().asItem());
				tabData.accept(LostdepthsModBlocks.TREASURE_NEO_SLAB.get().asItem());
				tabData.accept(LostdepthsModBlocks.TREASURE_FACTORY_PILLAR.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAIN_CROSS.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAIN_NORMAL.get().asItem());
				tabData.accept(LostdepthsModBlocks.FACTORY_FLOOR.get().asItem());
				tabData.accept(LostdepthsModBlocks.TREASURE_DARK_BRICKS.get().asItem());
				tabData.accept(LostdepthsModBlocks.TREASURE_DARK_TILES.get().asItem());
				tabData.accept(LostdepthsModBlocks.SECURITY_CLEARANCE_GATE_1.get().asItem());
				tabData.accept(LostdepthsModBlocks.SECURITYCLEARANCEGATE_2.get().asItem());
				tabData.accept(LostdepthsModBlocks.SECURITY_CLEARANCE_3.get().asItem());
				tabData.accept(LostdepthsModBlocks.TREASURE_GLOWSTEEL.get().asItem());
				tabData.accept(LostdepthsModBlocks.MODULE_CREATOR.get().asItem());
				tabData.accept(LostdepthsModBlocks.META_MATERIALIZER.get().asItem());
				tabData.accept(LostdepthsModBlocks.NEOSTEEL_LANTERN.get().asItem());
				tabData.accept(LostdepthsModBlocks.SECURITY_CLEARANCE_4.get().asItem());
				tabData.accept(LostdepthsModBlocks.META_COLLECTOR.get().asItem());
				tabData.accept(LostdepthsModBlocks.LIGHT_PUZZLE_A.get().asItem());
				tabData.accept(LostdepthsModBlocks.LIGHT_PUZZLE_CONTROLLER.get().asItem());
				tabData.accept(LostdepthsModBlocks.RUINED_ARCH.get().asItem());
				tabData.accept(LostdepthsModBlocks.POWER_TERMINAL.get().asItem());
				tabData.accept(LostdepthsModBlocks.JAMMER_ACCESS_MODULE.get().asItem());
				tabData.accept(LostdepthsModBlocks.JAMMER_GATE.get().asItem());
				tabData.accept(LostdepthsModBlocks.JAMMER_GATE_OFF.get().asItem());
				tabData.accept(LostdepthsModBlocks.QUANTUM_LAB_GATE.get().asItem());
				tabData.accept(LostdepthsModBlocks.QUANTUM_LAB_GATE_LASER.get().asItem());
				tabData.accept(LostdepthsModBlocks.QUANTUM_LAB_FLOOR.get().asItem());
				tabData.accept(LostdepthsModBlocks.DRILL_HEAD.get().asItem());
				tabData.accept(LostdepthsModBlocks.DRILL_SHAFT.get().asItem());
				tabData.accept(LostdepthsModBlocks.DRILL_CONSOLE.get().asItem());
				tabData.accept(LostdepthsModBlocks.TREASURE_NEO_SLAB_BLOCK.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHIP_FORMATTER_A.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHIP_FORMATTER_B.get().asItem());
				tabData.accept(LostdepthsModBlocks.VENT_BLCOK.get().asItem());
				tabData.accept(LostdepthsModBlocks.RESEARCH_GLASS.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_BARRIER_A.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_BARRIER_B.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_BARRIER_C.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_BARRIER_D.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_PILLAR_SIDE.get().asItem());
				tabData.accept(LostdepthsModBlocks.POWER_COLLIDER.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_PILLAR.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_PILLAR_B.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_LAMP.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_SLABS.get().asItem());
				tabData.accept(LostdepthsModBlocks.BLIGHT_IDOL.get().asItem());
				tabData.accept(LostdepthsModBlocks.LUMIO_IDOL.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_CONVERTER.get().asItem());
				tabData.accept(LostdepthsModBlocks.FIELD_CONTROLLER.get().asItem());
				tabData.accept(LostdepthsModBlocks.DARK_STEEL_GLOW_LIGHT.get().asItem());
				tabData.accept(LostdepthsModBlocks.DARK_STEEL_GLOW_LIGHT_A.get().asItem());
				tabData.accept(LostdepthsModBlocks.DARK_STEEL_GLOW_LIGHT_B.get().asItem());
				tabData.accept(LostdepthsModBlocks.DARK_STEEL_GLOW_LIGHT_C.get().asItem());
				tabData.accept(LostdepthsModBlocks.DARK_STEEL_GLOW_LIGHT_D.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPESPLUS.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_PLUS_A.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_T.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_TA.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_I.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_IA.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_WEST.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_WEST_A.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_SOUTH.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_SOUTH_A.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_NORTH.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_NORTH_A.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_EAST.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_EAST_A.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_T_WEST.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_T_WEST_A.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_T_SOUTH.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_T_SOUTH_A.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_T_NORTH.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_T_NORTH_A.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_T_EAST.get().asItem());
				tabData.accept(LostdepthsModBlocks.PIPES_T_EAST_A.get().asItem());
				tabData.accept(LostdepthsModBlocks.POWER_BLOCK_BLUE.get().asItem());
				tabData.accept(LostdepthsModBlocks.POWER_BLOCK_YELLOW.get().asItem());
				tabData.accept(LostdepthsModBlocks.POWER_BLOCK_PURPLE.get().asItem());
				tabData.accept(LostdepthsModBlocks.POWER_BLOCK_GREEN.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_STEEL_BLACK.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_LAMP_RAZOR_A.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_LAMP_RAZOR_B.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_LAMP_RAZOR_C.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_LAMP_DARK.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_GLASS_BLUE.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_SLABS_SEA.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_PILLAR_SEA.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_ANI_SLABS_SEA.get().asItem());
				tabData.accept(LostdepthsModBlocks.ARENA_BARRIER.get().asItem());
				tabData.accept(LostdepthsModBlocks.CELESTIAL_BARRIER.get().asItem());
				tabData.accept(LostdepthsModBlocks.POWER_CONDUIT.get().asItem());
				tabData.accept(LostdepthsModBlocks.LABYRINTH_METAL_BLUE.get().asItem());
				tabData.accept(LostdepthsModBlocks.LABYRINTH_LAMP_A.get().asItem());
				tabData.accept(LostdepthsModBlocks.LABYRINTH_LAMP_B.get().asItem());
				tabData.accept(LostdepthsModBlocks.LABYRINTH_LAMP_C.get().asItem());
				tabData.accept(LostdepthsModBlocks.ASTROSTEEL.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL_FLOOR.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL_BEAM.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL_LIGHT.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL_WALL.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL_GLASS.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL_GLASS_DARK.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL_HEAT_OFF.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL_HEAT_ON.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL_WIRING_A.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL_WIRING_B.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL_WIRING_C.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL_WIRING_D.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL_WIRING_E.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL_WIRING_F.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL_WIRING_G.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL_WIRING_H.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL_WIRING_I.get().asItem());
				tabData.accept(LostdepthsModBlocks.CHAMPION_STEEL_WIRING_J.get().asItem());
				tabData.accept(LostdepthsModBlocks.ITEM_CHALLENGE_COLLECTOR.get().asItem());
				tabData.accept(LostdepthsModBlocks.ITEM_CHALLENGE_BLANK.get().asItem());
				tabData.accept(LostdepthsModBlocks.NEON_BRICKS.get().asItem());
				tabData.accept(LostdepthsModBlocks.NEON_BRICKS_A.get().asItem());
				tabData.accept(LostdepthsModBlocks.NEON_BRICKS_B.get().asItem());
				tabData.accept(LostdepthsModBlocks.NEON_BRICKS_C.get().asItem());
				tabData.accept(LostdepthsModBlocks.NEON_BRICKS_D.get().asItem());
				tabData.accept(LostdepthsModBlocks.NEON_BRICKS_E.get().asItem());
				tabData.accept(LostdepthsModBlocks.NEON_BRICKS_F.get().asItem());
				tabData.accept(LostdepthsModBlocks.NEON_BRICKS_G.get().asItem());
				tabData.accept(LostdepthsModBlocks.RING_SLIDE_BUTTON.get().asItem());
				tabData.accept(LostdepthsModBlocks.LIGHT_RECEIVER.get().asItem());
				tabData.accept(LostdepthsModBlocks.TIMELINE_STABILIZER.get().asItem());
				tabData.accept(LostdepthsModBlocks.MELODIC_SEQUENCER.get().asItem());
				tabData.accept(LostdepthsModBlocks.ALIGNMENT_TILE.get().asItem());
				tabData.accept(LostdepthsModBlocks.ALIGNMENT_TILE_ON.get().asItem());
				tabData.accept(LostdepthsModBlocks.ALIGNMENT_DIAL.get().asItem());
				tabData.accept(LostdepthsModBlocks.CONNECT_GAME.get().asItem());
				tabData.accept(LostdepthsModBlocks.CONTROL_SCREEN.get().asItem());
				tabData.accept(LostdepthsModBlocks.CONTROL_SCREEN_KEYBOARD.get().asItem());
				tabData.accept(LostdepthsModBlocks.CONTROL_KEYBOARD.get().asItem());
				tabData.accept(LostdepthsModBlocks.KEYBOARD_SCREEN.get().asItem());
				tabData.accept(LostdepthsModBlocks.SECURITY_CLEARANCE_5.get().asItem());
				tabData.accept(LostdepthsModBlocks.SECURITY_CLEARANCE_6.get().asItem());
				tabData.accept(LostdepthsModBlocks.TERMINAL_MONITOR.get().asItem());
				tabData.accept(LostdepthsModBlocks.TERMINAL_CONSOLE_1.get().asItem());
				tabData.accept(LostdepthsModBlocks.TERMINAL_CONSOLE_2.get().asItem());
				tabData.accept(LostdepthsModBlocks.TERMINAL_CONSOLE_3.get().asItem());
				tabData.accept(LostdepthsModBlocks.MYRITE_ORE.get().asItem());
			})

					.build());
	public static final RegistryObject<CreativeModeTab> LD_MODULES = REGISTRY.register("ld_modules",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.lostdepths.ld_modules")).icon(() -> new ItemStack(LostdepthsModItems.EMPTY_MODULE_CONTAINER.get())).displayItems((parameters, tabData) -> {
				tabData.accept(LostdepthsModItems.EMPTY_MODULE_CONTAINER.get());
				tabData.accept(LostdepthsModItems.MODULE_ACCELERATION.get());
				tabData.accept(LostdepthsModItems.MODULE_HEXING.get());
				tabData.accept(LostdepthsModItems.MODULE_POWER.get());
				tabData.accept(LostdepthsModItems.MODULE_DURABILITY.get());
				tabData.accept(LostdepthsModItems.MODULE_RESTORATION.get());
				tabData.accept(LostdepthsModItems.MODULE_TRANSMITTING.get());
				tabData.accept(LostdepthsModItems.MODULE_RANGE.get());
				tabData.accept(LostdepthsModItems.MODULE_CONVERSION.get());
				tabData.accept(LostdepthsModItems.MODULE_WEIGHT.get());
			})

					.build());
	public static final RegistryObject<CreativeModeTab> LD_ITEMS = REGISTRY.register("ld_items",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.lostdepths.ld_items")).icon(() -> new ItemStack(LostdepthsModItems.INFUSED_IRON.get())).displayItems((parameters, tabData) -> {
				tabData.accept(LostdepthsModItems.ADVANCED_MECHANICAL_PARTS.get());
				tabData.accept(LostdepthsModItems.ADVANCED_CONSTRUCTION_PARTS.get());
				tabData.accept(LostdepthsModItems.ADVANCED_ELECTRONIC_PARTS.get());
				tabData.accept(LostdepthsModItems.GEO_MAGNETIC_GLUE.get());
				tabData.accept(LostdepthsModItems.KEY_FRAGMENT_1.get());
				tabData.accept(LostdepthsModItems.KEY_FRAGMENT_2.get());
				tabData.accept(LostdepthsModItems.KEY_FRAGMENT_3.get());
				tabData.accept(LostdepthsModItems.MAGMA_SOLUTION.get());
				tabData.accept(LostdepthsModItems.MADRIGEN_SOLUTION.get());
				tabData.accept(LostdepthsModItems.ENDER_VOLTAIC_SOLUTION.get());
				tabData.accept(LostdepthsModItems.MAGMA_COMPOUND.get());
				tabData.accept(LostdepthsModItems.CORRUPTED_GEMSTONE.get());
				tabData.accept(LostdepthsModItems.POWER_CHARGE.get());
				tabData.accept(LostdepthsModItems.LAVA_STRING.get());
				tabData.accept(LostdepthsModItems.IRON_GOLEM_ESSENCE.get());
				tabData.accept(LostdepthsModItems.ION_CRYSTAL.get());
				tabData.accept(LostdepthsModItems.POWER_CORE.get());
				tabData.accept(LostdepthsModItems.UNBREAKABLE_CHAIN.get());
				tabData.accept(LostdepthsModItems.FERRO_ROCK.get());
				tabData.accept(LostdepthsModItems.METAL_BRANCH.get());
				tabData.accept(LostdepthsModItems.DARK_FUSE_DIAMOND.get());
				tabData.accept(LostdepthsModItems.SUPER_CHARGED_COIL.get());
				tabData.accept(LostdepthsModItems.DUAL_POWER_ASSEMBLY.get());
				tabData.accept(LostdepthsModItems.POWER_CHARGE_SOLUTIONS.get());
				tabData.accept(LostdepthsModItems.POWER_CONNECTOR_ASSEMBLY.get());
				tabData.accept(LostdepthsModItems.ADVANCED_POWER_CORE.get());
				tabData.accept(LostdepthsModItems.WORKBENCH_GRID.get());
				tabData.accept(LostdepthsModItems.OVER_CHARGE_COIL.get());
				tabData.accept(LostdepthsModItems.GIGA_CHARGE_COIL.get());
				tabData.accept(LostdepthsModItems.POWER_COIL.get());
				tabData.accept(LostdepthsModItems.POWER_CLAMP.get());
				tabData.accept(LostdepthsModItems.CRYSTALIZED_ALLOY.get());
				tabData.accept(LostdepthsModItems.ENRICHED_PRISMARINE.get());
				tabData.accept(LostdepthsModItems.POLISHING_SOLUTION.get());
				tabData.accept(LostdepthsModItems.METAL_BONE.get());
				tabData.accept(LostdepthsModItems.HARDENED_CALCIUM.get());
				tabData.accept(LostdepthsModItems.CRYSTALIZED_SNOWBALL.get());
				tabData.accept(LostdepthsModItems.HARDENED_BONE_BLOCKS.get());
				tabData.accept(LostdepthsModItems.CRYSTALIZED_BONE_BLOCKS.get());
				tabData.accept(LostdepthsModItems.POWER_SUPPLY_MODULE.get());
				tabData.accept(LostdepthsModItems.VIBRANT_CAPACITOR.get());
				tabData.accept(LostdepthsModItems.REINFORCED_PLATE.get());
				tabData.accept(LostdepthsModItems.HARDENED_GLASS_PANEL.get());
				tabData.accept(LostdepthsModItems.HEAT_RESISTANCE_GLASS.get());
				tabData.accept(LostdepthsModItems.PERFECT_PEARL.get());
				tabData.accept(LostdepthsModItems.ULTRALIGHT_DUST.get());
				tabData.accept(LostdepthsModItems.GRAVITY_DUST.get());
				tabData.accept(LostdepthsModItems.CYCLONE_DUST.get());
				tabData.accept(LostdepthsModItems.ACIDIC_OOZE.get());
				tabData.accept(LostdepthsModItems.ACIDBLOOD_SOLUTION.get());
				tabData.accept(LostdepthsModItems.SLIMED_BEDROCK.get());
				tabData.accept(LostdepthsModItems.ASTROMETAL_AMALGAMATION_GLUE.get());
				tabData.accept(LostdepthsModItems.SUNDER_LOG.get());
				tabData.accept(LostdepthsModItems.CLOVINITE.get());
				tabData.accept(LostdepthsModItems.BURNING_SAP.get());
				tabData.accept(LostdepthsModItems.JAR_OF_SAP.get());
				tabData.accept(LostdepthsModItems.JAR_OF_SYRUP.get());
				tabData.accept(LostdepthsModItems.CLOVINITE_POWER_BANK.get());
				tabData.accept(LostdepthsModItems.EXTERNAL_CLOVINITE_BATTERY.get());
				tabData.accept(LostdepthsModItems.CLOVINITE_POWER_HUB.get());
				tabData.accept(LostdepthsModItems.VOLATILE_BLOOD.get());
				tabData.accept(LostdepthsModItems.VOLATILITY_SOLUTION.get());
				tabData.accept(LostdepthsModItems.GLISTENING_CRYSTALS.get());
				tabData.accept(LostdepthsModItems.POLYCHARGE_SOLUTION.get());
				tabData.accept(LostdepthsModItems.DIGITAL_PANEL.get());
				tabData.accept(LostdepthsModItems.FRESH_DATA_CHIP.get());
				tabData.accept(LostdepthsModItems.BASIC_STORAGE_CHIP.get());
				tabData.accept(LostdepthsModItems.QUICKFLAME_SOLUTION.get());
				tabData.accept(LostdepthsModItems.POLY_AMPLIFICATION_SOLUTION.get());
				tabData.accept(LostdepthsModItems.HOLOSCREEN.get());
				tabData.accept(LostdepthsModItems.HOLOTRON_DISC.get());
				tabData.accept(LostdepthsModItems.BLANK_AUGMENT_SLIDE.get());
				tabData.accept(LostdepthsModItems.LUMINESCENT_CUBES.get());
				tabData.accept(LostdepthsModItems.HEAVY_CRYSTAL.get());
				tabData.accept(LostdepthsModItems.CRYSTAL_DIODE.get());
				tabData.accept(LostdepthsModItems.NIGHTMARE_SOLUTION.get());
				tabData.accept(LostdepthsModItems.ELECTRON_RECYCLER.get());
				tabData.accept(LostdepthsModItems.GLOWBLOOD_SOLUTION.get());
				tabData.accept(LostdepthsModItems.SCORCHED_PIPE.get());
				tabData.accept(LostdepthsModItems.PYRE_LOG.get());
				tabData.accept(LostdepthsModItems.HOLLOW_VOIDSTONE.get());
				tabData.accept(LostdepthsModItems.POWER_FUNNEL.get());
				tabData.accept(LostdepthsModItems.LIFESTRAIN_ENIGMA.get());
				tabData.accept(LostdepthsModItems.ATLAS_BEACON.get());
				tabData.accept(LostdepthsModItems.STONE_OF_REBIRTH.get());
				tabData.accept(LostdepthsModItems.CONCENTRATED_VENOM.get());
				tabData.accept(LostdepthsModItems.FIREBLOOD_SOLUTION.get());
				tabData.accept(LostdepthsModItems.BIOCORRUPTION_POWDER.get());
				tabData.accept(LostdepthsModItems.CARBONIC_ACID.get());
				tabData.accept(LostdepthsModItems.CORRUPTED_SOLUTION.get());
				tabData.accept(LostdepthsModItems.CORROSIVE_MIX.get());
				tabData.accept(LostdepthsModItems.GAZE_OF_NIGHTMARES.get());
				tabData.accept(LostdepthsModItems.CRYSTAL_OF_CURSES.get());
				tabData.accept(LostdepthsModItems.HAUNTED_INGOT.get());
				tabData.accept(LostdepthsModItems.SOLARIUM_HUNTER.get());
				tabData.accept(LostdepthsModItems.REINFORCED_BOTTLE.get());
				tabData.accept(LostdepthsModItems.COOKED_EYE.get());
				tabData.accept(LostdepthsModItems.SOLARIUM_TOXIC.get());
				tabData.accept(LostdepthsModItems.UNSTABLE_INGOT.get());
				tabData.accept(LostdepthsModItems.RADIOACTIVE_ISOTOPES.get());
				tabData.accept(LostdepthsModItems.ANCIENT_TEMPLATE.get());
				tabData.accept(LostdepthsModItems.BLAZING_STAR.get());
				tabData.accept(LostdepthsModItems.RADIOCHRONIC_ISOTOPE.get());
				tabData.accept(LostdepthsModItems.NEGAMETRIC_ISOTOPE.get());
				tabData.accept(LostdepthsModItems.VIAL_OF_SOLARUM.get());
				tabData.accept(LostdepthsModItems.REFLECTIVE_SHARD.get());
				tabData.accept(LostdepthsModItems.ASTRAL_GENERATOR.get());
				tabData.accept(LostdepthsModItems.INTERSPACE_TRANSMITTER.get());
				tabData.accept(LostdepthsModItems.ULTRAVIOLET_SOLUTION.get());
				tabData.accept(LostdepthsModItems.AUGMENTICON_BOX.get());
				tabData.accept(LostdepthsModItems.UPGRADE_KIT_II.get());
				tabData.accept(LostdepthsModItems.UPGRADE_KIT_III.get());
				tabData.accept(LostdepthsModItems.AUGMENTICON_BOX_CHARGED.get());
				tabData.accept(LostdepthsModItems.FRACTURED_MULTIVERSITE.get());
				tabData.accept(LostdepthsModItems.PHOTOTENZYTE.get());
				tabData.accept(LostdepthsModItems.NEGATIVE_MAGNECRONITE.get());
				tabData.accept(LostdepthsModItems.POSITIVE_MAGNECRONITE.get());
				tabData.accept(LostdepthsModItems.WEAK_POLARCRONITE.get());
				tabData.accept(LostdepthsModItems.POTENT_POLARCRONITE.get());
				tabData.accept(LostdepthsModItems.POWERFUL_POLARCRONITE.get());
				tabData.accept(LostdepthsModItems.EMPTY_DATA_DRIVE.get());
			})

					.build());
	public static final RegistryObject<CreativeModeTab> LD_DROPS = REGISTRY.register("ld_drops",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.lostdepths.ld_drops")).icon(() -> new ItemStack(LostdepthsModItems.INFUSED_REDSTONE.get())).displayItems((parameters, tabData) -> {
				tabData.accept(LostdepthsModItems.INFUSED_IRON.get());
				tabData.accept(LostdepthsModItems.INFUSED_CRYSTAL.get());
				tabData.accept(LostdepthsModItems.INFUSED_REDSTONE.get());
				tabData.accept(LostdepthsModItems.CORROSIVE_TEAR.get());
				tabData.accept(LostdepthsModItems.ACIDIC_SHELL.get());
				tabData.accept(LostdepthsModItems.MYSTERIOS_GLOOP.get());
				tabData.accept(LostdepthsModItems.DUSKER_EGGS.get());
				tabData.accept(LostdepthsModItems.GIANT_DUSKER_EGGS.get());
				tabData.accept(LostdepthsModItems.NIGHTMARE_POWDER.get());
				tabData.accept(LostdepthsModItems.INFUSED_GOLD.get());
				tabData.accept(LostdepthsModItems.PLUCKED_EYE.get());
				tabData.accept(LostdepthsModItems.MAGIC_STONE.get());
				tabData.accept(LostdepthsModItems.HARD_CRYSTALS.get());
				tabData.accept(LostdepthsModItems.HYPNOTIC_EYE.get());
				tabData.accept(LostdepthsModItems.EXPLOSIVE_SAC.get());
				tabData.accept(LostdepthsModItems.GLOWING_SAC.get());
				tabData.accept(LostdepthsModItems.EMBER_SAC.get());
				tabData.accept(LostdepthsModItems.ACID_TONGUE.get());
				tabData.accept(LostdepthsModItems.RAZOR_FANG.get());
				tabData.accept(LostdepthsModItems.ION_BLAZE_ROD.get());
				tabData.accept(LostdepthsModItems.EVERWATCHING_EYE.get());
			})

					.build());
	public static final RegistryObject<CreativeModeTab> CONTAINERS = REGISTRY.register("containers",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.lostdepths.containers")).icon(() -> new ItemStack(LostdepthsModItems.GEM_IMPOSITION.get())).displayItems((parameters, tabData) -> {
				tabData.accept(LostdepthsModItems.GEM_IMPOSITION.get());
				tabData.accept(LostdepthsModItems.GEM_RESOLVE.get());
				tabData.accept(LostdepthsModItems.GEM_INGENUITY.get());
			})

					.build());
	public static final RegistryObject<CreativeModeTab> LD_EGGS = REGISTRY.register("ld_eggs",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.lostdepths.ld_eggs")).icon(() -> new ItemStack(Items.WARDEN_SPAWN_EGG)).displayItems((parameters, tabData) -> {
				tabData.accept(LostdepthsModItems.LOST_DARK_SPAWN_EGG.get());
				tabData.accept(LostdepthsModItems.MAELSTROM_SPAWN_EGG.get());
				tabData.accept(LostdepthsModItems.GALAXY_DRAGON_SPAWN_EGG.get());
				tabData.accept(LostdepthsModItems.GUOON_SPAWN_EGG.get());
				tabData.accept(LostdepthsModItems.FLAPPER_SPAWN_EGG.get());
				tabData.accept(LostdepthsModItems.THE_PROTECTOR_SPAWN_EGG.get());
				tabData.accept(LostdepthsModItems.NEUROBLAZE_SPAWN_EGG.get());
				tabData.accept(LostdepthsModItems.ASTRALCLAW_SPAWN_EGG.get());
				tabData.accept(LostdepthsModItems.DM_12_SPAWN_EGG.get());
				tabData.accept(LostdepthsModItems.ARACHNOTA_SPAWN_EGG.get());
			}).withSearchBar().build());
}
