
/*
 * MCreator note: This file will be REGENERATED on each build.
 */
package cz.blackdragoncz.lostdepths.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;

import cz.blackdragoncz.lostdepths.fluid.ConcentratedAcidFluid;
import cz.blackdragoncz.lostdepths.LostdepthsMod;

public class LostdepthsModFluids {
	public static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(ForgeRegistries.FLUIDS, LostdepthsMod.MODID);
	public static final RegistryObject<FlowingFluid> CONCENTRATED_ACID = REGISTRY.register("concentrated_acid", () -> new ConcentratedAcidFluid.Source());
	public static final RegistryObject<FlowingFluid> FLOWING_CONCENTRATED_ACID = REGISTRY.register("flowing_concentrated_acid", () -> new ConcentratedAcidFluid.Flowing());

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientSideHandler {
		@SubscribeEvent
		public static void clientSetup(FMLClientSetupEvent event) {
			ItemBlockRenderTypes.setRenderLayer(CONCENTRATED_ACID.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(FLOWING_CONCENTRATED_ACID.get(), RenderType.translucent());
		}
	}
}
