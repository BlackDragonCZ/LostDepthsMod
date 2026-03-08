package cz.blackdragoncz.lostdepths.network;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.block.entity.FusionTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FusionTablePlaceShapePacket {

    private final BlockPos pos;
    private final int cellIndex;

    public FusionTablePlaceShapePacket(BlockPos pos, int cellIndex) {
        this.pos = pos;
        this.cellIndex = cellIndex;
    }

    public FusionTablePlaceShapePacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.cellIndex = buffer.readInt();
    }

    public static void buffer(FusionTablePlaceShapePacket message, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(message.pos);
        buffer.writeInt(message.cellIndex);
    }

    public static void handler(FusionTablePlaceShapePacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            Level level = player.level();
            if (!level.isLoaded(message.pos)) return;

            // Verify player is close enough
            if (!message.pos.closerToCenterThan(player.position(), 8.0)) return;

            BlockEntity be = level.getBlockEntity(message.pos);
            if (be instanceof FusionTableBlockEntity fusionTable) {
                fusionTable.tryPlaceShape(message.cellIndex);
            }
        });
        context.setPacketHandled(true);
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        LostdepthsMod.addNetworkMessage(
                FusionTablePlaceShapePacket.class,
                FusionTablePlaceShapePacket::buffer,
                FusionTablePlaceShapePacket::new,
                FusionTablePlaceShapePacket::handler
        );
    }
}
