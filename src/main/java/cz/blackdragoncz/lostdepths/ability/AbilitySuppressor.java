package cz.blackdragoncz.lostdepths.ability;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Implemented by an Item that lets its holder punch through an ability - the planned shield that
 * negates the soul totem's protection. Checked on the attacker's two hands only; nothing implements
 * this yet.
 */
public interface AbilitySuppressor {

    boolean suppresses(SpecialAbility ability, ItemStack stack, LivingEntity holder, Player target);
}
