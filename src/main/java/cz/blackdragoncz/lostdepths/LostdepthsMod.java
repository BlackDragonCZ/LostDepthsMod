package cz.blackdragoncz.lostdepths;

import cz.blackdragoncz.lostdepths.client.ClientSide;
import cz.blackdragoncz.lostdepths.init.*;
import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiConsumer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.AbstractMap;

import cz.blackdragoncz.lostdepths.world.features.StructureFeature;

@Mod("lostdepths")
public class LostdepthsMod {
	public static final Logger LOGGER = LogManager.getLogger(LostdepthsMod.class);
	public static final String MODID = "lostdepths";

	public LostdepthsMod() {
		MinecraftForge.EVENT_BUS.register(this);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		LostdepthsModSounds.REGISTRY.register(bus);
		LostdepthsModBlocks.REGISTRY.register(bus);
		LostdepthsModBlockEntities.REGISTRY.register(bus);
		LostdepthsModItems.REGISTRY.register(bus);
		LostdepthsModEntities.REGISTRY.register(bus);
		LostdepthsModRecipeSerializers.REGISTRY_SERIALIZER.register(bus);
		LostDepthsModRecipeType.REGISTRY.register(bus);

		LostdepthsModTabs.REGISTRY.register(bus);

		StructureFeature.REGISTRY.register(bus);
		LostdepthsModMobEffects.REGISTRY.register(bus);
		LostdepthsModPotions.REGISTRY.register(bus);

		LostdepthsModParticleTypes.REGISTRY.register(bus);
		LostdepthsModVillagerProfessions.PROFESSIONS.register(bus);
		LostdepthsModMenus.REGISTRY.register(bus);
		LostdepthsModFluids.REGISTRY.register(bus);
		LostdepthsModFluidTypes.REGISTRY.register(bus);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientSide.INSTANCE::setup);
	}

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	private static int messageID = 0;

	public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
		PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
		messageID++;
	}

	private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		workQueue.add(new AbstractMap.SimpleEntry(action, tick));
	}

	public static ResourceLocation rl(String path) {
		return new ResourceLocation(MODID, path);
	}

	@SubscribeEvent
	public void tick(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
			workQueue.forEach(work -> {
				work.setValue(work.getValue() - 1);
				if (work.getValue() == 0)
					actions.add(work);
			});
			actions.forEach(e -> e.getKey().run());
			workQueue.removeAll(actions);
		}
	}
}
