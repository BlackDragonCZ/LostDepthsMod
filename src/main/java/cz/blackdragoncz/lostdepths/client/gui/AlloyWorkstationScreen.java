package cz.blackdragoncz.lostdepths.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;

import cz.blackdragoncz.lostdepths.world.inventory.AlloyWorkstationMenu;

import com.mojang.blaze3d.systems.RenderSystem;

public class AlloyWorkstationScreen extends AbstractWorkstationScreen<AlloyWorkstationMenu> {

	private static final ResourceLocation texture = new ResourceLocation("lostdepths:textures/screens/wsgui_2.png");

	public AlloyWorkstationScreen(AlloyWorkstationMenu container, Inventory inventory, Component text) {
		super(container, inventory, text, texture);
		this.imageWidth = 176;
		this.imageHeight = 206;
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		super.renderLabels(guiGraphics, mouseX, mouseY);
		guiGraphics.drawString(this.font, Component.translatable("gui.lostdepths.wsgui_2.label_alloy_workbench"), 6, 9, -16750849, false);
	}
}
