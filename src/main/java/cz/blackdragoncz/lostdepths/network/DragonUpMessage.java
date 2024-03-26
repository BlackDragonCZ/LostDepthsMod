
package cz.blackdragoncz.lostdepths.network;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

import cz.blackdragoncz.lostdepths.procedures.DragonUpOnKeyReleasedProcedure;
import cz.blackdragoncz.lostdepths.procedures.DragonUpOnKeyPressedProcedure;
import cz.blackdragoncz.lostdepths.LostdepthsMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DragonUpMessage {
	int type, pressedms;

	public DragonUpMessage(int type, int pressedms) {
		this.type = type;
		this.pressedms = pressedms;
	}

	public DragonUpMessage(FriendlyByteBuf buffer) {
		this.type = buffer.readInt();
		this.pressedms = buffer.readInt();
	}

	public static void buffer(DragonUpMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.type);
		buffer.writeInt(message.pressedms);
	}

	public static void handler(DragonUpMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			pressAction(context.getSender(), message.type, message.pressedms);
		});
		context.setPacketHandled(true);
	}

	public static void pressAction(Player entity, int type, int pressedms) {
		Level world = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(entity.blockPosition()))
			return;
		if (type == 0) {

			DragonUpOnKeyPressedProcedure.execute(entity);
		}
		if (type == 1) {

			DragonUpOnKeyReleasedProcedure.execute(entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		LostdepthsMod.addNetworkMessage(DragonUpMessage.class, DragonUpMessage::buffer, DragonUpMessage::new, DragonUpMessage::handler);
	}
}
