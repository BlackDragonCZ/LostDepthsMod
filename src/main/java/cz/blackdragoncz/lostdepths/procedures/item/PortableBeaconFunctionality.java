package cz.blackdragoncz.lostdepths.procedures.item;

import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public class PortableBeaconFunctionality {

    /** How far to affect entities (matches your original 0..35 selector). */
    private static final double RADIUS = 35.0D;

    private static final int DURATION_TICKS = 10 * 20;

    /**
     * Refresh threshold in ticks. We only refresh an existing effect when its
     * remaining duration is below this (prevents reapplying every single tick).
     */
    private static final int REFRESH_BELOW_TICKS = 40;

    /**
     * Brewable, positive vanilla potion effects.
     * Keys are full effect IDs as strings so they match what's stored in "potionListA".
     * Values are the amplifier to use (same defaults you used: mostly 1; regen was 0).
     * Note:
     * - We intentionally skip effects like Turtle Master (mixed positive/negative),
     *   Luck (not brewable), Haste (not brewable in vanilla), etc.
     * - If you want to allow more effects, just add new entries here and ensure
     *   your NBT "potionListA" includes their IDs.
     */
    private static final Map<String, EffectSpec> ALLOWED_EFFECTS = Map.of(
                    "minecraft:speed",           new EffectSpec(MobEffects.MOVEMENT_SPEED, 1),
                    "minecraft:fire_resistance", new EffectSpec(MobEffects.FIRE_RESISTANCE, 1),
                    "minecraft:invisibility",    new EffectSpec(MobEffects.INVISIBILITY, 1),
                    "minecraft:jump_boost",      new EffectSpec(MobEffects.JUMP, 1),
                    "minecraft:night_vision",    new EffectSpec(MobEffects.NIGHT_VISION, 1),
                    "minecraft:regeneration",    new EffectSpec(MobEffects.REGENERATION, 0),
                    "minecraft:slow_falling",    new EffectSpec(MobEffects.SLOW_FALLING, 1),
                    "minecraft:strength",        new EffectSpec(MobEffects.DAMAGE_BOOST, 1),
                    "minecraft:water_breathing", new EffectSpec(MobEffects.WATER_BREATHING, 1),
                    "minecraft:glowing",         new EffectSpec(MobEffects.GLOWING, 1)
            )
            .entrySet().stream()
            .filter(event -> !event.getKey().isEmpty())
            .collect(java.util.stream.Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

    public static void execute(Entity entity, ItemStack itemstack) {
        if (entity == null) return;

        // Must be holding the Portable Beacon in OFFHAND like your original logic.
        if (!isHoldingPortableBeaconOffhand(entity)) return;

        // Server-only logic (effects should be applied on the logical server).
        Level level = entity.level();
        if (level.isClientSide) return;

        // Read the allowed-effect list from the item once.
        final String allowedList = itemstack.getOrCreateTag().getString("potionListA");
        if (allowedList.isEmpty()) return;

        // Collect nearby living entities within RADIUS (including self).
        Vec3 center = entity.position();
        AABB box = new AABB(
                center.x - RADIUS, center.y - RADIUS, center.z - RADIUS,
                center.x + RADIUS, center.y + RADIUS, center.z + RADIUS
        );

        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, box, e -> true);
        if (targets.isEmpty()) return;

        // For each allowed effect present in the item's list, apply to all targets.
        for (Map.Entry<String, EffectSpec> entry : ALLOWED_EFFECTS.entrySet()) {
            String effectId = entry.getKey();
            if (!allowedList.contains(effectId)) continue; // Not enabled by the item.

            EffectSpec spec = entry.getValue();
            applyEffectToTargets(targets, spec.effect, spec.amplifier);
        }
    }

    private static boolean isHoldingPortableBeaconOffhand(Entity entity) {
        if (!(entity instanceof LivingEntity liv)) return false;
        ItemStack off = liv.getOffhandItem();
        return !off.isEmpty() && off.getItem() == LostdepthsModItems.PORTABLE_BEACON.get();
    }

    private static void applyEffectToTargets(List<LivingEntity> targets, MobEffect effect, int amplifier) {
        for (LivingEntity target : targets) {
            MobEffectInstance existing = target.getEffect(effect);

            // Only (re)apply if missing or about to expire — reduces ticking overhead.
            if (existing == null || existing.getDuration() < REFRESH_BELOW_TICKS || existing.getAmplifier() < amplifier) {
                // ambient=true (beacon-like), visible=false (hide particles), showIcon=true (default)
                target.addEffect(new MobEffectInstance(effect, DURATION_TICKS, amplifier, true, false));
            }
        }
    }

    private record EffectSpec(MobEffect effect, int amplifier) {}
}
