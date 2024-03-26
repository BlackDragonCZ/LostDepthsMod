
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package cz.blackdragoncz.lostdepths.init;

import org.lwjgl.glfw.GLFW;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import cz.blackdragoncz.lostdepths.network.DragonUpMessage;
import cz.blackdragoncz.lostdepths.network.DragonDownMessage;
import cz.blackdragoncz.lostdepths.network.ActionButtonMessage;
import cz.blackdragoncz.lostdepths.LostdepthsMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class LostdepthsModKeyMappings {
	public static final KeyMapping ACTION_BUTTON = new KeyMapping("key.lostdepths.action_button", GLFW.GLFW_KEY_KP_DECIMAL, "key.categories.gameplay") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				LostdepthsMod.PACKET_HANDLER.sendToServer(new ActionButtonMessage(0, 0));
				ActionButtonMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping DRAGON_DOWN = new KeyMapping("key.lostdepths.dragon_down", GLFW.GLFW_KEY_LEFT_CONTROL, "key.categories.movement") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				LostdepthsMod.PACKET_HANDLER.sendToServer(new DragonDownMessage(0, 0));
				DragonDownMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				DRAGON_DOWN_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - DRAGON_DOWN_LASTPRESS);
				LostdepthsMod.PACKET_HANDLER.sendToServer(new DragonDownMessage(1, dt));
				DragonDownMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping DRAGON_UP = new KeyMapping("key.lostdepths.dragon_up", GLFW.GLFW_KEY_Y, "key.categories.movement") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				LostdepthsMod.PACKET_HANDLER.sendToServer(new DragonUpMessage(0, 0));
				DragonUpMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				DRAGON_UP_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - DRAGON_UP_LASTPRESS);
				LostdepthsMod.PACKET_HANDLER.sendToServer(new DragonUpMessage(1, dt));
				DragonUpMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	private static long DRAGON_DOWN_LASTPRESS = 0;
	private static long DRAGON_UP_LASTPRESS = 0;

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(ACTION_BUTTON);
		event.register(DRAGON_DOWN);
		event.register(DRAGON_UP);
	}

	@Mod.EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event) {
			if (Minecraft.getInstance().screen == null) {
				ACTION_BUTTON.consumeClick();
				DRAGON_DOWN.consumeClick();
				DRAGON_UP.consumeClick();
			}
		}
	}
}
