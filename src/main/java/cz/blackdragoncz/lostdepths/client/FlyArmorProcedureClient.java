package cz.blackdragoncz.lostdepths.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class FlyArmorProcedureClient {

    @SubscribeEvent
    public static void onEquipChange(LivingEquipmentChangeEvent event) {
        FlyArmorProcedure.onEquipChange(event);
    }
}
