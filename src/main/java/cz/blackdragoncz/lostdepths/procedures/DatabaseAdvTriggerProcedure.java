package cz.blackdragoncz.lostdepths.procedures;

import cz.blackdragoncz.lostdepths.advancements.LostdepthsAdvancementTriggers;
import cz.blackdragoncz.lostdepths.advancements.LostdepthsAdvancements;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

@Mod.EventBusSubscriber
public class DatabaseAdvTriggerProcedure {

	// The lore book is only checked once a second; scanning the inventory every tick for every player was pure overhead.
	private static final int CHECK_INTERVAL = 20;

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END && event.player.tickCount % CHECK_INTERVAL == 0)
			execute(event.player);
	}

	public static void execute(Entity entity) {
		if (!(entity instanceof ServerPlayer player))
			return;
		if (player.getInventory().hasAnyMatching(stack -> stack.is(LostdepthsModItems.LOREBOOKICON.get())))
			LostdepthsAdvancementTriggers.award(player, LostdepthsAdvancements.DATABASELOG_1);
		if (LostdepthsAdvancementTriggers.isDone(player, LostdepthsAdvancements.DATABASELOG_1))
			LostdepthsAdvancementTriggers.award(player, LostdepthsAdvancements.DATABASEADV);
	}
}
