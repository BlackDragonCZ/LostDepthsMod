package cz.blackdragoncz.lostdepths.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class OnPlayerInDimensionTickProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player);
		}
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (((entity.level().dimension()) == (ResourceKey.create(Registries.DIMENSION, new ResourceLocation("lostdepths:below_bedrock")))
				|| (entity.level().dimension()) == (ResourceKey.create(Registries.DIMENSION, new ResourceLocation("lostdepths:between_bedrock_and_overworld")))) && entity instanceof LivingEntity _livEnt6
				&& _livEnt6.hasEffect(MobEffects.NIGHT_VISION)) {
			if (entity instanceof LivingEntity _entity)
				_entity.setHealth(1);
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal(("[\u00A74LostDepths\u00A7f] " + "\u00A74Nightvision causes your eyes to burn from fumes in the air. You feel severely weakened.")), true);
		}
	}
}
