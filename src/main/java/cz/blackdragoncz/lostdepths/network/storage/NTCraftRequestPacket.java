package cz.blackdragoncz.lostdepths.network.storage;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.block.entity.storage.NTControllerBlockEntity;
import cz.blackdragoncz.lostdepths.block.entity.storage.NTTerminalBlockEntity;
import cz.blackdragoncz.lostdepths.storage.crafting.CraftingManager;
import cz.blackdragoncz.lostdepths.storage.crafting.CraftingTask;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetwork;
import cz.blackdragoncz.lostdepths.world.inventory.NTCraftingTerminalMenu;
import cz.blackdragoncz.lostdepths.world.inventory.NTTerminalMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Client → Server: Player requests autocrafting of an item from terminal.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NTCraftRequestPacket {
	private final ItemStack output;
	private final int count;

	public NTCraftRequestPacket(ItemStack output, int count) {
		this.output = output;
		this.count = count;
	}

	public NTCraftRequestPacket(FriendlyByteBuf buf) {
		this.output = buf.readItem();
		this.count = buf.readInt();
	}

	public static void encode(NTCraftRequestPacket msg, FriendlyByteBuf buf) {
		buf.writeItem(msg.output);
		buf.writeInt(msg.count);
	}

	public static void handle(NTCraftRequestPacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			if (player == null || !(player.level() instanceof ServerLevel serverLevel)) return;

			// Get terminal pos from open menu
			BlockPos terminalPos = null;
			if (player.containerMenu instanceof NTTerminalMenu menu) {
				terminalPos = menu.getTerminalPos();
			} else if (player.containerMenu instanceof NTCraftingTerminalMenu menu) {
				terminalPos = menu.getTerminalPos();
			}
			if (terminalPos == null) return;

			// Get network via terminal BE
			BlockEntity terminalBe = serverLevel.getBlockEntity(terminalPos);
			if (!(terminalBe instanceof NTTerminalBlockEntity terminal)) return;

			StorageNetwork network = terminal.getNetwork();
			if (network == null || !network.isActive()) {
				player.displayClientMessage(Component.literal("§cNetwork offline"), true);
				return;
			}

			// Get controller's crafting manager
			BlockEntity controllerBe = serverLevel.getBlockEntity(network.getControllerPos());
			if (!(controllerBe instanceof NTControllerBlockEntity controller)) return;

			CraftingManager manager = controller.getCraftingManager();
			if (manager == null) {
				player.displayClientMessage(Component.literal("§cCrafting system unavailable"), true);
				return;
			}

			if (!manager.canAcceptTask()) {
				player.displayClientMessage(Component.literal("§cToo many active crafting tasks"), true);
				return;
			}

			CraftingTask task = manager.requestCraft(msg.output, msg.count, serverLevel);
			if (task != null) {
				player.displayClientMessage(Component.literal("§aCrafting started: " +
						msg.output.getHoverName().getString() + " x" + msg.count), true);
			} else {
				player.displayClientMessage(Component.literal("§cNo pattern found for " +
						msg.output.getHoverName().getString()), true);
			}
		});
		ctx.get().setPacketHandled(true);
	}

	@SubscribeEvent
	public static void register(FMLCommonSetupEvent event) {
		LostdepthsMod.addNetworkMessage(NTCraftRequestPacket.class, NTCraftRequestPacket::encode, NTCraftRequestPacket::new, NTCraftRequestPacket::handle);
	}
}
