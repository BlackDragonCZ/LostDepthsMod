package cz.blackdragoncz.lostdepths.ability;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** Totem of Undying, except the item survives and works from anywhere in the inventory. */
public class SoulRevivalAbility extends SpecialAbility {

    public SoulRevivalAbility(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean onLethalDamage(Player target, ItemStack stack, DamageSource source) {
        // Same escape hatch vanilla gives itself - /kill and the void still work.
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return false;
        if (!SoulBinding.isOwner(stack, target))
            return false;

        target.setHealth(1.0F);
        target.removeAllEffects();
        target.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        target.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        target.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
        target.level().broadcastEntityEvent(target, (byte) 35);
        target.level().playSound(null, target.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);

        if (target instanceof ServerPlayer serverPlayer) {
            serverPlayer.awardStat(net.minecraft.stats.Stats.ITEM_USED.get(stack.getItem()));
            CriteriaTriggers.USED_TOTEM.trigger(serverPlayer, stack);
        }
        return true;
    }
}
