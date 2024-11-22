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
import org.jetbrains.annotations.NotNull;

public record TheProtectorBiomeModifier(HolderSet<Biome> biomes) implements BiomeModifier {

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder)
    {
        builder.getMobSpawnSettings().addSpawn(
                MobCategory.CREATURE,
                new MobSpawnSettings.SpawnerData(LostdepthsModEntities.THE_PROTECTOR.get(), LostDepthsConfig.THE_PROTECTOR_SPAWN_WEIGHT, 1, 1));
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return LostDepthsBiomeModifiers.THE_PROTECTOR.get();
    }
}
