package cz.blackdragoncz.lostdepths.init;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.world.biome.modifier.TheProtectorBiomeModifier;
import cz.blackdragoncz.lostdepths.world.biome.modifier.FlapperBiomeModifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

public class LostDepthsBiomeModifiers {

    public static DeferredRegister<Codec<? extends BiomeModifier>> REGISTRY =
            DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, LostdepthsMod.MODID);

    public static RegistryObject<Codec<TheProtectorBiomeModifier>> THE_PROTECTOR = REGISTRY.register("the_protector", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(TheProtectorBiomeModifier::biomes)
            ).apply(builder, TheProtectorBiomeModifier::new)));

    public static RegistryObject<Codec<FlapperBiomeModifier>> FLAPPER = REGISTRY.register("flapper", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(FlapperBiomeModifier::biomes)
            ).apply(builder, FlapperBiomeModifier::new)));

}
