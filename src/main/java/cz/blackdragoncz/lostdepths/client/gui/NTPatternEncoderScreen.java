package cz.blackdragoncz.lostdepths.client.gui;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.network.storage.NTPatternEncodePacket;
import cz.blackdragoncz.lostdepths.network.storage.NTPatternModeTogglePacket;
import cz.blackdragoncz.lostdepths.world.inventory.NTPatternEncoderMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class NTPatternEncoderScreen extends AbstractContainerScreen<NTPatternEncoderMenu> {

	private Button encodeButton;
	private Button modeButton;

	public NTPatternEncoderScreen(NTPatternEncoderMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
		this.imageWidth = 176;
		this.imageHeight = 170;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	protected void init() {
		super.init();

		// Encode button
		encodeButton = addRenderableWidget(Button.builder(Component.literal("Encode"), btn -> {
			LostdepthsMod.PACKET_HANDLER.sendToServer(new NTPatternEncodePacket());
		}).bounds(this.leftPos + 68, this.topPos + 56, 44, 14).build());

		// Mode toggle button
		modeButton = addRenderableWidget(Button.builder(Component.literal("Mode"), btn -> {
			LostdepthsMod.PACKET_HANDLER.sendToServer(new NTPatternModeTogglePacket());
		}).bounds(this.leftPos + 68, this.topPos + 17, 44, 14).build());
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		graphics.fill(this.leftPos, this.topPos, this.leftPos + imageWidth, this.topPos + imageHeight, 0xFFC6C6C6);

		// Crafting/input grid (3x3)
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				int sx = this.leftPos + 7 + col * 18;
				int sy = this.topPos + 17 + row * 18;
				graphics.fill(sx, sy, sx + 18, sy + 18, 0xFF373737);
				graphics.fill(sx + 1, sy + 1, sx + 17, sy + 17, 0xFF8B8B8B);
			}
		}

		// Blank pattern slot
		int bx = this.leftPos + 115;
		int by = this.topPos + 35;
		graphics.fill(bx, by, bx + 18, by + 18, 0xFF373737);
		graphics.fill(bx + 1, by + 1, bx + 17, by + 17, 0xFF6060B0);

		// Output slot
		int ox = this.leftPos + 151;
		int oy = this.topPos + 35;
		graphics.fill(ox, oy, ox + 18, oy + 18, 0xFF373737);
		graphics.fill(ox + 1, oy + 1, ox + 17, oy + 17, 0xFFB8B800);

		// Arrow
		graphics.drawString(font, "\u2192", this.leftPos + 137, this.topPos + 39, 0x404040, false);

		// Labels
		graphics.drawString(font, "Pattern Encoder", this.leftPos + 8, this.topPos + 6, 0x404040, false);
		graphics.drawString(font, "Blank", this.leftPos + 112, this.topPos + 24, 0x404040, false);

		// Processing output slots (shown only in processing mode)
		if (menu.getEncoder().isProcessingMode()) {
			graphics.drawString(font, "Output:", this.leftPos + 114, this.topPos + 58, 0x404040, false);
			// Processing outputs are not real slots in current menu — TODO: add them
		}

		// Player inventory backgrounds
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				int sx = this.leftPos + 7 + col * 18;
				int sy = this.topPos + 89 + row * 18;
				graphics.fill(sx, sy, sx + 18, sy + 18, 0xFF373737);
				graphics.fill(sx + 1, sy + 1, sx + 17, sy + 17, 0xFF8B8B8B);
			}
		}
		for (int col = 0; col < 9; col++) {
			int sx = this.leftPos + 7 + col * 18;
			int sy = this.topPos + 147;
			graphics.fill(sx, sy, sx + 18, sy + 18, 0xFF373737);
			graphics.fill(sx + 1, sy + 1, sx + 17, sy + 17, 0xFF8B8B8B);
		}

		// Mode indicator
		String mode = menu.getEncoder().isProcessingMode() ? "§dProcessing" : "§aCrafting";
		graphics.drawString(font, mode, this.leftPos + 68, this.topPos + 34, 0x404040, false);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, partialTick);
		renderTooltip(graphics, mouseX, mouseY);
	}
}
