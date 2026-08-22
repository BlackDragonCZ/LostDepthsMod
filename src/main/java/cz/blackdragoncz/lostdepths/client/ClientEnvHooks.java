package cz.blackdragoncz.lostdepths.client;

import cz.blackdragoncz.lostdepths.init.LostdepthsModKeyMappings;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

// Must stay out of common code: every member here resolves a net.minecraft.client type. Call only behind FMLEnvironment.dist.isClient().
public final class ClientEnvHooks {

	private ClientEnvHooks() {
	}

	public static boolean isFancyGraphics() {
		Minecraft mc = Minecraft.getInstance();
		return mc == null || mc.options == null || mc.options.graphicsMode().get() != GraphicsStatus.FAST;
	}

	public static long clientGameTime() {
		Minecraft mc = Minecraft.getInstance();
		return (mc == null || mc.level == null) ? 0L : mc.level.getGameTime();
	}

	public static Component actionButtonKeyName() {
		return LostdepthsModKeyMappings.ACTION_BUTTON.getTranslatedKeyMessage();
	}
}
