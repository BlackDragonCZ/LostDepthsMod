package cz.blackdragoncz.lostdepths.client.gui;

import cz.blackdragoncz.lostdepths.world.inventory.GalacticWorkstationMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;

import com.mojang.blaze3d.systems.RenderSystem;

public class GalacticWorkstationScreen extends AbstractWorkstationScreen<GalacticWorkstationMenu> {

	private static final ResourceLocation texture = new ResourceLocation("lostdepths:textures/screens/galactic_workstation.png");

	public GalacticWorkstationScreen(GalacticWorkstationMenu container, Inventory inventory, Component text) {
		super(container, inventory, text, texture);
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		super.renderLabels(guiGraphics, mouseX, mouseY);
		guiGraphics.drawString(this.font, Component.translatable("gui.lostdepths.wsgui_1.label_workstation_tier_1"), 6, 7, -16750900, false);
	}
}
