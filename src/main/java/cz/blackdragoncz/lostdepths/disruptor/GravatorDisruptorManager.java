package cz.blackdragoncz.lostdepths.disruptor;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.block.creative.entity.GravatorDisruptorBlockEntity.Mode;
import cz.blackdragoncz.lostdepths.network.GravatorDisruptorSyncMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;

import java.util.*;

// A restriction, not a look: overlapping zones stack rather than picking a winner, and the strictest mode present wins.
public final class GravatorDisruptorManager {

    private static final int EXPIRY_TICKS = 12;

    private record Offer(long expiry, Mode mode) {}

    private static final Map<UUID, Map<BlockPos, Offer>> offers = new HashMap<>();
    private static final Map<UUID, Mode> resolved = new HashMap<>();
    private static final Set<UUID> lastSent = new HashSet<>();
    private static long currentTick = 0;

    private GravatorDisruptorManager() {
    }

    public static void offer(ServerPlayer player, BlockPos pos, Mode mode) {
        offers.computeIfAbsent(player.getUUID(), k -> new HashMap<>())
                .put(pos, new Offer(currentTick + EXPIRY_TICKS, mode));
    }

    public static void removeDisruptor(BlockPos pos) {
        for (Map<BlockPos, Offer> perPlayer : offers.values())
            perPlayer.remove(pos);
    }

    public static boolean isAffected(ServerPlayer player) {
        return resolved.containsKey(player.getUUID());
    }

    public static boolean isStrict(ServerPlayer player) {
        return resolved.get(player.getUUID()) == Mode.STRICT;
    }

    public static void tick(List<ServerPlayer> players) {
        currentTick++;

        for (ServerPlayer player : players) {
            UUID id = player.getUUID();
            Map<BlockPos, Offer> perPlayer = offers.get(id);

            Mode best = null;
            if (perPlayer != null) {
                perPlayer.values().removeIf(o -> currentTick >= o.expiry());
                for (Offer o : perPlayer.values())
                    if (best == null || o.mode() == Mode.STRICT)
                        best = o.mode();
                if (perPlayer.isEmpty())
                    offers.remove(id);
            }

            if (best == null)
                resolved.remove(id);
            else
                resolved.put(id, best);

            boolean active = best != null;
            if (active == lastSent.contains(id))
                continue;
            if (active)
                lastSent.add(id);
            else
                lastSent.remove(id);
            LostdepthsMod.PACKET_HANDLER.sendTo(new GravatorDisruptorSyncMessage(active), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }

        Set<UUID> online = new HashSet<>();
        for (ServerPlayer player : players)
            online.add(player.getUUID());
        lastSent.retainAll(online);
        resolved.keySet().retainAll(online);
        offers.keySet().retainAll(online);
    }

    public static void clear() {
        offers.clear();
        resolved.clear();
        lastSent.clear();
        currentTick = 0;
    }
}
