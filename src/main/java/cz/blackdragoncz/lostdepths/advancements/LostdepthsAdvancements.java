package cz.blackdragoncz.lostdepths.advancements;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static net.minecraft.advancements.FrameType.CHALLENGE;
import static net.minecraft.advancements.FrameType.GOAL;
import static net.minecraft.advancements.FrameType.TASK;

// Source of truth for every Lost Depths advancement. The JSON under data/lostdepths/advancements is generated from this by LostdepthsAdvancementProvider.
public final class LostdepthsAdvancements {

	private static final List<LostdepthsAdvancementBuilder> ALL = new ArrayList<>();

	private LostdepthsAdvancements() {
	}

	public static final LostdepthsAdvancementBuilder DATABASEADV = manual(null, "databaseadv", LostdepthsModItems.LOREBOOKICON, TASK).background("textures/screens/voidcutbrick.png").hidden();
	public static final LostdepthsAdvancementBuilder DATABASELOG_1 = manual(DATABASEADV, "databaselog_1", LostdepthsModItems.LOREBOOKICON, TASK).noAnnounce();

	public static final LostdepthsAdvancementBuilder INSTALL_ADV = manual(null, "install_adv", LostdepthsModItems.ADVICON, TASK).background("textures/screens/voidcutbrick.png").hidden().noAnnounce();
	public static final LostdepthsAdvancementBuilder CRYSTALIZED_NIGHTMARE = obtain(INSTALL_ADV, "crystalized_nightmare", LostdepthsModItems.INFUSED_CRYSTAL, CHALLENGE).hidden().noToast();
	public static final LostdepthsAdvancementBuilder ONMYWAY = obtain(CRYSTALIZED_NIGHTMARE, "onmyway", LostdepthsModItems.ENERGYZED_ALLOY, GOAL);
	public static final LostdepthsAdvancementBuilder CHEATER = manual(ONMYWAY, "cheater", () -> Items.ENCHANTED_BOOK, CHALLENGE).hidden();
	public static final LostdepthsAdvancementBuilder FRAGILE_ADV = obtain(ONMYWAY, "fragile_adv", LostdepthsModItems.CRYSTALIZED_ALLOY, TASK);
	public static final LostdepthsAdvancementBuilder FROM_BELOW = travel(ONMYWAY, "from_below", LostdepthsModItems.SPACE_ROCK, "below_bedrock", TASK);
	public static final LostdepthsAdvancementBuilder ANOTHER_SURFACE = travel(FROM_BELOW, "another_surface", LostdepthsModItems.NEOSTEEL_LANTERN, "lost_dungeons", TASK);
	public static final LostdepthsAdvancementBuilder FROM_ABOVE = travel(FROM_BELOW, "from_above", LostdepthsModItems.FERRO_LOG, "between_bedrock_and_overworld", TASK).hidden();
	public static final LostdepthsAdvancementBuilder CLOVINITE_ADV = obtain(FROM_ABOVE, "clovinite_adv", LostdepthsModItems.CLOVINITE, GOAL);
	public static final LostdepthsAdvancementBuilder SUNDER_LOG_ADV = obtain(FROM_ABOVE, "sunder_log_adv", LostdepthsModItems.SUNDER_LOG, GOAL);
	public static final LostdepthsAdvancementBuilder RUINS_ADV = manual(FROM_BELOW, "ruins_adv", LostdepthsModItems.TREASUREBRICKS, GOAL).hidden();
	public static final LostdepthsAdvancementBuilder RUINS_2_ADV = travel(RUINS_ADV, "ruins_2_adv", LostdepthsModItems.TREASURE_DARK_BRICKS, "below_bedrock", GOAL);
	public static final LostdepthsAdvancementBuilder META_CONSTRUCTOR_ADV = obtain(RUINS_2_ADV, "meta_constructor_adv", LostdepthsModItems.SECURITY_PASS_3, GOAL);
	public static final LostdepthsAdvancementBuilder GETTING_STARTED = obtain(INSTALL_ADV, "getting_started", LostdepthsModItems.INFUSED_IRON, TASK).hidden().noToast();
	public static final LostdepthsAdvancementBuilder ACIDIC_OOZE_ADV = obtain(GETTING_STARTED, "acidic_ooze_adv", LostdepthsModItems.ACIDIC_OOZE, TASK);
	public static final LostdepthsAdvancementBuilder ANGRY_ADV = obtain(GETTING_STARTED, "angry_adv", LostdepthsModItems.ENRAGED_IRON, TASK);
	public static final LostdepthsAdvancementBuilder POWER_CAPACITOR_ADV = obtain(ANGRY_ADV, "power_capacitor_adv", LostdepthsModItems.VIBRANT_CAPACITOR, TASK);
	public static final LostdepthsAdvancementBuilder COSMIC_BATTERY_ADV = obtain(POWER_CAPACITOR_ADV, "cosmic_battery_adv", LostdepthsModItems.POWER_SUPPLY_MODULE, TASK);
	public static final LostdepthsAdvancementBuilder TECH_ADV = place(ANGRY_ADV, "tech_adv", LostdepthsModBlocks.PRINT_TECH, TASK);
	public static final LostdepthsAdvancementBuilder ATMOS_TECH_ADV = obtain(TECH_ADV, "atmos_tech_adv", LostdepthsModItems.ATMOS_TECH, GOAL);
	public static final LostdepthsAdvancementBuilder ACIDBLOODSOLUTION_ADV = obtain(ATMOS_TECH_ADV, "acidbloodsolution_adv", LostdepthsModItems.ACIDBLOOD_SOLUTION, TASK);
	public static final LostdepthsAdvancementBuilder CONTROLLED_GRAVITY_ADV = obtain(ATMOS_TECH_ADV, "controlled_gravity_adv", LostdepthsModItems.ULTRALIGHT_DUST, TASK);
	public static final LostdepthsAdvancementBuilder VOLATILITY_SOLUTION_ADV = obtain(ATMOS_TECH_ADV, "volatility_solution_adv", LostdepthsModItems.VOLATILITY_SOLUTION, TASK);
	public static final LostdepthsAdvancementBuilder HARDENED_GLASS_ADV = obtain(TECH_ADV, "hardened_glass_adv", LostdepthsModItems.HARDENED_GLASS_PANEL, TASK);
	public static final LostdepthsAdvancementBuilder HEAT_GLASS_ADV = obtain(HARDENED_GLASS_ADV, "heat_glass_adv", LostdepthsModItems.HEAT_RESISTANCE_GLASS, TASK);
	public static final LostdepthsAdvancementBuilder DRUIDS_ADV = obtain(GETTING_STARTED, "druids_adv", LostdepthsModItems.DRUIDS_FLOWER, GOAL).hidden();
	public static final LostdepthsAdvancementBuilder INFUSED_REDSTONE_ADV = obtain(GETTING_STARTED, "infused_redstone_adv", LostdepthsModItems.INFUSED_REDSTONE, TASK);
	public static final LostdepthsAdvancementBuilder CRYING_GHAST_ADV = obtain(INFUSED_REDSTONE_ADV, "crying_ghast_adv", LostdepthsModItems.CORROSIVE_TEAR, GOAL);
	public static final LostdepthsAdvancementBuilder KEYMASTER_ADV = obtain(GETTING_STARTED, "keymaster_adv", LostdepthsModItems.CELESTIAL_KEY, TASK);
	public static final LostdepthsAdvancementBuilder INFUSEDMETAL_ADV = obtain(KEYMASTER_ADV, "infusedmetal_adv", LostdepthsModItems.MALICIUM_INGOT, GOAL);
	public static final LostdepthsAdvancementBuilder BLADEOF_FORGOTTEN_ADV = obtainOne(INFUSEDMETAL_ADV, "bladeof_forgotten_adv", LostdepthsModItems.BLADE_OF_FORGOTTEN, GOAL);
	public static final LostdepthsAdvancementBuilder SLIME_ON_BEDROCK = obtain(GETTING_STARTED, "slime_on_bedrock", LostdepthsModItems.SLIMED_BEDROCK, TASK);
	public static final LostdepthsAdvancementBuilder W_1_ADV = place(GETTING_STARTED, "w_1_adv", LostdepthsModBlocks.GALACTIC_WORKSTATION, TASK).experience(5);
	public static final LostdepthsAdvancementBuilder GALACTIC_COMPRESSOR_ADV = place(W_1_ADV, "galactic_compressor_adv", LostdepthsModBlocks.GALACTIC_COMPRESSOR, GOAL).hidden().experience(15);
	public static final LostdepthsAdvancementBuilder CRYZULITE_ADV = obtain(GALACTIC_COMPRESSOR_ADV, "cryzulite_adv", LostdepthsModItems.CONDENSED_CRYZULITE, GOAL);
	public static final LostdepthsAdvancementBuilder FIRERITE_ADV = obtain(GALACTIC_COMPRESSOR_ADV, "firerite_adv", LostdepthsModItems.CONDENSED_FIRERITE, TASK);
	public static final LostdepthsAdvancementBuilder MELWORITE_ADV = obtain(GALACTIC_COMPRESSOR_ADV, "melworite_adv", LostdepthsModItems.CONDENSED_MELWORITE, TASK);
	public static final LostdepthsAdvancementBuilder MORFARITE_ADV = obtain(GALACTIC_COMPRESSOR_ADV, "morfarite_adv", LostdepthsModItems.CONDENSED_MORFARITE, TASK);
	public static final LostdepthsAdvancementBuilder COSMERITE_ADV = obtain(MORFARITE_ADV, "cosmerite_adv", LostdepthsModItems.CONDENSED_COSMERITE, GOAL).experience(20);
	public static final LostdepthsAdvancementBuilder TERRESTRIAL = obtain(GALACTIC_COMPRESSOR_ADV, "terrestrial", LostdepthsModItems.EXTRA_TERESTRIAL_COMPRESSOR, TASK);
	public static final LostdepthsAdvancementBuilder BIOLLITERITE = obtain(TERRESTRIAL, "biolliterite", LostdepthsModItems.CONDENSED_BIOLLITERITE, TASK);
	public static final LostdepthsAdvancementBuilder BLACKHOLE = obtain(TERRESTRIAL, "blackhole", LostdepthsModItems.BLACK_HOLE_COMPRESSOR, TASK);
	public static final LostdepthsAdvancementBuilder NECROTONITE = obtain(BLACKHOLE, "necrotonite", LostdepthsModItems.CONDENSED_NECROTONITE, TASK);
	public static final LostdepthsAdvancementBuilder COGNITIUM = obtain(TERRESTRIAL, "cognitium", LostdepthsModItems.CONDENSED_COGNITIUM, TASK);
	public static final LostdepthsAdvancementBuilder HYPERIUM = obtain(TERRESTRIAL, "hyperium", LostdepthsModItems.CONDENSED_HYPERIUM, TASK);
	public static final LostdepthsAdvancementBuilder NOXHERTIUM = obtain(TERRESTRIAL, "noxhertium", LostdepthsModItems.CONDENSED_NOXHERTIUM, TASK);
	public static final LostdepthsAdvancementBuilder PSYCHERIUM = obtain(TERRESTRIAL, "psycherium", LostdepthsModItems.CONDENSED_PSYCHERIUM, GOAL);
	public static final LostdepthsAdvancementBuilder VARLLERIUM = obtain(TERRESTRIAL, "varllerium", LostdepthsModItems.CONDENSED_VARLLERIUM, TASK);
	public static final LostdepthsAdvancementBuilder ZERITHIUM_ADV = obtain(GALACTIC_COMPRESSOR_ADV, "zerithium_adv", LostdepthsModItems.CONDENSED_ZERITHIUM, GOAL);
	public static final LostdepthsAdvancementBuilder W_2_ADV = place(W_1_ADV, "w_2_adv", LostdepthsModBlocks.ALLOY_WORKSTATION, GOAL);
	public static final LostdepthsAdvancementBuilder CRYSTALIZED_PICKAXE_ADV = obtainOne(W_2_ADV, "crystalized_pickaxe_adv", LostdepthsModItems.CRYSTALIZED_PICKAXE, GOAL);

