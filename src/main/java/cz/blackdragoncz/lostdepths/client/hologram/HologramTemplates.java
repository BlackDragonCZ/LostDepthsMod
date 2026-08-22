package cz.blackdragoncz.lostdepths.client.hologram;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

// Loads assets/<ns>/holograms/*.nbt on the client. Deliberately an ASSET, not datapack data: only the client renders holograms, and datapack files are
// never sent to clients. Any resource pack, the mod jar, and kubejs/assets all feed this identically, so no KubeJS-specific code is needed.
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class HologramTemplates extends SimplePreparableReloadListener<Map<ResourceLocation, HologramTemplates.Entry>> {

	public record Entry(HologramMesh mesh, HologramDefinition definition) {
	}

	private static final Logger LOGGER = LogUtils.getLogger();
	private static final String DIRECTORY = "holograms";
	private static final String NBT_SUFFIX = ".nbt";
	private static final Map<ResourceLocation, Entry> LOADED = new HashMap<>();

	@SubscribeEvent
	public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(new HologramTemplates());
	}

	public static Entry get(ResourceLocation id) {
		return LOADED.get(id);
	}

	public static boolean has(ResourceLocation id) {
		return LOADED.containsKey(id);
	}

	public static java.util.Set<ResourceLocation> ids() {
		return java.util.Set.copyOf(LOADED.keySet());
	}

	@Override
	protected Map<ResourceLocation, Entry> prepare(ResourceManager manager, ProfilerFiller profiler) {
		Map<ResourceLocation, Entry> loaded = new HashMap<>();
		for (Map.Entry<ResourceLocation, Resource> found : manager.listResources(DIRECTORY, path -> path.getPath().endsWith(NBT_SUFFIX)).entrySet()) {
			ResourceLocation file = found.getKey();
			ResourceLocation id = stripPath(file);
			try (InputStream in = found.getValue().open()) {
				CompoundTag nbt = NbtIo.readCompressed(in);
				HologramMesh mesh = HologramMesh.bake(nbt);
				loaded.put(id, new Entry(mesh, readDefinition(manager, id)));
				LOGGER.debug("Baked hologram {} ({}x{}x{}, {} faces)", id, mesh.sizeX(), mesh.sizeY(), mesh.sizeZ(), mesh.faceCount());
			} catch (Exception e) {
				LOGGER.error("Failed to bake hologram {}", file, e);
			}
		}
		return loaded;
	}

	@Override
	protected void apply(Map<ResourceLocation, Entry> prepared, ResourceManager manager, ProfilerFiller profiler) {
		LOADED.clear();
		LOADED.putAll(prepared);
		LOGGER.info("Loaded {} hologram template(s)", LOADED.size());
	}

	private static HologramDefinition readDefinition(ResourceManager manager, ResourceLocation id) {
		ResourceLocation jsonPath = new ResourceLocation(id.getNamespace(), DIRECTORY + "/" + id.getPath() + ".json");
		return manager.getResource(jsonPath).map(resource -> {
			try (var reader = resource.openAsReader()) {
				JsonObject json = GsonHelper.parse(reader);
				return HologramDefinition.fromJson(json);
			} catch (Exception e) {
				LOGGER.error("Failed to read hologram definition {}", jsonPath, e);
				return HologramDefinition.DEFAULT;
			}
		}).orElse(HologramDefinition.DEFAULT);
	}

	private static ResourceLocation stripPath(ResourceLocation file) {
		String path = file.getPath();
		return new ResourceLocation(file.getNamespace(), path.substring(DIRECTORY.length() + 1, path.length() - NBT_SUFFIX.length()));
	}

}
