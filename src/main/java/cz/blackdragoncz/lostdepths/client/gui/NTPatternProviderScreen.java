package cz.blackdragoncz.lostdepths.client.gui;

import cz.blackdragoncz.lostdepths.world.inventory.NTPatternProviderMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class NTPatternProviderScreen extends AbstractContainerScreen<NTPatternProviderMenu> {

	public NTPatternProviderScreen(NTPatternProviderMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
		int slotCount = menu.getProvider().getSlotCount();
		int rows = (slotCount + 8) / 9;
		this.imageWidth = 176;
		this.imageHeight = 18 + rows * 18 + 6 + 18 + 8 + 76 + 18; // patterns + gap + result + gap + player inv
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		NTGuiTheme.background(graphics, leftPos, topPos, imageWidth, imageHeight);

		int slotCount = menu.getProvider().getSlotCount();
		int cols = 9;
		int rows = (slotCount + cols - 1) / cols;

		// Pattern slots
		for (int i = 0; i < slotCount; i++) {
			int fx = 8 + (i % cols) * 18;
			int fy = 18 + (i / cols) * 18;
			NTGuiTheme.slot(graphics, leftPos, topPos, fx, fy);
			NTGuiTheme.tint(graphics, leftPos, topPos, fx, fy, NTGuiTheme.TINT_PATTERN);
		}

		// Result buffer (row below patterns)
		int resultY = 18 + rows * 18 + 6;
		graphics.drawString(font, "Results:", this.leftPos + 8, this.topPos + resultY - 10, NTGuiTheme.BODY, false);
		for (int i = 0; i < 9; i++) {
			int fx = 8 + i * 18;
			NTGuiTheme.slot(graphics, leftPos, topPos, fx, resultY);
			NTGuiTheme.tint(graphics, leftPos, topPos, fx, resultY, NTGuiTheme.TINT_OUTPUT);
		}

		// Player inventory
		int playerY = resultY + 24;
		NTGuiTheme.playerInventory(graphics, leftPos, topPos, 8, playerY);

		// Title
		graphics.drawString(font, "Pattern Provider", this.leftPos + 8, this.topPos + 6, NTGuiTheme.HEADING, false);
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
		// Titles drawn in renderBg (custom positions); nothing default here.
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, partialTick);
		renderTooltip(graphics, mouseX, mouseY);
	}
}
