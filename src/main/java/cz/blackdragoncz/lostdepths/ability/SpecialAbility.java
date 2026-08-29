package cz.blackdragoncz.lostdepths.ability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Base class for a passive ability granted by an item a player is carrying. Items expose theirs via
 * {@link SpecialAbilityProvider}; {@link AbilityEvents} runs them. Instances are singletons registered
 * in {@code init/LostdepthsModAbilities}.
 */
public abstract class SpecialAbility {

    private final ResourceLocation id;

    protected SpecialAbility(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation id() {
        return id;
    }

    /**
     * Server side only, called once per ability-carrying stack in the target's inventory.
     *
     * @param stack the carried stack this ability came from - its NBT is the ability's state
     * @return true to negate the hit entirely (no damage, no knockback, no hurt animation)
     */
    public boolean onIncomingAttack(Player target, LivingEntity attacker, ItemStack stack, DamageSource source, float amount) {
        return false;
    }

    /**
     * Server side only, called on a carried stack when the player would die.
     *
     * @return true to cancel the death - the ability is responsible for leaving the player alive
     */
    public boolean onLethalDamage(Player target, ItemStack stack, DamageSource source) {
        return false;
    }
}
