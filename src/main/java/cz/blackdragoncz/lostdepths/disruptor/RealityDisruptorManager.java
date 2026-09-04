package cz.blackdragoncz.lostdepths.disruptor;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.block.creative.entity.RealityDisruptorBlockEntity.Camera;
import cz.blackdragoncz.lostdepths.block.creative.entity.RealityDisruptorBlockEntity.Debug;
import cz.blackdragoncz.lostdepths.network.RealityDisruptorSyncMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;

import java.util.*;

// Offers expire on their own, so a block being broken, unloaded or reconfigured needs no bookkeeping.
// Overlapping zones resolve by nearest block, which is the only rule that reads sensibly when a player walks between rooms.
public final class RealityDisruptorManager {

    private static final int EXPIRY_TICKS = 12; // a little over the 5-tick offer interval

    private record Offer(long expiry, double distSq, boolean hideGui, Debug debug, Camera camera, String shader) {}

    private static final Map<UUID, Map<BlockPos, Offer>> offers = new HashMap<>();
    private static final Map<UUID, RealityDisruptorSyncMessage> lastSent = new HashMap<>();
    private static long currentTick = 0;

    private RealityDisruptorManager() {
    }

    public static void offer(ServerPlayer player, BlockPos pos, double distSq, boolean hideGui, Debug debug, Camera camera, String shader) {
        offers.computeIfAbsent(player.getUUID(), k -> new HashMap<>())
                .put(pos, new Offer(currentTick + EXPIRY_TICKS, distSq, hideGui, debug, camera, shader));
    }

    public static void removeDisruptor(BlockPos pos) {
        for (Map<BlockPos, Offer> perPlayer : offers.values())
            perPlayer.remove(pos);
    }

    public static void tick(List<ServerPlayer> players) {
        currentTick++;

        for (ServerPlayer player : players) {
            UUID id = player.getUUID();
            Map<BlockPos, Offer> perPlayer = offers.get(id);

            Offer best = null;
            if (perPlayer != null) {
                perPlayer.values().removeIf(o -> currentTick >= o.expiry());
                for (Offer o : perPlayer.values())
                    if (best == null || o.distSq() < best.distSq())
                        best = o;
                if (perPlayer.isEmpty())
                    offers.remove(id);
            }

            RealityDisruptorSyncMessage state = best == null
                    ? RealityDisruptorSyncMessage.inactive()
                    : new RealityDisruptorSyncMessage(true, best.hideGui(), best.debug(), best.camera(), best.shader());

            if (state.equals(lastSent.get(id)))
                continue;
            lastSent.put(id, state);
            LostdepthsMod.PACKET_HANDLER.sendTo(state, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }

        // Drop bookkeeping for anyone who logged out, or they keep a stale state on rejoin.
        Set<UUID> online = new HashSet<>();
        for (ServerPlayer player : players)
            online.add(player.getUUID());
        lastSent.keySet().retainAll(online);
        offers.keySet().retainAll(online);
    }

    public static void clear() {
        offers.clear();
        lastSent.clear();
        currentTick = 0;
    }
}
