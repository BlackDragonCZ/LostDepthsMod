package cz.blackdragoncz.lostdepths;

import cz.blackdragoncz.lostdepths.client.ClientSide;
import cz.blackdragoncz.lostdepths.config.LostDepthsConfig;
import cz.blackdragoncz.lostdepths.init.*;
import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import cz.blackdragoncz.lostdepths.util.SecurityClearanceSystem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
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

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;

import java.util.*;
import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiConsumer;
import java.util.concurrent.ConcurrentLinkedQueue;

import cz.blackdragoncz.lostdepths.world.features.StructureFeature;

@Mod("lostdepths")
public class LostdepthsMod {
	public static final Logger LOGGER = LogManager.getLogger(LostdepthsMod.class);
	public static final String MODID = "lostdepths";

	public LostdepthsMod() {
		MinecraftForge.EVENT_BUS.register(this);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, LostDepthsConfig.SPEC);
        //CORE
		LostdepthsModSounds.REGISTRY.register(bus);
		LostdepthsModBlocks.REGISTRY.register(bus);
		LostdepthsModBlockEntities.REGISTRY.register(bus);
		LostdepthsModItems.REGISTRY.register(bus);
		LostdepthsModEntities.REGISTRY.register(bus);
		LostdepthsModRecipeSerializers.REGISTRY_SERIALIZER.register(bus);
		LostDepthsModRecipeType.REGISTRY.register(bus);
        //MODIFIERS
		LostdepthsModTabs.REGISTRY.register(bus);
        LostdepthsModEnchantments.REGISTRY.register(bus);
        LostdepthsModModifiers.REGISTRY.register(bus);
        LostdepthsModAttributes.REGISTRY.register(bus);
        //WORLD
		StructureFeature.REGISTRY.register(bus);
		LostdepthsModMobEffects.REGISTRY.register(bus);
		LostdepthsModPotions.REGISTRY.register(bus);
        //MISC
		LostdepthsModParticleTypes.REGISTRY.register(bus);
		LostdepthsModVillagerProfessions.PROFESSIONS.register(bus);
		LostdepthsModMenus.REGISTRY.register(bus);
		LostdepthsModFluids.REGISTRY.register(bus);
		LostdepthsModFluidTypes.REGISTRY.register(bus);
		LostdepthsModLoots.REGISTER.register(bus);
		LostDepthsBiomeModifiers.REGISTRY.register(bus);

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
			SecurityClearanceSystem.update();
			// Update wormhole disruption tracking
			var server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
			if (server != null) {
				cz.blackdragoncz.lostdepths.warp.WormholeDisruptorManager.tick(server.getPlayerList().getPlayers());
			}
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

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event)
	{
		LOGGER.info("Mlem all other mods :3");
	}

	@SubscribeEvent
	public void onServerStopping(net.minecraftforge.event.server.ServerStoppingEvent event) {
		cz.blackdragoncz.lostdepths.warp.WormholeDisruptorManager.clear();
	}

	@SubscribeEvent
	public void onPlayerChangeDimension(net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent event) {
		var player = event.getEntity();
		var level = player.level();
		if (level.isClientSide || !(level instanceof net.minecraft.server.level.ServerLevel serverLevel)) return;

		var dimKey = event.getTo();
		String dimId = dimKey.location().toString();
		// Create spawn platform for lostdepths dimensions (like The End)
		if (dimId.equals("lostdepths:below_bedrock") || dimId.equals("lostdepths:between_bedrock_and_overworld")) {
			BlockPos platformCenter = new BlockPos(0, 64, 0);
			// Place a 5x5 platform of space_rock at y=64, clear space above
			for (int x = -2; x <= 2; x++) {
				for (int z = -2; z <= 2; z++) {
					BlockPos platformPos = platformCenter.offset(x, 0, z);
					serverLevel.setBlockAndUpdate(platformPos,
							net.minecraftforge.registries.ForgeRegistries.BLOCKS.getValue(
									new ResourceLocation("lostdepths", "space_rock")).defaultBlockState());
					// Clear 3 blocks above for breathing room
					for (int y = 1; y <= 3; y++) {
						serverLevel.setBlockAndUpdate(platformPos.above(y),
								net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onMobSpawn(net.minecraftforge.event.entity.living.MobSpawnEvent.FinalizeSpawn event) {
		if (!(event.getEntity() instanceof cz.blackdragoncz.lostdepths.entity.TheProtectorEntity)) return;

		String dimensionId = event.getLevel().getLevel().dimension().location().toString();
		if (!LostDepthsConfig.isAllowed(dimensionId, LostDepthsConfig.THE_PROTECTOR_DIMENSIONS)) {
			event.setSpawnCancelled(true);
			return;
		}

		var biomeHolder = event.getLevel().getBiome(event.getEntity().blockPosition());
		biomeHolder.unwrapKey().ifPresent(biomeKey -> {
			String biomeId = biomeKey.location().toString();
			if (!LostDepthsConfig.isAllowed(biomeId, LostDepthsConfig.THE_PROTECTOR_BIOMES)) {
				event.setSpawnCancelled(true);
			}
		});
	}
}
