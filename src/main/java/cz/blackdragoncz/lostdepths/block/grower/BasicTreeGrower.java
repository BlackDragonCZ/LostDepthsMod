package cz.blackdragoncz.lostdepths.block.grower;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class BasicTreeGrower extends AbstractTreeGrower {

    private final ResourceKey<ConfiguredFeature<?, ?>> featureResourceKey;

    public BasicTreeGrower(ResourceKey<ConfiguredFeature<?, ?>> featureResourceKey) {
        this.featureResourceKey = featureResourceKey;
    }

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource pRandom, boolean pHasFlowers) {
        return featureResourceKey;
    }
}
