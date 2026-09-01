package cz.blackdragoncz.lostdepths.procedures;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

import cz.blackdragoncz.lostdepths.entity.TheProtectorEntity;
import cz.blackdragoncz.lostdepths.init.LostdepthsModEntities;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.init.LostdepthsModSounds;

/*
 * Every "use item on entity" interaction, in one table. This used to be four separate event subscribers -
 * the rod's own 237-line if-chain plus one class each for the stick, the bottle and the slippery ingot -
 * and the slippery ingot was implemented twice, so once both copies worked it converted twice per click.
 *
 * Mirrors data/lostdepths/recipes/item_use/*.json, which is what JEI shows. Add entries to both.
 */
@Mod.EventBusSubscriber
public class EntityItemUseProcedure {

	// Held item used on a tagged entity -> the target is replaced by a summoned mob.
	private record Transform(Supplier<Item> tool, TagKey<EntityType<?>> target, Supplier<EntityType<?>> result,
			boolean consumeTool, boolean playerCreated) {
	}

	// Held item used on a tagged entity -> one of the held item becomes the result item.
	private record Conversion(Supplier<Item> tool, TagKey<EntityType<?>> target, Supplier<Item> result,
			@Nullable Supplier<SoundEvent> sound, float damageTarget) {
	}

	private static final List<Transform> TRANSFORMS = List.of(
			new Transform(LostdepthsModItems.ROD_OF_TRANSFORMATION, tag("iron_golem"), () -> LostdepthsModEntities.THE_PROTECTOR.get(), false, false),
			new Transform(LostdepthsModItems.ROD_OF_TRANSFORMATION, tag("creeper"), () -> LostdepthsModEntities.MAELSTROM.get(), false, false),
			new Transform(LostdepthsModItems.ROD_OF_TRANSFORMATION, tag("ghast"), () -> LostdepthsModEntities.LOST_DARK.get(), false, false),
			new Transform(LostdepthsModItems.ROD_OF_TRANSFORMATION, tag("slime"), () -> LostdepthsModEntities.GUOON.get(), false, false),
			new Transform(LostdepthsModItems.ROD_OF_TRANSFORMATION, tag("bee"), () -> LostdepthsModEntities.FLAPPER.get(), false, false),
			new Transform(LostdepthsModItems.ROD_OF_TRANSFORMATION, tag("blaze"), () -> LostdepthsModEntities.NEUROBLAZE.get(), false, false),
			new Transform(LostdepthsModItems.ROD_OF_TRANSFORMATION, tag("spider"), () -> LostdepthsModEntities.ARACHNOTA.get(), false, false),
			new Transform(LostdepthsModItems.ROD_OF_TRANSFORMATION, tag("wolf"), () -> LostdepthsModEntities.ASTRALCLAW.get(), false, false),
			new Transform(LostdepthsModItems.INFUSED_GOLEM_ESSENCE, tag("iron_golem"), () -> LostdepthsModEntities.THE_PROTECTOR.get(), true, true));

	private static final List<Conversion> CONVERSIONS = List.of(
			new Conversion(LostdepthsModItems.FROZEN_INGOT, tag("the_protector_entity"), LostdepthsModItems.SPECTRAL_INGOT, null, 0),
			new Conversion(LostdepthsModItems.REINFORCED_BLADE, tag("the_protector_entity"), LostdepthsModItems.LASER_BLADE, null, 0),
			new Conversion(LostdepthsModItems.SLIPPERY_INGOT, tag("blaze"), LostdepthsModItems.UNSTABLE_INGOT, null, 0),
			new Conversion(LostdepthsModItems.REINFORCED_BOTTLE, tag("cave_spider"), LostdepthsModItems.CONCENTRATED_VENOM, null, 0),
			new Conversion(() -> Items.STICK, tag("zombie"), LostdepthsModItems.PLUCKED_EYE, LostdepthsModSounds.PLUCKED_EYE_AMBIENT, 200));

	private static TagKey<EntityType<?>> tag(String name) {
		return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("lostdepths", name));
	}

	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		if (event.getHand() != event.getEntity().getUsedItemHand())
			return;
		execute(event.getLevel(), event.getTarget(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity target, Entity sourceentity) {
		if (target == null || !(sourceentity instanceof Player player) || !(world instanceof ServerLevel level))
			return;

		Item held = player.getMainHandItem().getItem();
		for (Transform t : TRANSFORMS) {
			if (held == t.tool().get() && target.getType().is(t.target())) {
				transform(level, player, target, t);
				return;
			}
		}
		for (Conversion c : CONVERSIONS) {
			if (held == c.tool().get() && target.getType().is(c.target())) {
				convert(level, player, target, c);
				return;
			}
		}
	}

	private static void transform(ServerLevel level, Player player, Entity target, Transform t) {
		BlockPos pos = target.blockPosition();
		float yaw = target.getYRot(), pitch = target.getXRot();

		target.discard();
		level.playSound(null, player.blockPosition(), LostdepthsModSounds.CONVERSION.get(), SoundSource.HOSTILE, 2.0F, 1.0F);

		if (t.playerCreated()) {
			// Summoned deliberately: persistent, keeps the old mob's facing, and counts as a summon.
			Entity spawned = t.result().get().create(level);
			if (spawned instanceof TheProtectorEntity protector) {
				protector.setPlayerCreated(true);
				protector.setPersistenceRequired();
				protector.moveTo(pos, yaw, pitch);
				level.addFreshEntity(protector);
				for (ServerPlayer nearby : level.getEntitiesOfClass(ServerPlayer.class, protector.getBoundingBox().inflate(5.0D)))
					CriteriaTriggers.SUMMONED_ENTITY.trigger(nearby, protector);
			}
		} else {
			Entity spawned = t.result().get().spawn(level, pos, MobSpawnType.MOB_SUMMONED);
			if (spawned != null)
				spawned.setDeltaMovement(0, 0, 0);
		}

		if (t.consumeTool())
			player.getMainHandItem().shrink(1);
	}

	private static void convert(ServerLevel level, Player player, Entity target, Conversion c) {
		player.getMainHandItem().shrink(1);
		ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(c.result().get()));

		if (c.sound() != null)
			level.playSound(null, target.blockPosition(), c.sound().get(), SoundSource.HOSTILE, 1, 1);
		if (c.damageTarget() > 0)
			target.hurt(level.damageSources().playerAttack(player), c.damageTarget());
	}
}
