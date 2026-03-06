package cz.blackdragoncz.lostdepths.config;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = LostdepthsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LostDepthsConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<Integer> THE_PROTECTOR_SPAWN_WEIGHT_VAL =
            BUILDER.comment("Specify the protector spawn weight")
                    .define("the_protector_spawn_weight", 5);

    public static final ForgeConfigSpec.ConfigValue<Integer> FLAPPER_SPAWN_WEIGHT_VAL =
            BUILDER.comment("Specify the flapper spawn weight")
                    .define("flapper_spawn_weight", 50);

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> THE_PROTECTOR_DIMENSIONS_VAL =
            BUILDER.comment(
                    "Dimensions where The Protector can spawn.",
                    "Use dimension IDs (e.g. 'minecraft:overworld').",
                    "Prefix with '!' to exclude (e.g. '!minecraft:the_nether').",
                    "Empty list = all dimensions allowed.")
                    .defineListAllowEmpty(List.of("the_protector_spawn_dimensions"),
                            () -> List.of("minecraft:overworld",
                                    "lostdepths:below_bedrock",
                                    "lostdepths:between_bedrock_and_overworld",
                                    "lostdepths:lost_dungeons"),
                            e -> e instanceof String);

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> THE_PROTECTOR_BIOMES_VAL =
            BUILDER.comment(
                    "Biomes where The Protector can spawn.",
                    "Use biome IDs (e.g. 'minecraft:plains').",
                    "Prefix with '!' to exclude (e.g. '!minecraft:deep_ocean').",
                    "Empty list = all biomes allowed.")
                    .defineListAllowEmpty(List.of("the_protector_spawn_biomes"),
                            List::of,
                            e -> e instanceof String);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int THE_PROTECTOR_SPAWN_WEIGHT = 5;
    public static int FLAPPER_SPAWN_WEIGHT = 50;
    public static List<? extends String> THE_PROTECTOR_DIMENSIONS = List.of(
            "minecraft:overworld",
            "lostdepths:below_bedrock",
            "lostdepths:between_bedrock_and_overworld",
            "lostdepths:lost_dungeons");
    public static List<? extends String> THE_PROTECTOR_BIOMES = List.of();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        THE_PROTECTOR_SPAWN_WEIGHT = THE_PROTECTOR_SPAWN_WEIGHT_VAL.get();
        FLAPPER_SPAWN_WEIGHT = FLAPPER_SPAWN_WEIGHT_VAL.get();
        THE_PROTECTOR_DIMENSIONS = THE_PROTECTOR_DIMENSIONS_VAL.get();
        THE_PROTECTOR_BIOMES = THE_PROTECTOR_BIOMES_VAL.get();
    }

    /**
     * Checks if an ID is allowed by a config list with ! negation support.
     * - Empty list = all allowed
     * - Entries without ! = whitelist (only these are allowed)
     * - Entries with ! = blacklist (these are excluded)
     * - Negation always wins over inclusion
     */
    public static boolean isAllowed(String id, List<? extends String> configList) {
        if (configList.isEmpty()) return true;

        boolean hasIncludes = false;
        boolean included = false;

        for (String entry : configList) {
            if (entry.startsWith("!")) {
                if (entry.substring(1).equals(id)) return false;
            } else {
                hasIncludes = true;
                if (entry.equals(id)) included = true;
            }
        }

        return !hasIncludes || included;
    }
}
