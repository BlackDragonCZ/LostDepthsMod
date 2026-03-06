package cz.blackdragoncz.lostdepths.network;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.client.ClientSide;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class WarpDisruptionSyncMessage {

    private final boolean disrupted;

    public WarpDisruptionSyncMessage(boolean disrupted) {
        this.disrupted = disrupted;
    }

    public WarpDisruptionSyncMessage(FriendlyByteBuf buf) {
        this.disrupted = buf.readBoolean();
    }

    public static void buffer(WarpDisruptionSyncMessage msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.disrupted);
    }

    public static void handler(WarpDisruptionSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientSide.setWarpDisrupted(msg.disrupted));
        });
        ctx.get().setPacketHandled(true);
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        LostdepthsMod.addNetworkMessage(WarpDisruptionSyncMessage.class, WarpDisruptionSyncMessage::buffer, WarpDisruptionSyncMessage::new, WarpDisruptionSyncMessage::handler);
    }
}
