
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package cz.blackdragoncz.lostdepths.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;

import cz.blackdragoncz.lostdepths.LostdepthsMod;

public class LostdepthsModParticleTypes {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, LostdepthsMod.MODID);
	public static final RegistryObject<SimpleParticleType> VENOMPARTICLE = REGISTRY.register("venomparticle", () -> new SimpleParticleType(false));
}
