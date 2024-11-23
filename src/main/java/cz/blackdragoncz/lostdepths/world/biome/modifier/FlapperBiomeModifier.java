package cz.blackdragoncz.lostdepths.world.biome.modifier;

import com.mojang.serialization.Codec;
import cz.blackdragoncz.lostdepths.config.LostDepthsConfig;
import cz.blackdragoncz.lostdepths.init.LostDepthsBiomeModifiers;
import cz.blackdragoncz.lostdepths.init.LostdepthsModEntities;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public record FlapperBiomeModifier(HolderSet<Biome> biomes) implements BiomeModifier {

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (!this.biomes.contains(biome)) {
            return;
        }

        builder.getMobSpawnSettings().addSpawn(
                MobCategory.MONSTER,
                new MobSpawnSettings.SpawnerData(LostdepthsModEntities.FLAPPER.get(), LostDepthsConfig.FLAPPER_SPAWN_WEIGHT, 1, 1));
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return LostDepthsBiomeModifiers.FLAPPER.get();
    }
}
