package cz.blackdragoncz.lostdepths.network.storage;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.world.inventory.NTCraftingTerminalMenu;
import cz.blackdragoncz.lostdepths.world.inventory.NTTerminalMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Client → Server: Player clicked empty grid area with a carried item.
 * Triggers item insertion into the network.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NTGridInsertPacket {
	private final int button;

	public NTGridInsertPacket(int button) {
		this.button = button;
	}

	public NTGridInsertPacket(FriendlyByteBuf buf) {
		this.button = buf.readInt();
	}

	public static void encode(NTGridInsertPacket msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.button);
	}

	public static void handle(NTGridInsertPacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			if (player != null) {
				if (player.containerMenu instanceof NTTerminalMenu menu) {
					menu.handleGridInsert(msg.button);
				} else if (player.containerMenu instanceof NTCraftingTerminalMenu menu) {
					menu.handleGridInsert(msg.button);
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}

	@SubscribeEvent
	public static void register(FMLCommonSetupEvent event) {
		LostdepthsMod.addNetworkMessage(NTGridInsertPacket.class, NTGridInsertPacket::encode, NTGridInsertPacket::new, NTGridInsertPacket::handle);
	}
}
