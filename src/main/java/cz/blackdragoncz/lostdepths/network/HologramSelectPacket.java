package cz.blackdragoncz.lostdepths.network;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.block.entity.HologramProjectorBlockEntity;
import cz.blackdragoncz.lostdepths.hologram.HologramIndex;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

// Client -> Server: the whole projector configuration. Only the id travels, never the template; the client already has that.
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class HologramSelectPacket {

	private static final double REACH = 8.0D;

	private final BlockPos pos;
	@Nullable
	private final ResourceLocation hologram;
	private final float offsetX;
	private final float offsetY;
	private final float offsetZ;
	private final float rotationX;
	private final float rotationY;
	private final float rotationZ;
	private final boolean visible;
	private final boolean culled;

	public HologramSelectPacket(BlockPos pos, @Nullable ResourceLocation hologram, float offsetX, float offsetY, float offsetZ, float rotationX, float rotationY, float rotationZ, boolean visible, boolean culled) {
		this.pos = pos;
		this.hologram = hologram;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.rotationX = rotationX;
		this.rotationY = rotationY;
		this.rotationZ = rotationZ;
		this.visible = visible;
		this.culled = culled;
	}

	public HologramSelectPacket(FriendlyByteBuf buf) {
		this.pos = buf.readBlockPos();
		this.hologram = buf.readBoolean() ? buf.readResourceLocation() : null;
		this.offsetX = buf.readFloat();
		this.offsetY = buf.readFloat();
		this.offsetZ = buf.readFloat();
		this.rotationX = buf.readFloat();
		this.rotationY = buf.readFloat();
		this.rotationZ = buf.readFloat();
		this.visible = buf.readBoolean();
		this.culled = buf.readBoolean();
	}

	public static void encode(HologramSelectPacket msg, FriendlyByteBuf buf) {
		buf.writeBlockPos(msg.pos);
		buf.writeBoolean(msg.hologram != null);
		if (msg.hologram != null)
			buf.writeResourceLocation(msg.hologram);
		buf.writeFloat(msg.offsetX);
		buf.writeFloat(msg.offsetY);
		buf.writeFloat(msg.offsetZ);
		buf.writeFloat(msg.rotationX);
		buf.writeFloat(msg.rotationY);
		buf.writeFloat(msg.rotationZ);
		buf.writeBoolean(msg.visible);
		buf.writeBoolean(msg.culled);
	}

	public static void handle(HologramSelectPacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			if (player == null)
				return;
			if (!player.level().isLoaded(msg.pos) || player.distanceToSqr(msg.pos.getX() + 0.5, msg.pos.getY() + 0.5, msg.pos.getZ() + 0.5) > REACH * REACH)
				return;
			// A client could name anything, so only ids the server actually indexes are accepted. The block entity clamps the numbers.
			if (msg.hologram != null && !HologramIndex.ids().contains(msg.hologram))
				return;
			if (player.level().getBlockEntity(msg.pos) instanceof HologramProjectorBlockEntity projector)
				projector.configure(msg.hologram, msg.offsetX, msg.offsetY, msg.offsetZ, msg.rotationX, msg.rotationY, msg.rotationZ, msg.visible, msg.culled);
		});
		ctx.get().setPacketHandled(true);
	}

	@SubscribeEvent
	public static void register(FMLCommonSetupEvent event) {
		LostdepthsMod.addNetworkMessage(HologramSelectPacket.class, HologramSelectPacket::encode, HologramSelectPacket::new, HologramSelectPacket::handle);
	}
}
