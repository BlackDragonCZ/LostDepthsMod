package cz.blackdragoncz.lostdepths.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

/**
 * Custom animated text formatting effects for tooltips and GUI labels.
 * These require a tick value for animation and build per-character styled Components.
 * On Graphics: Fast, all effects fall back to static base color (no animation).
 */
public class TextEffects {

	/**
	 * Returns true if fancy graphics are enabled (animation allowed).
	 * Falls back to true if called on the server or options are unavailable.
	 */
	private static boolean isFancy() {
		try {
			return Minecraft.getInstance().options.graphicsMode().get() != GraphicsStatus.FAST;
		} catch (Exception e) {
			return true;
		}
	}

	// --- Rainbow ---

	/**
	 * Colors each character with a cycling rainbow. Static (no animation).
	 */
	public static MutableComponent rainbow(String text) {
		return rainbow(text, 0);
	}

	/**
	 * Colors each character with a cycling rainbow, offset by tick for animation.
	 * @param text  the string to colorize
	 * @param tick  game tick for animation (0 for static)
	 */
	public static MutableComponent rainbow(String text, long tick) {
		if (!isFancy()) {
			// Static: use middle of rainbow (green-ish) as flat color
			return Component.literal(text).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(hsbToRgb(0.33f, 0.8f, 1.0f))));
		}
		MutableComponent result = Component.empty();
		int len = text.length();
		for (int i = 0; i < len; i++) {
			char c = text.charAt(i);
			if (c == ' ') {
				result.append(Component.literal(" "));
				continue;
			}
			float hue = ((tick * 3L + i * 360L / Math.max(len, 1)) % 360) / 360f;
			int rgb = hsbToRgb(hue, 0.8f, 1.0f);
			result.append(Component.literal(String.valueOf(c)).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(rgb))));
		}
		return result;
	}

	// --- Wave ---

	/**
	 * Wave shimmer effect — a brightness peak travels across the text.
	 * @param text    the string
	 * @param color   base RGB color (e.g. 0x00CCFF)
	 * @param tick    game tick for animation
	 * @param bounce  false = left-to-right loop, true = left-to-right-to-left bounce
	 */
	public static MutableComponent wave(String text, int color, long tick, boolean bounce) {
		MutableComponent result = Component.empty();
		int len = text.length();
		if (len == 0) return result;

		if (!isFancy()) {
			return Component.literal(text).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(color)));
		}

		// Peak position (0.0 to 1.0 across the string)
		float speed = 0.05f;
		float rawPos;
		if (bounce) {
			// Ping-pong: 0→1→0→1...
			float cycle = (tick * speed) % 2f;
			rawPos = cycle <= 1f ? cycle : 2f - cycle;
		} else {
			// One direction: 0→1→0→1...
			rawPos = (tick * speed) % 1f;
		}

		float peakCenter = rawPos * (len - 1);
		float peakWidth = Math.max(3f, len * 0.25f);

		int baseR = (color >> 16) & 0xFF;
		int baseG = (color >> 8) & 0xFF;
		int baseB = color & 0xFF;

		for (int i = 0; i < len; i++) {
			char c = text.charAt(i);
			if (c == ' ') {
				result.append(Component.literal(" "));
				continue;
			}

			float dist = Math.abs(i - peakCenter);
			float brightness = Math.max(0f, 1f - (dist / peakWidth));
			// Smooth falloff
			brightness = brightness * brightness;

			// Lerp toward white based on brightness
			int r = Math.min(255, baseR + (int) ((255 - baseR) * brightness));
			int g = Math.min(255, baseG + (int) ((255 - baseG) * brightness));
			int b = Math.min(255, baseB + (int) ((255 - baseB) * brightness));

			int rgb = (r << 16) | (g << 8) | b;
			result.append(Component.literal(String.valueOf(c)).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(rgb))));
		}
		return result;
	}

	// --- HSB to RGB helper ---

	private static int hsbToRgb(float hue, float saturation, float brightness) {
		int r = 0, g = 0, b = 0;
		if (saturation == 0) {
			r = g = b = (int) (brightness * 255f + 0.5f);
		} else {
			float h = (hue - (float) Math.floor(hue)) * 6f;
			float f = h - (float) Math.floor(h);
			float p = brightness * (1f - saturation);
			float q = brightness * (1f - saturation * f);
			float t = brightness * (1f - (saturation * (1f - f)));
			switch ((int) h) {
				case 0 -> { r = (int) (brightness * 255f + 0.5f); g = (int) (t * 255f + 0.5f); b = (int) (p * 255f + 0.5f); }
				case 1 -> { r = (int) (q * 255f + 0.5f); g = (int) (brightness * 255f + 0.5f); b = (int) (p * 255f + 0.5f); }
				case 2 -> { r = (int) (p * 255f + 0.5f); g = (int) (brightness * 255f + 0.5f); b = (int) (t * 255f + 0.5f); }
				case 3 -> { r = (int) (p * 255f + 0.5f); g = (int) (q * 255f + 0.5f); b = (int) (brightness * 255f + 0.5f); }
				case 4 -> { r = (int) (t * 255f + 0.5f); g = (int) (p * 255f + 0.5f); b = (int) (brightness * 255f + 0.5f); }
				case 5 -> { r = (int) (brightness * 255f + 0.5f); g = (int) (p * 255f + 0.5f); b = (int) (q * 255f + 0.5f); }
			}
		}
		return (r << 16) | (g << 8) | b;
	}
}
