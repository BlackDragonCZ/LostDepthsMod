
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package cz.blackdragoncz.lostdepths.init;

import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.GameRules;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LostdepthsModGameRules {
	public static final GameRules.Key<GameRules.BooleanValue> DOLOSTDEPTHSSPAWNING = GameRules.register("doLostDepthsSpawning", GameRules.Category.SPAWNING, GameRules.BooleanValue.create(true));
}
