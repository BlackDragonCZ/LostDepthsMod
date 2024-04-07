package cz.blackdragoncz.lostdepths.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;

import cz.blackdragoncz.lostdepths.world.inventory.ModuleCreatorGUIMenu;

import com.mojang.blaze3d.systems.RenderSystem;

public class ModuleCreatorGUIScreen extends AbstractContainerScreen<ModuleCreatorGUIMenu> {
	private final static HashMap<String, Object> guistate = ModuleCreatorGUIMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	ImageButton imagebutton_recipebook_arrow;

	public ModuleCreatorGUIScreen(ModuleCreatorGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 186;
	}

	private static final ResourceLocation texture = new ResourceLocation("lostdepths:textures/screens/module_creator_gui.png");

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
		guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

		guiGraphics.blit(new ResourceLocation("lostdepths:textures/screens/mcgui.png"), this.leftPos + 0, this.topPos + 0, 0, 0, 176, 186, 176, 186);

		RenderSystem.disableBlend();
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

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, Component.translatable("gui.lostdepths.module_creator_gui.label_module_creatorssssss"), 51, 8, -12434878, false);
	}

	@Override
	public void onClose() {
		super.onClose();
	}

	@Override
	public void init() {
		super.init();
		imagebutton_recipebook_arrow = new ImageButton(this.leftPos + 96, this.topPos + 44, 24, 17, 0, 0, 17, new ResourceLocation("lostdepths:textures/screens/atlas/imagebutton_recipebook_arrow.png"), 24, 34, e -> {
		});
		guistate.put("button:imagebutton_recipebook_arrow", imagebutton_recipebook_arrow);
		this.addRenderableWidget(imagebutton_recipebook_arrow);
	}
}
