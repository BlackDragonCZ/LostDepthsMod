package cz.blackdragoncz.lostdepths.warp;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LostdepthsMod.MODID)
public class WormholeDisruptorEvents {

    private static final Component DISRUPTED_MSG = Component.literal("\u00A7cWormhole disruption prevents teleportation!");

    @SubscribeEvent
    public static void onEnderPearl(EntityTeleportEvent.EnderPearl event) {
        if (event.getEntity() instanceof ServerPlayer player && WormholeDisruptorManager.isAffected(player)) {
            event.setCanceled(true);
            player.displayClientMessage(DISRUPTED_MSG, true);
        }
    }

    @SubscribeEvent
    public static void onChorusFruit(EntityTeleportEvent.ChorusFruit event) {
        if (event.getEntity() instanceof ServerPlayer player && WormholeDisruptorManager.isAffected(player)) {
            event.setCanceled(true);
            player.displayClientMessage(DISRUPTED_MSG, true);
        }
    }

    @SubscribeEvent
    public static void onEntityTeleport(EntityTeleportEvent event) {
        // Catch-all for modded teleportation (Mekanism portable teleporter, etc.)
        // EnderPearl and ChorusFruit are subclasses, so they fire here too but are already handled
        if (event instanceof EntityTeleportEvent.EnderPearl || event instanceof EntityTeleportEvent.ChorusFruit)
            return;
        if (event.getEntity() instanceof ServerPlayer player && WormholeDisruptorManager.isAffected(player)) {
            event.setCanceled(true);
            player.displayClientMessage(DISRUPTED_MSG, true);
        }
    }

    @SubscribeEvent
    public static void onCommand(CommandEvent event) {
        // Block /tp, /teleport, /warp, /tpa, /home and similar teleport commands
        String input = event.getParseResults().getReader().getString().toLowerCase();
        if (isTeleportCommand(input)) {
            var source = event.getParseResults().getContext().getSource();
            if (source.getEntity() instanceof ServerPlayer player && WormholeDisruptorManager.isAffected(player)) {
                event.setCanceled(true);
                player.displayClientMessage(DISRUPTED_MSG, true);
            }
        }
    }

    private static boolean isTeleportCommand(String input) {
        // Strip leading slash if present
        String cmd = input.startsWith("/") ? input.substring(1) : input;
        return cmd.startsWith("tp ") || cmd.startsWith("teleport ")
                || cmd.startsWith("warp") || cmd.startsWith("tpa")
                || cmd.startsWith("home") || cmd.startsWith("back")
                || cmd.startsWith("spawn") || cmd.startsWith("rtp");
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        WormholeRangeCommand.register(event.getDispatcher());
    }
}
