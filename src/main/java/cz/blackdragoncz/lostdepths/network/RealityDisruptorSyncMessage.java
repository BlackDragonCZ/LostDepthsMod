package cz.blackdragoncz.lostdepths.network;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.block.creative.entity.RealityDisruptorBlockEntity.Camera;
import cz.blackdragoncz.lostdepths.block.creative.entity.RealityDisruptorBlockEntity.Debug;
import cz.blackdragoncz.lostdepths.client.DisruptorClientState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RealityDisruptorSyncMessage {

    private final boolean active;
    private final boolean hideGui;
    private final Debug debug;
    private final Camera camera;
    private final String shader;

    public RealityDisruptorSyncMessage(boolean active, boolean hideGui, Debug debug, Camera camera, String shader) {
        this.active = active;
        this.hideGui = hideGui;
        this.debug = debug;
        this.camera = camera;
        this.shader = shader == null ? "" : shader;
    }

    public static RealityDisruptorSyncMessage inactive() {
        return new RealityDisruptorSyncMessage(false, false, Debug.NORMAL, Camera.FREE, "");
    }

    public RealityDisruptorSyncMessage(FriendlyByteBuf buf) {
        this.active = buf.readBoolean();
        this.hideGui = buf.readBoolean();
        this.debug = Debug.values()[buf.readByte()];
        this.camera = Camera.values()[buf.readByte()];
        this.shader = buf.readUtf(64);
    }

    public static void buffer(RealityDisruptorSyncMessage msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.active);
        buf.writeBoolean(msg.hideGui);
        buf.writeByte(msg.debug.ordinal());
        buf.writeByte(msg.camera.ordinal());
        buf.writeUtf(msg.shader, 64);
    }

    public static void handler(RealityDisruptorSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> DisruptorClientState.setReality(msg.active, msg.hideGui, msg.debug, msg.camera, msg.shader)));
        ctx.get().setPacketHandled(true);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RealityDisruptorSyncMessage other))
            return false;
        return active == other.active && hideGui == other.hideGui && debug == other.debug
                && camera == other.camera && shader.equals(other.shader);
    }

    @Override
    public int hashCode() {
        return Objects.hash(active, hideGui, debug, camera, shader);
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        LostdepthsMod.addNetworkMessage(RealityDisruptorSyncMessage.class, RealityDisruptorSyncMessage::buffer,
                RealityDisruptorSyncMessage::new, RealityDisruptorSyncMessage::handler);
    }
}
