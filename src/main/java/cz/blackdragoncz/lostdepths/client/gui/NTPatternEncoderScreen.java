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
		NTGuiTheme.background(graphics, leftPos, topPos, imageWidth, imageHeight);

		// Crafting/input grid (3x3)
		NTGuiTheme.slotGrid(graphics, leftPos, topPos, 8, 18, 3, 3);

		// Blank pattern slot
		NTGuiTheme.slot(graphics, leftPos, topPos, 116, 36);
		NTGuiTheme.tint(graphics, leftPos, topPos, 116, 36, NTGuiTheme.TINT_PATTERN);

		// Output slot
		NTGuiTheme.slot(graphics, leftPos, topPos, 152, 36);
		NTGuiTheme.tint(graphics, leftPos, topPos, 152, 36, NTGuiTheme.TINT_OUTPUT);

		// Arrow
		graphics.drawString(font, "→", this.leftPos + 137, this.topPos + 39, NTGuiTheme.BODY, false);

		// Labels
		graphics.drawString(font, "Pattern Encoder", this.leftPos + 8, this.topPos + 6, NTGuiTheme.HEADING, false);
		graphics.drawString(font, "Blank", this.leftPos + 112, this.topPos + 24, NTGuiTheme.BODY, false);

		// Processing output slots (real slots 11-13, shown only in processing mode)
		if (menu.getEncoder().isProcessingMode()) {
			graphics.drawString(font, "Output:", this.leftPos + 114, this.topPos + 58, NTGuiTheme.BODY, false);
			int outs = menu.getEncoder().getProcessingOutputs().getSlots();
			for (int i = 0; i < outs; i++) {
				NTGuiTheme.slot(graphics, leftPos, topPos, 116 + i * 18, 58);
				NTGuiTheme.tint(graphics, leftPos, topPos, 116 + i * 18, 58, NTGuiTheme.TINT_OUTPUT);
			}
		}

		// Player inventory
		NTGuiTheme.playerInventory(graphics, leftPos, topPos, 8, 90);

		// Mode indicator
		String mode = menu.getEncoder().isProcessingMode() ? "§dProcessing" : "§aCrafting";
		graphics.drawString(font, mode, this.leftPos + 68, this.topPos + 34, NTGuiTheme.BODY, false);
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
		// Titles drawn in renderBg (custom positions).
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, partialTick);
		renderTooltip(graphics, mouseX, mouseY);
	}
}