	public static List<LostdepthsAdvancementBuilder> all() {
		return List.copyOf(ALL);
	}

	private static LostdepthsAdvancementBuilder register(@Nullable LostdepthsAdvancementBuilder parent, String name, Supplier<? extends ItemLike> icon, FrameType frame) {
		LostdepthsAdvancementBuilder builder = new LostdepthsAdvancementBuilder(parent, LostdepthsMod.rl(name), icon, frame);
		ALL.add(builder);
		return builder;
	}

	// Held in the inventory at all, any stack size.
	private static LostdepthsAdvancementBuilder obtain(@Nullable LostdepthsAdvancementBuilder parent, String name, Supplier<? extends ItemLike> item, FrameType frame) {
		return register(parent, name, item, frame).criterion(name, () -> hasItem(item, MinMaxBounds.Ints.between(1, 64)));
	}

	// Held in the inventory as a single item, for the unstackable tools.
	private static LostdepthsAdvancementBuilder obtainOne(@Nullable LostdepthsAdvancementBuilder parent, String name, Supplier<? extends ItemLike> item, FrameType frame) {
		return register(parent, name, item, frame).criterion(name, () -> hasItem(item, MinMaxBounds.Ints.exactly(1)));
	}

	private static LostdepthsAdvancementBuilder place(@Nullable LostdepthsAdvancementBuilder parent, String name, RegistryObject<Block> block, FrameType frame) {
		return register(parent, name, block, frame).criterion(name, () -> ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(block.get()));
	}

	private static LostdepthsAdvancementBuilder travel(@Nullable LostdepthsAdvancementBuilder parent, String name, Supplier<? extends ItemLike> icon, String dimension, FrameType frame) {
		return register(parent, name, icon, frame).criterion(name, () -> ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(ResourceKey.create(Registries.DIMENSION, LostdepthsMod.rl(dimension))));
	}

	// Granted from code via LostdepthsAdvancementTriggers, so the criterion itself can never fire on its own.
	private static LostdepthsAdvancementBuilder manual(@Nullable LostdepthsAdvancementBuilder parent, String name, Supplier<? extends ItemLike> icon, FrameType frame) {
		return register(parent, name, icon, frame).criterion(name, ImpossibleTrigger.TriggerInstance::new);
	}

	private static CriterionTriggerInstance hasItem(Supplier<? extends ItemLike> item, MinMaxBounds.Ints count) {
		return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(item.get()).withCount(count).build());
	}
}
