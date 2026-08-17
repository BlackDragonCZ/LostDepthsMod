package cz.blackdragoncz.lostdepths.client;

import cz.blackdragoncz.lostdepths.client.gui.InfusedBookEditScreen;
import cz.blackdragoncz.lostdepths.client.gui.InfusedBookViewScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

// Must stay out of the item classes: a Screen reference there loads Screen on the dedicated server. Call only via DistExecutor.
public final class ClientBookHooks {

	private ClientBookHooks() {
	}

	public static void openWritableBook(Player player, ItemStack stack, InteractionHand hand) {
		Minecraft.getInstance().setScreen(new InfusedBookEditScreen(player, stack, hand));
	}

	public static void openWrittenBook(ItemStack stack) {
		Minecraft.getInstance().setScreen(new InfusedBookViewScreen(stack));
	}
}
