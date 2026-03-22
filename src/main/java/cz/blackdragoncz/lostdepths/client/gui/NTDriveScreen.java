package cz.blackdragoncz.lostdepths.client.gui;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.world.inventory.NTDriveMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class NTDriveScreen extends AbstractContainerScreen<NTDriveMenu> {
	private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");

	public NTDriveScreen(NTDriveMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
		this.imageHeight = 166;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		// Draw a simple background — using generic chest texture as placeholder
		graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

		// Draw slot highlights for crystal slots
		int slotCount = menu.getSlotCount();
		int startX = (176 - slotCount * 18) / 2;
		for (int i = 0; i < slotCount; i++) {
			graphics.fill(
					this.leftPos + startX + i * 18,
					this.topPos + 34,
					this.leftPos + startX + 18 + i * 18,
					this.topPos + 52,
					0x408B5CF6 // purple tint for crystal slots
			);
		}
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, partialTick);
		renderTooltip(graphics, mouseX, mouseY);
	}
}
