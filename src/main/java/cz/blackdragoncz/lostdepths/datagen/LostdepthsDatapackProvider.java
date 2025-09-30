package cz.blackdragoncz.lostdepths.datagen;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.init.LostdepthsModDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class LostdepthsDatapackProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, ctx -> {
                ctx.register(LostdepthsModDamageTypes.TRUE_DAMAGE, new DamageType(
                        LostdepthsMod.MODID + ".true_damage",
                        DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
                        0.1F,
                        DamageEffects.HURT,
                        DeathMessageType.DEFAULT
                ));
            });

    public LostdepthsDatapackProvider(PackOutput output,
                                      CompletableFuture<HolderLookup.Provider> baseLookup) {
        super(output, baseLookup, BUILDER, Set.of(LostdepthsMod.MODID));
    }
}
