package cz.blackdragoncz.lostdepths.client.gui;

import cz.blackdragoncz.lostdepths.world.inventory.NTDriveMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class NTDriveScreen extends AbstractContainerScreen<NTDriveMenu> {

	public NTDriveScreen(NTDriveMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
		this.imageHeight = 166;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		NTGuiTheme.background(graphics, leftPos, topPos, imageWidth, imageHeight);

		// Crystal slots — centered horizontally (mirrors NTDriveMenu placement)
		int slotCount = menu.getSlotCount();
		int startX = (176 - slotCount * 18) / 2;
		for (int i = 0; i < slotCount; i++) {
			int fx = startX + 1 + i * 18;
			NTGuiTheme.slot(graphics, leftPos, topPos, fx, 35);
			NTGuiTheme.tint(graphics, leftPos, topPos, fx, 35, NTGuiTheme.TINT_CRYSTAL);
		}

		// Player inventory
		NTGuiTheme.playerInventory(graphics, leftPos, topPos, 8, 84);
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
		graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, NTGuiTheme.HEADING, false);
		graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, NTGuiTheme.BODY, false);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, partialTick);
		renderTooltip(graphics, mouseX, mouseY);
	}
}
