package cz.blackdragoncz.lostdepths.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.DEDICATED_SERVER})
public class SecurityClearanceSystem {

    public static final int CLEARANCE_DURATION = 600;

    public static class PlayerClearanceData {
        public int Clearance;
        public int Duration;
    }

    private static Map<ServerPlayer, PlayerClearanceData> playerClearanceData = new HashMap<>();

    public static void giveClearance(ServerPlayer player, int clearance) {
        if (playerClearanceData.containsKey(player)) {
            PlayerClearanceData data = playerClearanceData.get(player);
            data.Clearance = clearance;
            data.Duration = CLEARANCE_DURATION;
        } else {
            PlayerClearanceData data = new PlayerClearanceData();
            data.Clearance = clearance;
            data.Duration = CLEARANCE_DURATION;
            playerClearanceData.put(player, data);
        }
    }

    public static boolean haveClearance(ServerPlayer player, int clearance) {
        if (!playerClearanceData.containsKey(player)) {
            return false;
        }

        PlayerClearanceData data = playerClearanceData.get(player);

        return data.Clearance <= clearance && data.Duration > 0;
    }

    public static void update()
    {
        Iterator<Map.Entry<ServerPlayer, PlayerClearanceData>> it = playerClearanceData.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<ServerPlayer, PlayerClearanceData> entry = it.next();
            PlayerClearanceData data = entry.getValue();

            data.Duration--;

            if (data.Duration <= 0) {
                it.remove();
            }
        }
    }

}
