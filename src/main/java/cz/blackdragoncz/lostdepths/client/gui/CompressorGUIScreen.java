package cz.blackdragoncz.lostdepths.client.gui;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.block.entity.AbstractCompressorBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;

import java.text.DecimalFormat;
import java.util.HashMap;

import cz.blackdragoncz.lostdepths.world.inventory.CompressorGUIMenu;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraftforge.energy.EnergyStorage;

public class CompressorGUIScreen extends AbstractContainerScreen<CompressorGUIMenu> {
	private final static HashMap<String, Object> guistate = CompressorGUIMenu.guistate;

	private final AbstractCompressorBlockEntity blockEntity;

	public CompressorGUIScreen(CompressorGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.imageWidth = 176;
		this.imageHeight = 166;
		this.blockEntity = container.boundBlockEntity;
	}

	private static final ResourceLocation BG_RES = LostdepthsMod.rl("textures/screens/compression_table.png");
	private static final ResourceLocation FURNACE_RES = new ResourceLocation("textures/gui/container/furnace.png");
	private static final ResourceLocation JEI_RES = LostdepthsMod.rl("textures/gui/jei_handler.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(BG_RES, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

		float percent = ((float)blockEntity.getCurrentCraftTime() / (float)blockEntity.getCraftTickTime());
		RenderSystem.disableBlend();

		guiGraphics.blit(FURNACE_RES, this.leftPos + 77, this.topPos + 35, 177, 14, Math.round(20.0f * percent), 17, 254, 254);

		EnergyStorage energyStorage = blockEntity.getEnergyStorage();
		if (energyStorage != null) {
			float energyValue = energyStorage.getEnergyStored() / (float)energyStorage.getMaxEnergyStored();
			guiGraphics.blit(JEI_RES, this.leftPos + 148, this.topPos + 22, 64, 144, 14, 42, 256, 256);
			guiGraphics.enableScissor(this.leftPos + 148, this.topPos + 22 + 42 - Math.round(42 * energyValue), this.leftPos + 148 + 14, this.topPos + 22 + 42);
			guiGraphics.blit(JEI_RES, this.leftPos + 148, this.topPos + 22, 64 + 14 + 2, 144, 14, 42, 256, 256);
			guiGraphics.disableScissor();
		}
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	public void containerTick() {
		super.containerTick();
	}

	private static final DecimalFormat energyStringFormat = new DecimalFormat("#,###"); // #,###

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, "§4" + blockEntity.getDisplayName().getString(), 4, 5, -6750208, false);

		float percent = ((float)blockEntity.getCurrentCraftTime() / (float)blockEntity.getCraftTickTime()) * 100.0F;

		if (mouseX >= this.leftPos + 77 && mouseX < this.leftPos + 77 + 24 && mouseY >= this.topPos + 35 && mouseY < this.topPos + 35 + 17) {
			String val = "Progress: " + new java.text.DecimalFormat("##.#").format(percent) + "%";
			guiGraphics.renderTooltip(this.font, Component.literal(val), mouseX - this.leftPos, mouseY - this.topPos);
		}

		EnergyStorage energyStorage = blockEntity.getEnergyStorage();
		if (energyStorage != null) {
			if (mouseX >= this.leftPos + 148 && mouseX < this.leftPos + 148 + 14 && mouseY >= this.topPos + 22 && mouseY < this.topPos + 22 + 42) {
                String builder = energyStringFormat.format(energyStorage.getEnergyStored()) +
                        "FE / " +
                        energyStringFormat.format(energyStorage.getMaxEnergyStored()) +
                        "FE";

				if (blockEntity.haveEnergy()) {
					guiGraphics.setColor(0, 1, 0, 1);
				}
				else {
					guiGraphics.setColor(1, 0, 0, 1);
				}

				guiGraphics.renderTooltip(this.font, Component.literal(builder), mouseX - this.leftPos, mouseY - this.topPos);
			}
		}
	}

	@Override
	public void onClose() {
		super.onClose();
	}

	@Override
	public void init() {
		super.init();
	}
}
