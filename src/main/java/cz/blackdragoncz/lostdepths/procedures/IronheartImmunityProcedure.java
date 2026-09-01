package cz.blackdragoncz.lostdepths.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import net.minecraft.world.entity.player.Player;

import cz.blackdragoncz.lostdepths.init.LostdepthsModMobEffects;

// Ironheart negates incoming damage. Players only - any mob that picked the effect up used to become
// unkillable. Was named OnPlayerDamageProcedure while applying to every LivingEntity.
@Mod.EventBusSubscriber
public class IronheartImmunityProcedure {

	@SubscribeEvent
	public static void onEntityHurt(LivingHurtEvent event) {
		if (!(event.getEntity() instanceof Player player))
			return;
		if (player.hasEffect(LostdepthsModMobEffects.IRONHEART.get()))
			event.setCanceled(true);
	}
}
