package cz.blackdragoncz.lostdepths.ability;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

// Lets its holder punch through an ability - the planned shield that negates the Soulbinder's
// protection. Checked on the attacker's two hands only; nothing implements this yet.
public interface AbilitySuppressor {

    boolean suppresses(SpecialAbility ability, ItemStack stack, LivingEntity holder, Player target);
}
