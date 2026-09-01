package cz.blackdragoncz.lostdepths.ability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

// Passive ability granted by a carried item. Singletons registered in init/LostdepthsModAbilities.
public abstract class SpecialAbility {

    private final ResourceLocation id;

    protected SpecialAbility(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation id() {
        return id;
    }

    // Server side. stack is the carried stack this came from - its NBT is the ability's state.
    // Return true to negate the hit entirely: no damage, no knockback, no hurt animation.
    public boolean onIncomingAttack(Player target, LivingEntity attacker, ItemStack stack, DamageSource source, float amount) {
        return false;
    }

    // Server side, on death. Return true to cancel it - the ability must then leave the player alive.
    // Nothing implements this: the Soulbinder's revive was cut because it fired on every death source.
    public boolean onLethalDamage(Player target, ItemStack stack, DamageSource source) {
        return false;
    }
}
