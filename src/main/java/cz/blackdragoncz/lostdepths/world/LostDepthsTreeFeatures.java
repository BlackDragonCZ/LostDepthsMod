package cz.blackdragoncz.lostdepths.world;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class LostDepthsTreeFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> CELESTIAL_TREE_BLUE = ResourceKey.create(Registries.CONFIGURED_FEATURE, LostdepthsMod.rl("celestial_tree_blue_feature"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> CELESTIAL_TREE_RED = ResourceKey.create(Registries.CONFIGURED_FEATURE, LostdepthsMod.rl("celestial_tree_red_feature"));

}
