package cz.blackdragoncz.lostdepths.procedures.item;

import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DraconicTridentProcedure {

    @SubscribeEvent
    public static void onPlayerDamage(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getSource().getEntity() != null) {
                return;
            }

            if (!player.isInLava())
                return;

            ItemStack mainHandStack = player.getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack offHandStack = player.getItemInHand(InteractionHand.OFF_HAND);

            if (mainHandStack.getItem() == LostdepthsModItems.DRACONIC_TRIDENT.get() || offHandStack.getItem() == LostdepthsModItems.DRACONIC_TRIDENT.get()) {
                event.setCanceled(true);
                player.clearFire();
            }

            if (mainHandStack.getItem() == LostdepthsModItems.PRIME_DRACONIC_TRIDENT.get() || offHandStack.getItem() == LostdepthsModItems.PRIME_DRACONIC_TRIDENT.get()) {
                event.setCanceled(true);
                player.clearFire();
            }
        }
    }

}
