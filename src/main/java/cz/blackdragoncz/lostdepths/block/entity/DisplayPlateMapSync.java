package cz.blackdragoncz.lostdepths.block.entity;

import com.google.common.collect.Lists;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// Map pixels never travel with chunk data: vanilla only syncs them from ServerEntity, hardcoded to ItemFrame. A display plate is a block, so it has to push the
// same packets itself or the map renders blank for anyone who never carried it. Event driven on purpose, so the plate stays a non-ticking block.
@Mod.EventBusSubscriber
public final class DisplayPlateMapSync {

	private DisplayPlateMapSync() {
	}

	@SubscribeEvent
	public static void onChunkWatch(ChunkWatchEvent.Watch event) {
		ServerPlayer player = event.getPlayer();
		LevelChunk chunk = event.getChunk();
		for (BlockEntity blockEntity : chunk.getBlockEntities().values())
			if (blockEntity instanceof InfusedDisplayPlateBlockEntity plate)
				send(player, plate.getHeldItem());
	}

	// Called when a plate's contents change, so players already watching the chunk get the new map.
	public static void onHeldItemChanged(Level level, InfusedDisplayPlateBlockEntity plate) {
		if (!(level instanceof ServerLevel serverLevel))
			return;
		ItemStack stack = plate.getHeldItem();
		if (!(stack.getItem() instanceof MapItem))
			return;
		for (ServerPlayer player : serverLevel.players())
			if (player.blockPosition().closerThan(plate.getBlockPos(), 128.0D))
				send(player, stack);
	}

	private static void send(ServerPlayer player, ItemStack stack) {
		if (!(stack.getItem() instanceof MapItem))
			return;
		Integer mapId = MapItem.getMapId(stack);
		if (mapId == null)
			return;
		MapItemSavedData data = MapItem.getSavedData(mapId, player.level());
		if (data == null)
			return;
		// Not tickCarriedBy + getUpdatePacket: that pair drops any player who neither holds the stack nor has it in a real ItemFrame
		// (MapItemSavedData:175 needs inventory.contains || stack.isFramed), so it returns null and the map stays blank for everyone
		// except whoever once carried it. Push the whole 128x128 instead. Clone because the array serializes later on the netty thread.
		player.connection.send(new ClientboundMapItemDataPacket(mapId, data.scale, data.locked,
				Lists.newArrayList(data.getDecorations()),
				new MapItemSavedData.MapPatch(0, 0, 128, 128, data.colors.clone())));
	}
}
