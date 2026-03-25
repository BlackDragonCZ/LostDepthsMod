package cz.blackdragoncz.lostdepths.network.storage;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.block.entity.storage.NTPatternEncoderBlockEntity;
import cz.blackdragoncz.lostdepths.world.inventory.NTPatternEncoderMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Client → Server: Toggle crafting/processing mode on Pattern Encoder.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NTPatternModeTogglePacket {

	public NTPatternModeTogglePacket() {}

	public NTPatternModeTogglePacket(FriendlyByteBuf buf) {}

	public static void encode(NTPatternModeTogglePacket msg, FriendlyByteBuf buf) {}

	public static void handle(NTPatternModeTogglePacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			if (player == null) return;
			if (!(player.containerMenu instanceof NTPatternEncoderMenu menu)) return;

			NTPatternEncoderBlockEntity encoder = menu.getEncoder();
			if (encoder != null) {
				encoder.setProcessingMode(!encoder.isProcessingMode());
			}
		});
		ctx.get().setPacketHandled(true);
	}

	@SubscribeEvent
	public static void register(FMLCommonSetupEvent event) {
		LostdepthsMod.addNetworkMessage(NTPatternModeTogglePacket.class, NTPatternModeTogglePacket::encode, NTPatternModeTogglePacket::new, NTPatternModeTogglePacket::handle);
	}
}
