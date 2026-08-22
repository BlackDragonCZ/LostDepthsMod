package cz.blackdragoncz.lostdepths.hologram;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// The server never renders a hologram, so it only needs the list of valid ids - a tiny datapack index rather than the templates themselves. This is
// what lets it validate and cycle a projector's selection without ever holding the structure data.
@Mod.EventBusSubscriber
public final class HologramIndex extends SimpleJsonResourceReloadListener {

	private static final Gson GSON = new Gson();
	private static final List<ResourceLocation> IDS = new ArrayList<>();

	public HologramIndex() {
		super(GSON, "holograms");
	}

	@SubscribeEvent
	public static void onAddReloadListener(AddReloadListenerEvent event) {
		event.addListener(new HologramIndex());
	}

	public static List<ResourceLocation> ids() {
		return List.copyOf(IDS);
	}

	public static ResourceLocation next(ResourceLocation current) {
		if (IDS.isEmpty())
			return null;
		int at = IDS.indexOf(current);
		return IDS.get((at + 1) % IDS.size());
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager manager, ProfilerFiller profiler) {
		IDS.clear();
		for (Map.Entry<ResourceLocation, JsonElement> file : files.entrySet()) {
			if (!file.getValue().isJsonObject())
				continue;
			JsonObject json = file.getValue().getAsJsonObject();
			if (!json.has("values"))
				continue;
			for (JsonElement value : GsonHelper.getAsJsonArray(json, "values")) {
				ResourceLocation id = ResourceLocation.tryParse(value.getAsString());
				if (id != null && !IDS.contains(id))
					IDS.add(id);
			}
		}
		IDS.sort(ResourceLocation::compareTo);
	}
}
