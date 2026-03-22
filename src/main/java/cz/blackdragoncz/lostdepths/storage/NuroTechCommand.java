package cz.blackdragoncz.lostdepths.storage;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import cz.blackdragoncz.lostdepths.storage.data.CrystalInventory;
import cz.blackdragoncz.lostdepths.storage.data.CrystalStorageData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

import java.util.Set;
import java.util.UUID;

public class NuroTechCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("ld_nurotech")
				.requires(source -> source.hasPermission(2))

				// /ld_nurotech admin purge <uuid>
				.then(Commands.literal("admin")
						.then(Commands.literal("purge")
								.then(Commands.argument("uuid", StringArgumentType.string())
										.executes(ctx -> {
											String uuid = StringArgumentType.getString(ctx, "uuid");
											return purge(ctx.getSource(), uuid);
										})
								)
						)
						// /ld_nurotech admin list networks
						.then(Commands.literal("list")
								.then(Commands.literal("networks")
										.executes(ctx -> listNetworks(ctx.getSource()))
								)
								// /ld_nurotech admin list crystals
								.then(Commands.literal("crystals")
										.executes(ctx -> listCrystals(ctx.getSource()))
								)
								// /ld_nurotech admin list orphans
								.then(Commands.literal("orphans")
										.executes(ctx -> listOrphans(ctx.getSource()))
								)
						)
						// /ld_nurotech admin purge-orphans
						.then(Commands.literal("purge-orphans")
								.executes(ctx -> purgeOrphans(ctx.getSource()))
						)
				)

				// /ld_nurotech status
				.then(Commands.literal("status")
						.executes(ctx -> status(ctx.getSource()))
				)

				// /ld_nurotech perf [on|off|reset]
				.then(Commands.literal("perf")
						.executes(ctx -> perfStatus(ctx.getSource()))
						.then(Commands.literal("on")
								.executes(ctx -> perfToggle(ctx.getSource(), true))
						)
						.then(Commands.literal("off")
								.executes(ctx -> perfToggle(ctx.getSource(), false))
						)
						.then(Commands.literal("reset")
								.executes(ctx -> perfReset(ctx.getSource()))
						)
				)
		);
	}

	private static int purge(CommandSourceStack source, String uuidStr) {
		ServerLevel level = source.getLevel();
		CrystalStorageData data = CrystalStorageData.get(level);

		UUID id;
		try {
			id = UUID.fromString(uuidStr);
		} catch (IllegalArgumentException e) {
			source.sendFailure(Component.literal("§c[NuroTech] Invalid UUID: " + uuidStr));
			return 0;
		}

		if (data.removeCrystal(id)) {
			source.sendSuccess(() -> Component.literal("§c[NuroTech] Purged crystal: " + uuidStr), true);
		} else {
			source.sendFailure(Component.literal("§c[NuroTech] No crystal found with UUID: " + uuidStr));
			return 0;
		}
		return 1;
	}

	private static int listNetworks(CommandSourceStack source) {
		// TODO: List all active NT networks with controller positions and stats
		source.sendSuccess(() -> Component.literal("§b[NuroTech] Network listing not yet implemented."), false);
		return 1;
	}

	private static int listCrystals(CommandSourceStack source) {
		ServerLevel level = source.getLevel();
		CrystalStorageData data = CrystalStorageData.get(level);

		Set<UUID> ids = data.getAllCrystalIds();
		if (ids.isEmpty()) {
			source.sendSuccess(() -> Component.literal("§b[NuroTech] No crystals registered."), false);
			return 1;
		}

		source.sendSuccess(() -> Component.literal("§b[NuroTech] Registered crystals (" + ids.size() + "):"), false);
		for (UUID id : ids) {
			CrystalInventory inv = data.getCrystal(id);
			if (inv != null) {
				String info = "§7  " + id + " §f[" + inv.getType().getDisplayName() + "] §7"
						+ inv.getTotalCount() + "/" + inv.getType().getCapacity()
						+ " (" + inv.getTypeCount() + "/" + inv.getType().getTypeLimit() + " types)";
				source.sendSuccess(() -> Component.literal(info), false);
			}
		}
		return 1;
	}

	private static int listOrphans(CommandSourceStack source) {
		// TODO: Scan all loaded block entities and player inventories for crystal items,
		// compare against SavedData entries to find orphans
		source.sendSuccess(() -> Component.literal("§b[NuroTech] Orphan detection not yet implemented."), false);
		return 1;
	}

	private static int purgeOrphans(CommandSourceStack source) {
		// TODO: Remove all orphaned crystal entries from SavedData
		source.sendSuccess(() -> Component.literal("§b[NuroTech] Orphan purge not yet implemented."), false);
		return 1;
	}

	private static int status(CommandSourceStack source) {
		ServerLevel level = source.getLevel();
		CrystalStorageData data = CrystalStorageData.get(level);

		int crystalCount = data.getCrystalCount();
		int totalItems = data.getTotalStoredItems();

		source.sendSuccess(() -> Component.literal("§b[NuroTech] Storage system status:"), false);
		source.sendSuccess(() -> Component.literal("§7  Networks: §fnot yet tracked"), false);
		source.sendSuccess(() -> Component.literal("§7  Crystals: §f" + crystalCount), false);
		source.sendSuccess(() -> Component.literal("§7  Total stored: §f" + totalItems + " items"), false);
		return 1;
	}

	private static int perfToggle(CommandSourceStack source, boolean enable) {
		NTPerformanceTracker.setEnabled(enable);
		source.sendSuccess(() -> Component.literal("§b[NuroTech] Performance tracking " + (enable ? "§aENABLED" : "§cDISABLED")), true);
		return 1;
	}

	private static int perfReset(CommandSourceStack source) {
		NTPerformanceTracker.reset();
		source.sendSuccess(() -> Component.literal("§b[NuroTech] Performance data reset."), true);
		return 1;
	}

	private static int perfStatus(CommandSourceStack source) {
		if (!NTPerformanceTracker.isEnabled()) {
			source.sendSuccess(() -> Component.literal("§7[NuroTech] Performance tracking is disabled. Use §f/ld_nurotech perf on§7 to enable."), false);
			return 1;
		}
		int samples = NTPerformanceTracker.getSampleCount();
		double avgUs = NTPerformanceTracker.getAverageTickUs();
		double peakUs = NTPerformanceTracker.getPeakTickUs();
		double pct = NTPerformanceTracker.getTickPercentage();

		String avgColor = avgUs < 1000 ? "§a" : avgUs < 5000 ? "§e" : "§c";
		String pctColor = pct < 2 ? "§a" : pct < 10 ? "§e" : "§c";

		source.sendSuccess(() -> Component.literal("§b[NuroTech] Performance (" + samples + " tick samples):"), false);
		source.sendSuccess(() -> Component.literal("§7  Avg: " + avgColor + String.format("%.1f", avgUs) + " µs/tick"), false);
		source.sendSuccess(() -> Component.literal("§7  Peak: §f" + String.format("%.1f", peakUs) + " µs/tick"), false);
		source.sendSuccess(() -> Component.literal("§7  Tick budget: " + pctColor + String.format("%.2f", pct) + "%%§7 of 50ms"), false);
		return 1;
	}
}
