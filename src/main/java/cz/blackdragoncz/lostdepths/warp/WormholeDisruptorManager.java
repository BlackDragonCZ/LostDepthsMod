package cz.blackdragoncz.lostdepths.warp;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.network.WarpDisruptionSyncMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

/**
 * Server-side manager tracking which players are inside wormhole disruption zones.
 * Players are added by WormholeDisruptorBlockEntity ticks and expire after a short duration.
 */
public class WormholeDisruptorManager {

    private static final int EXPIRY_TICKS = 15; // Slightly longer than the 10-tick check interval

    private static final Map<UUID, Long> affectedPlayers = new HashMap<>();
    private static long currentTick = 0;

    /**
     * Called from block entity tick when a player is within range.
     */
    public static void addAffectedPlayer(ServerPlayer player, BlockPos disruptorPos) {
        UUID uuid = player.getUUID();
        boolean wasAffected = isAffected(uuid);
        affectedPlayers.put(uuid, currentTick + EXPIRY_TICKS);

        if (!wasAffected) {
            // Player just entered — sync to client
            LostdepthsMod.PACKET_HANDLER.sendTo(
                    new WarpDisruptionSyncMessage(true),
                    player.connection.connection,
                    net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT
            );
        }
    }

    /**
     * Called when a disruptor block is removed — doesn't immediately clear players,
     * they'll expire naturally on the next tick cycle.
     */
    public static void removeDisruptor(BlockPos pos) {
        // Players will expire naturally via tick expiry
    }

    /**
     * Check if a player UUID is currently in a disruption zone.
     */
    public static boolean isAffected(UUID playerUuid) {
        Long expiry = affectedPlayers.get(playerUuid);
        return expiry != null && currentTick < expiry;
    }

    /**
     * Check if a player entity is currently in a disruption zone.
     */
    public static boolean isAffected(ServerPlayer player) {
        return isAffected(player.getUUID());
    }

    /**
     * Called every server tick to update expiry and sync to clients.
     */
    public static void tick(List<ServerPlayer> players) {
        currentTick++;

        Iterator<Map.Entry<UUID, Long>> it = affectedPlayers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, Long> entry = it.next();
            if (currentTick >= entry.getValue()) {
                UUID uuid = entry.getKey();
                it.remove();

                // Find the player and sync removal to client
                for (ServerPlayer player : players) {
                    if (player.getUUID().equals(uuid)) {
                        LostdepthsMod.PACKET_HANDLER.sendTo(
                                new WarpDisruptionSyncMessage(false),
                                player.connection.connection,
                                net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT
                        );
                        break;
                    }
                }
            }
        }
    }

    /**
     * Clear all tracking data (called on server stop).
     */
    public static void clear() {
        affectedPlayers.clear();
        currentTick = 0;
    }
}
