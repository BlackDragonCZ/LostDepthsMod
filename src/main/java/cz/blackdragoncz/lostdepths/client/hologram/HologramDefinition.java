package cz.blackdragoncz.lostdepths.client.hologram;

import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;

// Presentation for one hologram, authored next to the .nbt so a pack can tune it without touching the structure.
public record HologramDefinition(float scale, float offsetX, float offsetY, float offsetZ, int tint, float alpha, float spin) {

	// One structure block per pixel, as requested: 1/16 of a block.
	public static final HologramDefinition DEFAULT = new HologramDefinition(0.0625f, 0, 1.0f, 0, 0xFFFFFF, 0.65f, 0);

	public static HologramDefinition fromJson(JsonObject json) {
		float scale = GsonHelper.getAsFloat(json, "scale", DEFAULT.scale);
		float ox = DEFAULT.offsetX;
		float oy = DEFAULT.offsetY;
		float oz = DEFAULT.offsetZ;
		if (json.has("offset")) {
			var offset = GsonHelper.getAsJsonArray(json, "offset");
			if (offset.size() == 3) {
				ox = offset.get(0).getAsFloat();
				oy = offset.get(1).getAsFloat();
				oz = offset.get(2).getAsFloat();
			}
		}
		int tint = DEFAULT.tint;
		if (json.has("tint")) {
			String raw = GsonHelper.getAsString(json, "tint").replace("#", "");
			try {
				tint = Integer.parseInt(raw, 16) & 0xFFFFFF;
			} catch (NumberFormatException ignored) {
			}
		}
		return new HologramDefinition(scale, ox, oy, oz, tint, GsonHelper.getAsFloat(json, "alpha", DEFAULT.alpha), GsonHelper.getAsFloat(json, "spin", DEFAULT.spin));
	}
}
