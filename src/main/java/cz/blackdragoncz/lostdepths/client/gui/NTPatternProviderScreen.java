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
		graphics.fill(this.leftPos, this.topPos, this.leftPos + imageWidth, this.topPos + imageHeight, 0xFFC6C6C6);

		int slotCount = menu.getProvider().getSlotCount();
		int cols = 9;
		int rows = (slotCount + cols - 1) / cols;

		// Pattern slots
		for (int i = 0; i < slotCount; i++) {
			int row = i / cols;
			int col = i % cols;
			int sx = this.leftPos + 7 + col * 18;
			int sy = this.topPos + 17 + row * 18;
			graphics.fill(sx, sy, sx + 18, sy + 18, 0xFF373737);
			graphics.fill(sx + 1, sy + 1, sx + 17, sy + 17, 0xFF6060B0);
		}

		// Result buffer
		int resultY = this.topPos + 17 + rows * 18 + 6;
		graphics.drawString(font, "Results:", this.leftPos + 8, resultY - 10, 0x404040, false);
		for (int i = 0; i < 9; i++) {
			int sx = this.leftPos + 7 + i * 18;
			graphics.fill(sx, resultY, sx + 18, resultY + 18, 0xFF373737);
			graphics.fill(sx + 1, resultY + 1, sx + 17, resultY + 17, 0xFFB8B800);
		}

		// Player inventory backgrounds
		int playerY = resultY + 24;
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				int sx = this.leftPos + 7 + col * 18;
				int sy = playerY + row * 18;
				graphics.fill(sx, sy, sx + 18, sy + 18, 0xFF373737);
				graphics.fill(sx + 1, sy + 1, sx + 17, sy + 17, 0xFF8B8B8B);
			}
		}
		for (int col = 0; col < 9; col++) {
			int sx = this.leftPos + 7 + col * 18;
			int sy = playerY + 58;
			graphics.fill(sx, sy, sx + 18, sy + 18, 0xFF373737);
			graphics.fill(sx + 1, sy + 1, sx + 17, sy + 17, 0xFF8B8B8B);
		}

		// Title
		graphics.drawString(font, "Pattern Provider", this.leftPos + 8, this.topPos + 6, 0x404040, false);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, partialTick);
		renderTooltip(graphics, mouseX, mouseY);
	}
}
