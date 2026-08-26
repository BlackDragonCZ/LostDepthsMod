package cz.blackdragoncz.lostdepths.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class LostdepthsModOres {

    private static final List<OreDefinition> ALL_ORES = new ArrayList<>();

    /** Flat for every ore by design - ores are needed in bulk. */
    public static final int REGROW_SECONDS = 150;

    // --- Pickaxe tiers (ordered lowest to highest) ---
    public enum PickaxeTier {
        FORGEFIRE(LostdepthsModItems.FORGEFIRE_PICKAXE::get, 160),
        CRYSTALIZED(LostdepthsModItems.CRYSTALIZED_PICKAXE::get, 240),
        CELESTIAL(LostdepthsModItems.CELESTIAL_PICKAXE::get, 320),
        NIGHTMARE(LostdepthsModItems.NIGHTMARE_PICKAXE::get, 480);

        private final Supplier<Item> item;
        private final int energyPerTick;

        PickaxeTier(Supplier<Item> item, int energyPerTick) {
            this.item = item;
            this.energyPerTick = energyPerTick;
        }

        public Item getItem() { return item.get(); }

        /** FE/t the Resource Extractor burns on an ore of this tier. */
        public int energyPerTick() { return energyPerTick; }
    }

    // --- Depletion behavior after mining ---
    public enum DepletionType {
        TO_EMPTY,           // Transforms to ORE_EMPTY block (with oreType NBT), regrows after REGROW_SECONDS
        CHANCE_DEACTIVATE,  // 50/50 chance to revert to unpowered variant
        TO_BEDROCK          // Transforms to bedrock (one-time mine)
    }

    // --- Ore definition record ---
    public record OreDefinition(
            String id,
            PickaxeTier minTier,
            int baseDrop,
            int tierIncrement,
            Supplier<Item> dropItem,
            DepletionType depletionType,
            @Nullable String oreEmptyTag,
            @Nullable Supplier<Item> activationSolution,
            Supplier<Block> oreBlock,
            @Nullable Supplier<Block> unpoweredBlock
    ) {
        public int getDropCount(Item pickaxe) {
            int pickaxeTierOrd = -1;
            for (PickaxeTier tier : PickaxeTier.values()) {
                if (tier.getItem() == pickaxe) {
                    pickaxeTierOrd = tier.ordinal();
                    break;
                }
            }
            if (pickaxeTierOrd < 0 || pickaxeTierOrd < minTier.ordinal()) return 0;
            return baseDrop + (pickaxeTierOrd - minTier.ordinal()) * tierIncrement;
        }

        public boolean canMine(Item pickaxe) {
            return getDropCount(pickaxe) > 0;
        }

        public boolean requiresSolution() {
            return activationSolution != null;
        }
    }

    // --- Depletion ores (turn to ORE_EMPTY, regrow) ---

    public static final OreDefinition FIRERITE_ORE = register("firerite_ore",
            PickaxeTier.FORGEFIRE, 2, 2,
            LostdepthsModItems.RAW_FIRERITE::get,
            DepletionType.TO_EMPTY, "firerite",
            LostdepthsModBlocks.FIRERITE_ORE::get);

    public static final OreDefinition MELWORIUM_ORE = register("melworium_ore",
            PickaxeTier.FORGEFIRE, 2, 2,
            LostdepthsModItems.RAW_MELWORITE::get,
            DepletionType.TO_EMPTY, "melworite",
            LostdepthsModBlocks.MELWORIUM_ORE::get);

    public static final OreDefinition MORFARITE_ORE = register("morfarite_ore",
            PickaxeTier.FORGEFIRE, 2, 2,
            LostdepthsModItems.RAW_MORFARITE::get,
            DepletionType.TO_EMPTY, "morfarite",
            LostdepthsModBlocks.MORFARITE_ORE::get);

    public static final OreDefinition CRYZULITE_ORE = register("cryzulite_ore",
            PickaxeTier.CRYSTALIZED, 2, 2,
            LostdepthsModItems.RAW_CRYZULITE::get,
            DepletionType.TO_EMPTY, "cryzulite",
            LostdepthsModBlocks.CRYZULITE_ORE::get);

    public static final OreDefinition ZERITHIUM_ORE = register("zerithium_ore",
            PickaxeTier.CRYSTALIZED, 2, 2,
            LostdepthsModItems.RAW_ZERITHIUM::get,
            DepletionType.TO_EMPTY, "zerithium",
            LostdepthsModBlocks.ZERITHIUM_ORE::get);

    public static final OreDefinition HYPERIUM_ORE = register("hyperium_ore",
            PickaxeTier.CELESTIAL, 1, 2,
            LostdepthsModItems.RAW_HYPERIUM::get,
            DepletionType.TO_EMPTY, "hyperium",
            LostdepthsModBlocks.HYPERIUM_ORE::get);

    public static final OreDefinition BIOLLITERITE_ORE = register("biolliterite_ore",
            PickaxeTier.CELESTIAL, 1, 2,
            LostdepthsModItems.RAW_BIOLLITERITE::get,
            DepletionType.TO_EMPTY, "biolliterite",
            LostdepthsModBlocks.BIOLLITERITE_ORE::get);

    public static final OreDefinition COGNITIUM_ORE = register("cognitium_ore",
            PickaxeTier.CELESTIAL, 1, 2,
            LostdepthsModItems.RAW_COGNITIUM::get,
            DepletionType.TO_EMPTY, "cognitium",
            LostdepthsModBlocks.COGNITIUM_ORE::get);

    public static final OreDefinition NECROTONITE_ORE = register("necrotonite_ore",
            PickaxeTier.CELESTIAL, 1, 2,
            LostdepthsModItems.RAW_NECROTONITE::get,
            DepletionType.TO_EMPTY, "necrotonite",
            LostdepthsModBlocks.NECROTONITE_ORE::get);

    public static final OreDefinition NOXHERTIUM_ORE = register("noxhertium_ore",
            PickaxeTier.CELESTIAL, 1, 2,
            LostdepthsModItems.RAW_NOXHERTIUM::get,
            DepletionType.TO_EMPTY, "noxhertium",
            LostdepthsModBlocks.NOXHERTIUM_ORE::get);

    public static final OreDefinition PSYCHERIUM_ORE = register("psycherium_ore",
            PickaxeTier.CELESTIAL, 1, 2,
            LostdepthsModItems.RAW_PSYCHERIUM::get,
            DepletionType.TO_EMPTY, "psycherium",
            LostdepthsModBlocks.PSYCHERIUM_ORE::get);

    public static final OreDefinition VARLLERIUM_ORE = register("varllerium_ore",
            PickaxeTier.CELESTIAL, 1, 2,
            LostdepthsModItems.RAW_VARLLERIUM::get,
            DepletionType.TO_EMPTY, "varllerium",
            LostdepthsModBlocks.VARLLERIUM_ORE::get);

    // --- Solution ores (50/50 deactivation, re-chargeable) ---

    public static final OreDefinition MYRITE_ORE = registerSolution("myrite_ore",
            PickaxeTier.NIGHTMARE, 1, 0,
            LostdepthsModItems.MYRITE_CRYSTAL::get,
            LostdepthsModItems.ULTRAVIOLET_SOLUTION::get,
            LostdepthsModBlocks.MYRITE_ORE_ACTIVE::get,
            LostdepthsModBlocks.MYRITE_ORE::get);

    public static final OreDefinition LUCIENT_ORE = registerSolution("lucient_ore",
            PickaxeTier.NIGHTMARE, 1, 0,
            LostdepthsModItems.PURE_LUCIENT::get,
            LostdepthsModItems.CORRUPTED_SOLUTION::get,
            LostdepthsModBlocks.LUCIENT_ORE_ACTIVE::get,
            LostdepthsModBlocks.LUCIENT_ORE::get);

    public static final OreDefinition PHOTOTENZYTE_ORE = registerSolution("phototenzyte_ore",
            PickaxeTier.CELESTIAL, 1, 2,
            LostdepthsModItems.PHOTOTENZYTE::get,
            LostdepthsModItems.POLY_AMPLIFICATION_SOLUTION::get,
            LostdepthsModBlocks.PHOTOTENZYTE_ORE_ACTIVE::get,
            LostdepthsModBlocks.PHOTOTENZYTE_ORE::get);

    public static final OreDefinition SERPENTINE_ORE = registerSolution("serpentine_ore",
            PickaxeTier.CRYSTALIZED, 1, 2,
            LostdepthsModItems.SERPENTINE_CRYSTAL::get,
            LostdepthsModItems.ENDER_VOLTAIC_SOLUTION::get,
            LostdepthsModBlocks.SERPENTINE_ORE::get,
            LostdepthsModBlocks.SERPENTINE_ORE_UNPOWERED::get);

    // --- One-time ores ---

    public static final OreDefinition MULTIVERSITE_ORE = register("multiversite_ore",
            PickaxeTier.NIGHTMARE, 1, 0,
            LostdepthsModItems.FRACTURED_MULTIVERSITE::get,
            DepletionType.TO_BEDROCK, null,
            LostdepthsModBlocks.MULTIVERSITE_ORE::get);

    // --- Crystals ---
    // Not ores: infinite, never deplete, so no DepletionType. Kept here as the "what can be mined" hub.

    private static final List<CrystalDefinition> ALL_CRYSTALS = new ArrayList<>();

    public record CrystalDefinition(PickaxeTier minTier, Supplier<Block> block, Supplier<Item> dropItem, int dropCount) {
        public boolean canMine(Item pickaxe) {
            for (PickaxeTier tier : PickaxeTier.values()) {
                if (tier.getItem() == pickaxe) return tier.ordinal() >= minTier.ordinal();
            }
            return false;
        }
    }

    public static final CrystalDefinition HARD_CRYSTAL_B = registerCrystal(PickaxeTier.CELESTIAL,
            LostdepthsModBlocks.HARD_CRYSTAL_B::get, LostdepthsModItems.HARD_CRYSTALS::get, 1);

    public static final CrystalDefinition HARD_CRYSTAL_R = registerCrystal(PickaxeTier.CELESTIAL,
            LostdepthsModBlocks.HARD_CRYSTAL_R::get, LostdepthsModItems.HARD_CRYSTALS::get, 1);

    private static CrystalDefinition registerCrystal(PickaxeTier minTier, Supplier<Block> block,
                                                     Supplier<Item> dropItem, int dropCount) {
        CrystalDefinition def = new CrystalDefinition(minTier, block, dropItem, dropCount);
        ALL_CRYSTALS.add(def);
        return def;
    }

    @Nullable
    public static CrystalDefinition findCrystal(Block block) {
        for (CrystalDefinition def : ALL_CRYSTALS) {
            if (block == def.block.get()) return def;
        }
        return null;
    }

    // --- Registry helpers ---

    /** Drop count is baseDrop at minTier, plus tierIncrement per tier above. */
    private static OreDefinition register(String id, PickaxeTier minTier, int baseDrop, int tierIncrement,
                                          Supplier<Item> dropItem, DepletionType depletionType,
                                          @Nullable String oreEmptyTag, Supplier<Block> oreBlock) {
        return add(new OreDefinition(id, minTier, baseDrop, tierIncrement, dropItem,
                depletionType, oreEmptyTag, null, oreBlock, null));
    }

    /** Solution-activated, 50/50 to fall back to the unpowered block when mined. */
    private static OreDefinition registerSolution(String id, PickaxeTier minTier, int baseDrop, int tierIncrement,
                                                  Supplier<Item> dropItem, Supplier<Item> activationSolution,
                                                  Supplier<Block> activeBlock, Supplier<Block> unpoweredBlock) {
        return add(new OreDefinition(id, minTier, baseDrop, tierIncrement, dropItem,
                DepletionType.CHANCE_DEACTIVATE, null, activationSolution, activeBlock, unpoweredBlock));
    }

    private static OreDefinition add(OreDefinition def) {
        ALL_ORES.add(def);
        return def;
    }

    /** Matches the mineable block or its unpowered variant. */
    @Nullable
    public static OreDefinition findByBlock(Block block) {
        for (OreDefinition def : ALL_ORES) {
            if (block == def.oreBlock.get()) return def;
            if (def.unpoweredBlock != null && block == def.unpoweredBlock.get()) return def;
        }
        return null;
    }

    /** Matches the oreType tag stored on an ORE_EMPTY node. */
    @Nullable
    public static OreDefinition findByOreTag(String oreTag) {
        if (oreTag == null || oreTag.isEmpty()) return null;
        for (OreDefinition def : ALL_ORES) {
            if (oreTag.equals(def.oreEmptyTag)) return def;
        }
        return null;
    }

    public static boolean isUnpowered(OreDefinition def, Block block) {
        return def.unpoweredBlock != null && block == def.unpoweredBlock.get();
    }

    public static boolean isActive(OreDefinition def, Block block) {
        return block == def.oreBlock.get();
    }

    public static List<OreDefinition> getAll() {
        return Collections.unmodifiableList(ALL_ORES);
    }

    public static void init() {}
}
