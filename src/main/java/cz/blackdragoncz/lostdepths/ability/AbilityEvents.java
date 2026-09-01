package cz.blackdragoncz.lostdepths.ability;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// LivingAttackEvent, not LivingHurtEvent: cancelling here costs the attacker damage, knockback and the
// hurt animation, instead of landing a hit for zero.
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AbilityEvents {

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (!(event.getEntity() instanceof Player target) || target.level().isClientSide())
            return;
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker))
            return;

        // Anywhere in the inventory counts - main slots, armour and offhand.
        Inventory inventory = target.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!(stack.getItem() instanceof SpecialAbilityProvider provider))
                continue;

            for (SpecialAbility ability : provider.getAbilities(stack)) {
                if (isSuppressed(ability, stack, target, attacker))
                    continue;
                if (ability.onIncomingAttack(target, attacker, stack, event.getSource(), event.getAmount())) {
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player target) || target.level().isClientSide())
            return;

        Inventory inventory = target.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!(stack.getItem() instanceof SpecialAbilityProvider provider))
                continue;

            for (SpecialAbility ability : provider.getAbilities(stack))
                if (ability.onLethalDamage(target, stack, event.getSource())) {
                    event.setCanceled(true);
                    return;
                }
        }
    }

    /** Only the attacker's hands are checked - the shield is meant to break through, not to defend. */
    private static boolean isSuppressed(SpecialAbility ability, ItemStack stack, Player target, LivingEntity attacker) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack held = attacker.getItemInHand(hand);
            if (held.getItem() instanceof AbilitySuppressor suppressor && suppressor.suppresses(ability, held, attacker, target))
                return true;
        }
        return false;
    }
}
