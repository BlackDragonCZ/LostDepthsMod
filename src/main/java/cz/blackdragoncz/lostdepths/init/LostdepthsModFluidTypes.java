
/*
 * MCreator note: This file will be REGENERATED on each build.
 */
package cz.blackdragoncz.lostdepths.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fluids.FluidType;

import cz.blackdragoncz.lostdepths.fluid.types.ConcentratedAcidFluidType;
import cz.blackdragoncz.lostdepths.LostdepthsMod;

public class LostdepthsModFluidTypes {
	public static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, LostdepthsMod.MODID);
	public static final RegistryObject<FluidType> CONCENTRATED_ACID_TYPE = REGISTRY.register("concentrated_acid", () -> new ConcentratedAcidFluidType());
}
