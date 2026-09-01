package cz.blackdragoncz.lostdepths.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mod.EventBusSubscriber
public class NightVisionFumesProcedure {

	private static final ResourceKey<Level> BELOW_BEDROCK = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("lostdepths:below_bedrock"));
	private static final ResourceKey<Level> BETWEEN_BEDROCK = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("lostdepths:between_bedrock_and_overworld"));

	private static final int MESSAGE_INTERVAL = 20;

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END)
			return;
		Player player = event.player;
		// Server owns health. Doing this client-side only desynced the bar.
		if (player.level().isClientSide())
			return;
		// NEVER touch a corpse. Healing a dead player leaves the server thinking they are alive while the
		// client sits on the death screen, and ServerGamePacketListenerImpl drops PERFORM_RESPAWN when
		// health > 0 - the player is then locked out of that world for good.
		if (player.isDeadOrDying() || player.isSpectator())
			return;
		execute(player);
	}

	public static void execute(Player player) {
		ResourceKey<Level> dim = player.level().dimension();
		if (dim != BELOW_BEDROCK && dim != BETWEEN_BEDROCK)
			return;
		if (!player.hasEffect(MobEffects.NIGHT_VISION))
			return;

		if (player.getHealth() > 1.0F)
			player.setHealth(1.0F);
		if (player.tickCount % MESSAGE_INTERVAL == 0)
			player.displayClientMessage(Component.literal("[§4LostDepths§f] §4Nightvision causes your eyes to burn from fumes in the air. You feel severely weakened."), true);
	}
}
