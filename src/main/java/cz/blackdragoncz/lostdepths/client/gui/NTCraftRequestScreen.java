package cz.blackdragoncz.lostdepths.client.gui;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.item.storage.EncodedPatternItem;
import cz.blackdragoncz.lostdepths.network.storage.NTCraftRequestPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Crafting request screen — opened from terminal when player wants to craft an item.
 * Phase 1: Amount input with text field (default 1).
 * Phase 2: Ingredient tree showing required items + available count.
 * Start button sends NTCraftRequestPacket. Cancel returns to terminal.
 */
public class NTCraftRequestScreen extends Screen {

	private enum Phase { AMOUNT, CONFIRM }

	private final Screen parentScreen;
	private final ItemStack targetOutput;
	private final ItemStack pattern;

	private Phase phase = Phase.AMOUNT;
	private EditBox amountBox;
	private int requestedAmount = 1;

	public NTCraftRequestScreen(Screen parent, ItemStack output, ItemStack pattern) {
		super(Component.literal("Craft Request"));
		this.parentScreen = parent;
		this.targetOutput = output.copy();
		this.pattern = pattern.copy();
	}

	@Override
	protected void init() {
		clearWidgets();

		int cx = this.width / 2;
		int cy = this.height / 2;

		if (phase == Phase.AMOUNT) {
			// Amount input phase
			amountBox = new EditBox(this.font, cx - 30, cy - 20, 60, 16, Component.literal("Amount"));
			amountBox.setValue("1");
			amountBox.setMaxLength(6);
			amountBox.setTextColor(0xFFFFFF);
			amountBox.setFilter(s -> s.matches("\\d*"));
			addRenderableWidget(amountBox);

			addRenderableWidget(Button.builder(Component.literal("Next"), btn -> {
				try {
					requestedAmount = Math.max(1, Integer.parseInt(amountBox.getValue()));
				} catch (NumberFormatException e) {
					requestedAmount = 1;
				}
				phase = Phase.CONFIRM;
				init();
			}).bounds(cx - 52, cy + 10, 50, 20).build());

			addRenderableWidget(Button.builder(Component.literal("Cancel"), btn -> {
				minecraft.setScreen(parentScreen);
			}).bounds(cx + 2, cy + 10, 50, 20).build());

		} else {
			// Confirm phase — show ingredient tree + start/cancel
			addRenderableWidget(Button.builder(Component.literal("§aStart Craft"), btn -> {
				LostdepthsMod.PACKET_HANDLER.sendToServer(
						new NTCraftRequestPacket(targetOutput, requestedAmount));
				minecraft.setScreen(parentScreen);
			}).bounds(cx - 52, cy + 60, 50, 20).build());

			addRenderableWidget(Button.builder(Component.literal("Cancel"), btn -> {
				minecraft.setScreen(parentScreen);
			}).bounds(cx + 2, cy + 60, 50, 20).build());
		}
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		renderBackground(graphics);

		int cx = this.width / 2;
		int cy = this.height / 2;

		// Background panel
		int panelW = 200;
		int panelH = phase == Phase.AMOUNT ? 80 : 160;
		int px = cx - panelW / 2;
		int py = cy - panelH / 2;

		graphics.fill(px - 1, py - 1, px + panelW + 1, py + panelH + 1, 0xFF222222);
		graphics.fill(px, py, px + panelW, py + panelH, 0xFF3C3C3C);

		if (phase == Phase.AMOUNT) {
			// Title + item icon
			graphics.drawString(font, "Craft Request", cx - font.width("Craft Request") / 2, cy - 46, 0xFFFFFF, true);

			// Render target item
			graphics.renderItem(targetOutput, cx - 8, cy - 38);
			graphics.drawString(font, targetOutput.getHoverName(), cx + 12, cy - 34, 0xFFFFFF, false);

			graphics.drawString(font, "Amount:", cx - 60, cy - 16, 0xAAAAAA, false);

		} else {
			// Confirm phase
			graphics.drawString(font, "Crafting: " + targetOutput.getHoverName().getString(),
					px + 6, py + 6, 0xFFFFFF, false);
			graphics.drawString(font, "Amount: §e" + requestedAmount, px + 6, py + 18, 0xAAAAAA, false);

			// Ingredient tree
			graphics.drawString(font, "§7Required ingredients:", px + 6, py + 34, 0xAAAAAA, false);

			List<ItemStack> inputs = EncodedPatternItem.getInputs(pattern);
			List<ItemStack> outputs = EncodedPatternItem.getOutputs(pattern);
			int outputPerCraft = outputs.isEmpty() ? 1 : outputs.get(0).getCount();
			int batches = Math.max(1, (requestedAmount + outputPerCraft - 1) / outputPerCraft);

			int yOffset = py + 48;
			for (ItemStack input : inputs) {
				if (input.isEmpty()) continue;
				int needed = input.getCount() * batches;

				graphics.renderItem(input, px + 8, yOffset);
				String name = input.getHoverName().getString();
				graphics.drawString(font, "§f" + name + " §7x" + needed, px + 28, yOffset + 4, 0xFFFFFF, false);
				yOffset += 18;

				if (yOffset > py + panelH - 30) break;
			}
		}

		super.render(graphics, mouseX, mouseY, partialTick);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == 256) { // Escape
			minecraft.setScreen(parentScreen);
			return true;
		}
		if (phase == Phase.AMOUNT && amountBox != null && amountBox.isFocused()) {
			if (keyCode == 257 || keyCode == 335) { // Enter
				try {
					requestedAmount = Math.max(1, Integer.parseInt(amountBox.getValue()));
				} catch (NumberFormatException e) {
					requestedAmount = 1;
				}
				phase = Phase.CONFIRM;
				init();
				return true;
			}
			return amountBox.keyPressed(keyCode, scanCode, modifiers);
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char c, int modifiers) {
		if (phase == Phase.AMOUNT && amountBox != null && amountBox.isFocused()) {
			return amountBox.charTyped(c, modifiers);
		}
		return super.charTyped(c, modifiers);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
