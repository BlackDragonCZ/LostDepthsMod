package cz.blackdragoncz.lostdepths.ability;

import cz.blackdragoncz.lostdepths.init.LostdepthsModDamageTypes;
import cz.blackdragoncz.lostdepths.item.armor.SpectrosArmorItem;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// "Normal hits" = anything not dealt by Lost Depths gear; void and /kill still work.
// Not a SpecialAbility: that dispatches per stack, which would apply the set bonus four times.
@Mod.EventBusSubscriber
public class SpectrosArmorEffects {

	private static final float REDUCTION_PER_PIECE = 0.02F;
	private static final float FULL_SET_REDUCTION = 0.07F;
	private static final int FULL_SET = 4;

	// LivingAttackEvent so a negated hit costs the attacker knockback and animation too.
	@SubscribeEvent
	public static void onAttack(LivingAttackEvent event) {
		if (!(event.getEntity() instanceof Player player) || player.level().isClientSide())
			return;
		if (equippedPieces(player) == 0)
			return;
		if (pierces(event.getSource()))
			return;
		event.setCanceled(true);
	}

	// Only LivingHurtEvent carries the amount; only damage past the immunity above reaches here.
	@SubscribeEvent
	public static void onHurt(LivingHurtEvent event) {
		if (!(event.getEntity() instanceof Player player) || player.level().isClientSide())
			return;
		int pieces = equippedPieces(player);
		if (pieces == 0)
			return;

		float fraction = REDUCTION_PER_PIECE * pieces + (pieces >= FULL_SET ? FULL_SET_REDUCTION : 0F);
		event.setAmount(Math.max(0F, event.getAmount() - player.getMaxHealth() * fraction));
	}

	private static boolean pierces(DamageSource source) {
		if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
			return true;
		if (source.is(LostdepthsModDamageTypes.TRUE_DAMAGE))
			return true;
		// A mirror shield throws your own hit back: Lost Depths gear dealt it, so the armour does not shrug it off.
		if (DamageOrigin.isReflected(source))
			return true;
		return DamageOrigin.isLostdepthsWeapon(source);
	}

	private static int equippedPieces(Player player) {
		int count = 0;
		for (ItemStack stack : player.getInventory().armor)
			if (stack.getItem() instanceof SpectrosArmorItem)
				count++;
		return count;
	}
}
