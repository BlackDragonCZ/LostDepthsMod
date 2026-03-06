package cz.blackdragoncz.lostdepths.network;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ContractSignMessage {
	private final InteractionHand hand;

	public ContractSignMessage(InteractionHand hand) {
		this.hand = hand;
	}

	public ContractSignMessage(FriendlyByteBuf buf) {
		this.hand = buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
	}

	public static void buffer(ContractSignMessage msg, FriendlyByteBuf buf) {
		buf.writeBoolean(msg.hand == InteractionHand.MAIN_HAND);
	}

	public static void handler(ContractSignMessage msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			if (player == null) return;
			ItemStack stack = player.getItemInHand(msg.hand);
			if (!stack.is(LostdepthsModItems.INFUSED_WRITTEN_BOOK.get())) return;

			CompoundTag tag = stack.getTag();
			if (tag == null) return;

			// Must have contract_allowed flag set during book signing
			if (!tag.getBoolean("contract_allowed")) {
				player.displayClientMessage(Component.literal("\u00A7cThis book does not allow contract signing."), true);
				return;
			}

			// Cannot re-sign
			if (tag.contains("contract_signer")) {
				player.displayClientMessage(Component.literal("\u00A7cThis book has already been signed."), true);
				return;
			}

			tag.putString("contract_signer", player.getGameProfile().getName());
			player.displayClientMessage(Component.literal("\u00A7aContract signed!"), true);
		});
		ctx.get().setPacketHandled(true);
	}

	public static void sendToServer(InteractionHand hand) {
		LostdepthsMod.PACKET_HANDLER.sendToServer(new ContractSignMessage(hand));
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		LostdepthsMod.addNetworkMessage(ContractSignMessage.class, ContractSignMessage::buffer, ContractSignMessage::new, ContractSignMessage::handler);
	}
}
