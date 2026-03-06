package cz.blackdragoncz.lostdepths.network;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.item.book.InfusedWritableBookItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SaveInfusedBookMessage {
	private final InteractionHand hand;
	private final List<String> pages;
	private final Optional<String> title;
	private final boolean contractAllowed;

	public SaveInfusedBookMessage(InteractionHand hand, List<String> pages, Optional<String> title, boolean contractAllowed) {
		this.hand = hand;
		this.pages = pages;
		this.title = title;
		this.contractAllowed = contractAllowed;
	}

	public SaveInfusedBookMessage(FriendlyByteBuf buf) {
		this.hand = buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		int pageCount = buf.readVarInt();
		this.pages = new ArrayList<>(pageCount);
		for (int i = 0; i < pageCount; i++) {
			this.pages.add(buf.readUtf(8192));
		}
		this.title = buf.readOptional(b -> b.readUtf(32));
		this.contractAllowed = buf.readBoolean();
	}

	public static void buffer(SaveInfusedBookMessage msg, FriendlyByteBuf buf) {
		buf.writeBoolean(msg.hand == InteractionHand.MAIN_HAND);
		buf.writeVarInt(msg.pages.size());
		for (String page : msg.pages) {
			buf.writeUtf(page, 8192);
		}
		buf.writeOptional(msg.title, (b, s) -> b.writeUtf(s, 32));
		buf.writeBoolean(msg.contractAllowed);
	}

	public static void handler(SaveInfusedBookMessage msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			if (player == null) return;
			ItemStack stack = player.getItemInHand(msg.hand);
			if (!stack.is(LostdepthsModItems.INFUSED_WRITABLE_BOOK.get())) return;
			if (msg.pages.size() > 100) return;

			// Update pages
			ListTag pagesList = new ListTag();
			for (String page : msg.pages) {
				String trimmed = page.length() > 1024 ? page.substring(0, 1024) : page;
				pagesList.add(StringTag.valueOf(trimmed));
			}

			if (msg.title.isPresent()) {
				// Signing the book - convert to written book
				String bookTitle = msg.title.get().trim();
				if (bookTitle.isEmpty() || bookTitle.length() > 32) return;

				ItemStack writtenBook = new ItemStack(LostdepthsModItems.INFUSED_WRITTEN_BOOK.get());
				CompoundTag tag = stack.getTag();
				CompoundTag newTag = tag != null ? tag.copy() : new CompoundTag();
				newTag.put("pages", pagesList);
				newTag.putString("title", bookTitle);
				newTag.putString("author", player.getGameProfile().getName());
				newTag.putInt("generation", 0);
				if (msg.contractAllowed) {
					newTag.putBoolean("contract_allowed", true);
				}
				writtenBook.setTag(newTag);
				player.setItemInHand(msg.hand, writtenBook);
			} else {
				// Just saving pages
				if (!pagesList.isEmpty()) {
					stack.addTagElement("pages", pagesList);
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		LostdepthsMod.addNetworkMessage(SaveInfusedBookMessage.class, SaveInfusedBookMessage::buffer, SaveInfusedBookMessage::new, SaveInfusedBookMessage::handler);
	}
}
