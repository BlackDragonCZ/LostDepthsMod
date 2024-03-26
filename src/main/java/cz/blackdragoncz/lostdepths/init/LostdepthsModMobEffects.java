
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package cz.blackdragoncz.lostdepths.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.effect.MobEffect;

import cz.blackdragoncz.lostdepths.potion.UltraHealMobEffect;
import cz.blackdragoncz.lostdepths.potion.SecurityBypassMobEffect;
import cz.blackdragoncz.lostdepths.potion.SecurityBypass6MobEffect;
import cz.blackdragoncz.lostdepths.potion.SecurityBypass5MobEffect;
import cz.blackdragoncz.lostdepths.potion.SecurityBypass4MobEffect;
import cz.blackdragoncz.lostdepths.potion.SecurityBypass3MobEffect;
import cz.blackdragoncz.lostdepths.potion.SecurityBypass2MobEffect;
import cz.blackdragoncz.lostdepths.potion.IronheartMobEffect;
import cz.blackdragoncz.lostdepths.potion.AntiWarpMobEffect;
import cz.blackdragoncz.lostdepths.LostdepthsMod;

public class LostdepthsModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, LostdepthsMod.MODID);
	public static final RegistryObject<MobEffect> SECURITY_BYPASS = REGISTRY.register("security_bypass", () -> new SecurityBypassMobEffect());
	public static final RegistryObject<MobEffect> ULTRA_HEAL = REGISTRY.register("ultra_heal", () -> new UltraHealMobEffect());
	public static final RegistryObject<MobEffect> SECURITY_BYPASS_2 = REGISTRY.register("security_bypass_2", () -> new SecurityBypass2MobEffect());
	public static final RegistryObject<MobEffect> SECURITY_BYPASS_3 = REGISTRY.register("security_bypass_3", () -> new SecurityBypass3MobEffect());
	public static final RegistryObject<MobEffect> IRONHEART = REGISTRY.register("ironheart", () -> new IronheartMobEffect());
	public static final RegistryObject<MobEffect> ANTI_WARP = REGISTRY.register("anti_warp", () -> new AntiWarpMobEffect());
	public static final RegistryObject<MobEffect> SECURITY_BYPASS_4 = REGISTRY.register("security_bypass_4", () -> new SecurityBypass4MobEffect());
	public static final RegistryObject<MobEffect> SECURITY_BYPASS_5 = REGISTRY.register("security_bypass_5", () -> new SecurityBypass5MobEffect());
	public static final RegistryObject<MobEffect> SECURITY_BYPASS_6 = REGISTRY.register("security_bypass_6", () -> new SecurityBypass6MobEffect());
}
