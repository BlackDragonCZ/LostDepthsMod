
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package cz.blackdragoncz.lostdepths.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.effect.MobEffect;

import cz.blackdragoncz.lostdepths.potion.UltraHealMobEffect;
import cz.blackdragoncz.lostdepths.potion.IronheartMobEffect;
import cz.blackdragoncz.lostdepths.potion.AntiWarpMobEffect;
import cz.blackdragoncz.lostdepths.LostdepthsMod;

public class LostdepthsModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, LostdepthsMod.MODID);
	public static final RegistryObject<MobEffect> ULTRA_HEAL = REGISTRY.register("ultra_heal", () -> new UltraHealMobEffect());
	public static final RegistryObject<MobEffect> IRONHEART = REGISTRY.register("ironheart", () -> new IronheartMobEffect());
	public static final RegistryObject<MobEffect> ANTI_WARP = REGISTRY.register("anti_warp", () -> new AntiWarpMobEffect());
}
