package cz.blackdragoncz.lostdepths.network;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.client.DisruptorClientState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class GravatorDisruptorSyncMessage {

    private final boolean active;

    public GravatorDisruptorSyncMessage(boolean active) {
        this.active = active;
    }

    public GravatorDisruptorSyncMessage(FriendlyByteBuf buf) {
        this.active = buf.readBoolean();
    }

    public static void buffer(GravatorDisruptorSyncMessage msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.active);
    }

    public static void handler(GravatorDisruptorSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> DisruptorClientState.setGravator(msg.active)));
        ctx.get().setPacketHandled(true);
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        LostdepthsMod.addNetworkMessage(GravatorDisruptorSyncMessage.class, GravatorDisruptorSyncMessage::buffer,
                GravatorDisruptorSyncMessage::new, GravatorDisruptorSyncMessage::handler);
    }
}
