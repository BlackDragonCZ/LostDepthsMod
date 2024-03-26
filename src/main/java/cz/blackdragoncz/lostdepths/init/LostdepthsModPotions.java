
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package cz.blackdragoncz.lostdepths.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.effect.MobEffectInstance;

import cz.blackdragoncz.lostdepths.LostdepthsMod;

public class LostdepthsModPotions {
	public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, LostdepthsMod.MODID);
	public static final RegistryObject<Potion> ULTRA_HEAL_POTION = REGISTRY.register("ultra_heal_potion", () -> new Potion(new MobEffectInstance(LostdepthsModMobEffects.ULTRA_HEAL.get(), 12, 0, false, true)));
}
