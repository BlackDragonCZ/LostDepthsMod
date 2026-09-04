package cz.blackdragoncz.lostdepths.disruptor;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.client.FlyArmorProcedure;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Phase.END at LOWEST so we run after anything that hands flight out during the tick - an angel ring re-granting mayfly
// every tick would otherwise win the race half the time.
@Mod.EventBusSubscriber(modid = LostdepthsMod.MODID)
public final class GravatorEvents {

    // Set while a player is grounded by a disruptor, so leaving the zone can hand armour flight back.
    private static final String GROUNDED = "LostdepthsGravatorGrounded";

    private GravatorEvents() {
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player))
            return;
        // Never touch a corpse or a spectator: see the night vision bug in NightVisionFumesProcedure.
        if (player.isDeadOrDying() || player.isSpectator() || player.isCreative())
            return;

        boolean affected = GravatorDisruptorManager.isAffected(player);
        boolean wasGrounded = player.getPersistentData().getBoolean(GROUNDED);

        if (!affected) {
            if (wasGrounded) {
                player.getPersistentData().putBoolean(GROUNDED, false);
                forget(player.getUUID());
                // Re-derive rather than granting: the armour rules live in one place.
                FlyArmorProcedure.apply(player);
            }
            return;
        }

        if (!wasGrounded)
            player.getPersistentData().putBoolean(GROUNDED, true);

        if (player.getAbilities().mayfly || player.getAbilities().flying) {
            player.getAbilities().mayfly = false;
            player.getAbilities().flying = false;
            player.onUpdateAbilities();
        }
        if (player.isFallFlying())
            player.stopFallFlying();
        if (player.hasEffect(MobEffects.LEVITATION))
            player.removeEffect(MobEffects.LEVITATION);

        if (GravatorDisruptorManager.isStrict(player))
            clampAscent(player);
    }

    // The blunt layer. Jetpacks write velocity straight onto the player and expose no hook, so the only thing that stops
    // them is refusing upward motion. Judged by how long the ascent has lasted, not its speed: a jump opens at 0.42/tick,
    // faster than most jetpacks, so a speed threshold would ban jumping and miss a slow hover.
    private static final int JUMP_GRACE_TICKS = 8; // a vanilla jump reaches its apex in about six
    private static final Map<UUID, Integer> risingTicks = new HashMap<>();

    private static void clampAscent(ServerPlayer player) {
        UUID id = player.getUUID();
        if (player.onGround() || player.isInWater() || player.isInLava() || player.onClimbable() || player.isPassenger()) {
            risingTicks.remove(id);
            return;
        }

        Vec3 motion = player.getDeltaMovement();
        if (motion.y <= 0) {
            risingTicks.remove(id);
            return;
        }

        int rising = risingTicks.merge(id, 1, Integer::sum);
        if (rising <= JUMP_GRACE_TICKS)
            return;

        player.setDeltaMovement(motion.x, -0.1, motion.z);
        player.hurtMarked = true;
    }

    static void forget(UUID id) {
        risingTicks.remove(id);
    }

    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide())
            return;
        if (event.getEffectInstance().getEffect() != MobEffects.LEVITATION)
            return;
        if (player instanceof ServerPlayer serverPlayer && GravatorDisruptorManager.isAffected(serverPlayer))
            event.setResult(Event.Result.DENY);
    }
}
