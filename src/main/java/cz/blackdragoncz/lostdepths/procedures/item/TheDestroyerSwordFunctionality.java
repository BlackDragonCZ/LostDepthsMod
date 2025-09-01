package cz.blackdragoncz.lostdepths.procedures.item;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.init.LostdepthsModDamageTypes;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.Optional;

public class TheDestroyerSwordFunctionality {

    private static final TagKey<EntityType<?>> MUTATED_TAG = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(LostdepthsMod.MODID, "mutated"));

    public static void execute(Entity entity, Entity sourceEntity) {
        if (entity == null || sourceEntity == null) return;
        if (!(entity instanceof LivingEntity target)) return;

        ItemStack held = (sourceEntity instanceof LivingEntity living) ? living.getMainHandItem() : ItemStack.EMPTY;
        if (held.isEmpty() || held.getItem() != LostdepthsModItems.THE_DESTROYER.get()) return;

        boolean isMutated = target.getType().is(MUTATED_TAG);
        double fraction = isMutated ? 0.6D : 0.8D;

        float amount = computeTrueDamage(target, fraction);
        if (amount <= 0f) return;

        DamageSource source = createDestroyerDamageSource(target, sourceEntity);

        target.hurt(source, amount);
    }

    private static float computeTrueDamage(LivingEntity target, double fraction) {
        float maxHP = Math.max(1.0f, target.getMaxHealth());
        double damage = Math.max(1.0D, Math.min(10000.0D, maxHP * fraction));
        return (float) damage;
    }

    private static DamageSource createDestroyerDamageSource(LivingEntity target, Entity attacker) {
        var access = target.level().registryAccess();
        var damageTypeRegistry = access.registryOrThrow(Registries.DAMAGE_TYPE);

        var holder = Optional.ofNullable(damageTypeRegistry.getHolder(LostdepthsModDamageTypes.TRUE_DAMAGE).orElse(null))
                .orElseGet(() -> damageTypeRegistry.getHolderOrThrow(DamageTypes.GENERIC));
        return new DamageSource(holder, attacker) {
            @Override
            public Component getLocalizedDeathMessage(LivingEntity victim) {
                String key = "death.attack.destroyer";
                if (this.getEntity() == null && this.getDirectEntity() == null) {
                    return victim.getKillCredit() != null ? Component.translatable(key + ".player", victim.getDisplayName(), victim.getKillCredit().getDisplayName())
                            : Component.translatable(key, victim.getDisplayName());
                } else {
                    Component killerName = (this.getEntity() == null ? this.getDirectEntity() : this.getEntity()).getDisplayName();
                    ItemStack item = ItemStack.EMPTY;
                    if (this.getEntity() instanceof LivingEntity living) item = living.getMainHandItem();
                    return (!item.isEmpty() && item.hasCustomHoverName())
                            ? Component.translatable(key + ".item", victim.getDisplayName(), killerName, item.getDisplayName())
                            : Component.translatable(key, victim.getDisplayName(), killerName);
                }
            }
        };
    }
}
