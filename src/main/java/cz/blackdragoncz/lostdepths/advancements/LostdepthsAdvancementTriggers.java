package cz.blackdragoncz.lostdepths.advancements;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

// Grants the advancements whose criterion is minecraft:impossible, which can only be awarded from code.
public final class LostdepthsAdvancementTriggers {

	private LostdepthsAdvancementTriggers() {
	}

	public static void award(Entity entity, LostdepthsAdvancementBuilder advancement) {
		if (entity instanceof ServerPlayer player)
			award(player, advancement);
	}

	public static void award(ServerPlayer player, LostdepthsAdvancementBuilder advancement) {
		Advancement resolved = player.server.getAdvancements().getAdvancement(advancement.id());
		if (resolved == null)
			return;
		AdvancementProgress progress = player.getAdvancements().getOrStartProgress(resolved);
		if (progress.isDone())
			return;
		for (String criterion : progress.getRemainingCriteria())
			player.getAdvancements().award(resolved, criterion);
	}

	public static boolean isDone(Entity entity, LostdepthsAdvancementBuilder advancement) {
		if (!(entity instanceof ServerPlayer player))
			return false;
		Advancement resolved = player.server.getAdvancements().getAdvancement(advancement.id());
		return resolved != null && player.getAdvancements().getOrStartProgress(resolved).isDone();
	}
}
