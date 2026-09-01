package cz.blackdragoncz.lostdepths.procedures;

import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;

// Critical hit with a Venom Knife or Blade of the Forgotten -> chance of Volatile Blood.
// Was named OnEntityDeathProcedure and read the victim's hand instead of the attacker's.
@Mod.EventBusSubscriber
public class VenomWeaponCriticalHitProcedure {

	@SubscribeEvent
	public static void onCriticalHit(CriticalHitEvent event) {
		// ForgeHooks.getCriticalHit posts this on every melee swing and only then decides whether it
		// counted, so without this check the reward fired on ordinary hits too.
		if (!event.isVanillaCritical())
			return;
		execute(event.getEntity());
	}

	public static void execute(Entity entity) {
		if (!(entity instanceof Player player) || player.level().isClientSide())
			return;

		Item weapon = player.getMainHandItem().getItem();
		if (weapon != LostdepthsModItems.VENOM_KNIFE.get() && weapon != LostdepthsModItems.BLADE_OF_FORGOTTEN.get())
			return;
		if (Mth.nextInt(player.getRandom(), 0, 10) < 5)
			return;

		ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(LostdepthsModItems.VOLATILE_BLOOD.get()));
	}
}
