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

    // --- Pickaxe tiers (ordered lowest to highest) ---
    public enum PickaxeTier {
        FORGEFIRE(LostdepthsModItems.FORGEFIRE_PICKAXE::get),
        CRYSTALIZED(LostdepthsModItems.CRYSTALIZED_PICKAXE::get),
        CELESTIAL(LostdepthsModItems.CELESTIAL_PICKAXE::get),
        NIGHTMARE(LostdepthsModItems.NIGHTMARE_PICKAXE::get);

        private final Supplier<Item> item;

        PickaxeTier(Supplier<Item> item) {
            this.item = item;
        }

        public Item getItem() { return item.get(); }
    }

    // --- Depletion behavior after mining ---
    public enum DepletionType {
        TO_EMPTY,           // Transforms to ORE_EMPTY block (with oreType NBT)
        CHANCE_DEACTIVATE,  // 3/11 chance to revert to unpowered variant
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
            @Nullable Supplier<Block> unpoweredBlock,
            @Nullable Supplier<Block> activeBlock
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

    // --- Depletion ores (turn to ORE_EMPTY) ---

    public static final OreDefinition FIRERITE_ORE = register("firerite_ore",
            PickaxeTier.FORGEFIRE, 2, 2,
            LostdepthsModItems.RAW_FIRERITE::get,
            DepletionType.TO_EMPTY, "firerite",
            null, null, null);

    public static final OreDefinition MELWORIUM_ORE = register("melworium_ore",
            PickaxeTier.FORGEFIRE, 2, 2,
            LostdepthsModItems.RAW_MELWORITE::get,
            DepletionType.TO_EMPTY, "melworite",
            null, null, null);

    public static final OreDefinition MORFARITE_ORE = register("morfarite_ore",
            PickaxeTier.FORGEFIRE, 2, 2,
            LostdepthsModItems.RAW_MORFARITE::get,
            DepletionType.TO_EMPTY, "morfarite",
            null, null, null);

    public static final OreDefinition HYPERIUM_ORE = register("hyperium_ore",
            PickaxeTier.CELESTIAL, 1, 2,
            LostdepthsModItems.RAW_HYPERIUM::get,
            DepletionType.TO_EMPTY, "hyperium",
            null, null, null);

    // --- Solution ores (chance deactivation) ---

    public static final OreDefinition MYRITE_ORE = register("myrite_ore",
            PickaxeTier.NIGHTMARE, 1, 0,
            LostdepthsModItems.MYRITE_CRYSTAL::get,
            DepletionType.CHANCE_DEACTIVATE, null,
            LostdepthsModItems.ULTRAVIOLET_SOLUTION::get,
            LostdepthsModBlocks.MYRITE_ORE::get,
            LostdepthsModBlocks.MYRITE_ORE_ACTIVE::get);

    public static final OreDefinition LUCIENT_ORE = register("lucient_ore",
            PickaxeTier.NIGHTMARE, 1, 0,
            LostdepthsModItems.PURE_LUCIENT::get,
            DepletionType.CHANCE_DEACTIVATE, null,
            LostdepthsModItems.CORRUPTED_SOLUTION::get,
            LostdepthsModBlocks.LUCIENT_ORE::get,
            LostdepthsModBlocks.LUCIENT_ORE_ACTIVE::get);

    public static final OreDefinition PHOTOTENZYTE_ORE = register("phototenzyte_ore",
            PickaxeTier.CELESTIAL, 1, 2,
            LostdepthsModItems.PHOTOTENZYTE::get,
            DepletionType.CHANCE_DEACTIVATE, null,
            LostdepthsModItems.POLY_AMPLIFICATION_SOLUTION::get,
            LostdepthsModBlocks.PHOTOTENZYTE_ORE::get,
            LostdepthsModBlocks.PHOTOTENZYTE_ORE_ACTIVE::get);

    public static final OreDefinition SERPENTINE_ORE = register("serpentine_ore",
            PickaxeTier.CRYSTALIZED, 1, 2,
            LostdepthsModItems.SERPENTINE_CRYSTAL::get,
            DepletionType.CHANCE_DEACTIVATE, null,
            LostdepthsModItems.ENDER_VOLTAIC_SOLUTION::get,
            LostdepthsModBlocks.SERPENTINE_ORE_UNPOWERED::get,
            LostdepthsModBlocks.SERPENTINE_ORE::get);

    // --- One-time ores ---

    public static final OreDefinition MULTIVERSITE_ORE = register("multiversite_ore",
            PickaxeTier.NIGHTMARE, 1, 0,
            LostdepthsModItems.FRACTURED_MULTIVERSITE::get,
            DepletionType.TO_BEDROCK, null,
            null, null, null);

    // --- Registry helpers ---

    private static OreDefinition register(String id, PickaxeTier minTier, int baseDrop, int tierIncrement,
                                          Supplier<Item> dropItem, DepletionType depletionType,
                                          @Nullable String oreEmptyTag,
                                          @Nullable Supplier<Item> activationSolution,
                                          @Nullable Supplier<Block> unpoweredBlock,
                                          @Nullable Supplier<Block> activeBlock) {
        OreDefinition def = new OreDefinition(id, minTier, baseDrop, tierIncrement, dropItem,
                depletionType, oreEmptyTag, activationSolution, unpoweredBlock, activeBlock);
        ALL_ORES.add(def);
        return def;
    }

    @Nullable
    public static OreDefinition findByBlock(Block block) {
        for (OreDefinition def : ALL_ORES) {
            if (def.activeBlock != null && block == def.activeBlock.get()) return def;
            if (def.unpoweredBlock != null && block == def.unpoweredBlock.get()) return def;
        }
        String descId = block.getDescriptionId();
        for (OreDefinition def : ALL_ORES) {
            if (def.activeBlock == null && descId.contains(def.id.replace("_ore", ""))) return def;
        }
        return null;
    }

    public static boolean isUnpowered(OreDefinition def, Block block) {
        return def.unpoweredBlock != null && block == def.unpoweredBlock.get();
    }

    public static boolean isActive(OreDefinition def, Block block) {
        return def.activeBlock != null && block == def.activeBlock.get();
    }

    public static List<OreDefinition> getAll() {
        return Collections.unmodifiableList(ALL_ORES);
    }

    public static void init() {}
}
