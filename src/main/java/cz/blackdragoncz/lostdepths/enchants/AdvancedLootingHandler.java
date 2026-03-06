package cz.blackdragoncz.lostdepths.enchants;

import cz.blackdragoncz.lostdepths.init.LostdepthsModEnchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AdvancedLootingHandler {

    @SubscribeEvent
    public static void onLootingLevel(LootingLevelEvent event) {
        if (event.getDamageSource() == null) return;
        if (!(event.getDamageSource().getEntity() instanceof LivingEntity killer)) return;

        int afLevel = EnchantmentHelper.getEnchantmentLevel(
                LostdepthsModEnchantments.ADVANCED_LOOTING.get(), killer);
        if (afLevel > 0) {
            event.setLootingLevel(afLevel + 3);
        }
    }
}
