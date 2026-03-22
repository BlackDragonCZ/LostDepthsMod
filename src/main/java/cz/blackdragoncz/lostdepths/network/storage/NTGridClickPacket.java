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
 * Client → Server: Player clicked a virtual grid slot in the terminal.
 * Triggers item extraction from the network.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NTGridClickPacket {
	private final int gridIndex;
	private final int button;

	public NTGridClickPacket(int gridIndex, int button) {
		this.gridIndex = gridIndex;
		this.button = button;
	}

	public NTGridClickPacket(FriendlyByteBuf buf) {
		this.gridIndex = buf.readInt();
		this.button = buf.readInt();
	}

	public static void encode(NTGridClickPacket msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.gridIndex);
		buf.writeInt(msg.button);
	}

	public static void handle(NTGridClickPacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			if (player != null) {
				if (player.containerMenu instanceof NTTerminalMenu menu) {
					menu.handleGridClick(msg.gridIndex, msg.button);
				} else if (player.containerMenu instanceof NTCraftingTerminalMenu menu) {
					menu.handleGridClick(msg.gridIndex, msg.button);
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}

	@SubscribeEvent
	public static void register(FMLCommonSetupEvent event) {
		LostdepthsMod.addNetworkMessage(NTGridClickPacket.class, NTGridClickPacket::encode, NTGridClickPacket::new, NTGridClickPacket::handle);
	}
}
