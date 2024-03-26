package cz.blackdragoncz.lostdepths.client;

import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class FlyArmorProcedure {
    @SubscribeEvent
    public static void onEquipChange(LivingEquipmentChangeEvent event) {
        if (!(event.getSlot() == EquipmentSlot.FEET || event.getSlot() == EquipmentSlot.LEGS || event.getSlot() == EquipmentSlot.CHEST)) {
            return;
        }

        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.gameMode.getGameModeForPlayer() == GameType.CREATIVE) {
                if (!player.getAbilities().mayfly) {
                    player.getAbilities().mayfly = true;
                    player.onUpdateAbilities();
                }
                return;
            }
        }

        if (event.getEntity() instanceof Player player) {
            ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);

            if (
                    feet.getItem() == LostdepthsModItems.CELESTIAL_ARMOR_BOOTS.get() &&
                            legs.getItem() == LostdepthsModItems.CELESTIAL_ARMOR_LEGGINGS.get() &&
                            chest.getItem() == LostdepthsModItems.CELESTIAL_ARMOR_CHESTPLATE.get()
            ) {
                player.getAbilities().mayfly = true;
                player.onUpdateAbilities();
            } else if (
                    feet.getItem() == LostdepthsModItems.STAR_ARMOR_BOOTS.get() &&
                            legs.getItem() == LostdepthsModItems.STAR_ARMOR_LEGGINGS.get() &&
                            chest.getItem() == LostdepthsModItems.STAR_ARMOR_CHESTPLATE.get()
            ) {
                player.getAbilities().mayfly = true;
                player.onUpdateAbilities();
            } else {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
            }
        }
    }
}
