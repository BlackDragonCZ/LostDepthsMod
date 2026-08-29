package cz.blackdragoncz.lostdepths.ability;

import net.minecraft.world.item.ItemStack;

import java.util.List;

/** Implemented by an Item whose stacks grant abilities while carried. */
public interface SpecialAbilityProvider {

    List<SpecialAbility> getAbilities(ItemStack stack);
}
