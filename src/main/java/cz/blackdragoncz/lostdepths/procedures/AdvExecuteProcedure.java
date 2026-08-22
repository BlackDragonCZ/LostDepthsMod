package cz.blackdragoncz.lostdepths.procedures;

import cz.blackdragoncz.lostdepths.advancements.LostdepthsAdvancementTriggers;
import cz.blackdragoncz.lostdepths.advancements.LostdepthsAdvancements;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.entity.Entity;

@Mod.EventBusSubscriber
public class AdvExecuteProcedure {

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		execute(event.getEntity());
	}

	public static void execute(Entity entity) {
		if (entity == null)
			return;
		LostdepthsAdvancementTriggers.award(entity, LostdepthsAdvancements.INSTALL_ADV);
	}
}
