package cz.blackdragoncz.lostdepths.client.gui;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.network.storage.NTGridClickPacket;
import cz.blackdragoncz.lostdepths.network.storage.NTGridInsertPacket;
import cz.blackdragoncz.lostdepths.storage.data.CrystalInventory;
import cz.blackdragoncz.lostdepths.world.inventory.NTCraftingTerminalMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class NTCraftingTerminalScreen extends AbstractContainerScreen<NTCraftingTerminalMenu> {

	private EditBox searchBox;

	private static final int GRID_X = 8;
	private static final int GRID_Y = 18;
	private static final int SLOT_SIZE = 18;
	private static final int COLS = NTCraftingTerminalMenu.GRID_COLS;
	private static final int ROWS = NTCraftingTerminalMenu.GRID_ROWS;

	public NTCraftingTerminalScreen(NTCraftingTerminalMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
		this.imageWidth = 270;
		this.imageHeight = 222;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	protected void init() {
		super.init();
		searchBox = new EditBox(this.font, this.leftPos + 82, this.topPos + 6, 86, 10, Component.literal("Search"));
		searchBox.setMaxLength(50);
		searchBox.setBordered(true);
		searchBox.setTextColor(0xFFFFFF);
		searchBox.setResponder(text -> menu.setSearchFilter(text));
		addRenderableWidget(searchBox);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		// Main background
		graphics.fill(this.leftPos, this.topPos, this.leftPos + imageWidth, this.topPos + imageHeight, 0xFFC6C6C6);

		// Virtual grid background
		graphics.fill(this.leftPos + GRID_X - 1, this.topPos + GRID_Y - 1,
				this.leftPos + GRID_X + COLS * SLOT_SIZE + 1, this.topPos + GRID_Y + ROWS * SLOT_SIZE + 1,
				0xFF373737);
		graphics.fill(this.leftPos + GRID_X, this.topPos + GRID_Y,
				this.leftPos + GRID_X + COLS * SLOT_SIZE, this.topPos + GRID_Y + ROWS * SLOT_SIZE,
				0xFF8B8B8B);

		// Grid lines
		for (int row = 0; row <= ROWS; row++) {
			int y = this.topPos + GRID_Y + row * SLOT_SIZE;
			graphics.fill(this.leftPos + GRID_X, y, this.leftPos + GRID_X + COLS * SLOT_SIZE, y + 1, 0xFF373737);
		}
		for (int col = 0; col <= COLS; col++) {
			int x = this.leftPos + GRID_X + col * SLOT_SIZE;
			graphics.fill(x, this.topPos + GRID_Y, x + 1, this.topPos + GRID_Y + ROWS * SLOT_SIZE, 0xFF373737);
		}

		// Crafting grid label
		graphics.drawString(font, "Crafting", this.leftPos + 188, this.topPos + 8, 0x404040, false);

		// Crafting grid slot backgrounds (3x3)
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				int sx = this.leftPos + 187 + col * 18;
				int sy = this.topPos + 17 + row * 18;
				graphics.fill(sx, sy, sx + 18, sy + 18, 0xFF373737);
				graphics.fill(sx + 1, sy + 1, sx + 17, sy + 17, 0xFF8B8B8B);
			}
		}

		// Result slot background
		int rx = this.leftPos + 243;
		int ry = this.topPos + 35;
		graphics.fill(rx, ry, rx + 18, ry + 18, 0xFF373737);
		graphics.fill(rx + 1, ry + 1, rx + 17, ry + 17, 0xFFB8B800);

		// Arrow between grid and result
		graphics.drawString(font, "→", this.leftPos + 247, this.topPos + 24, 0x404040, false);

		// Player inventory backgrounds
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				int sx = this.leftPos + 7 + col * 18;
				int sy = this.topPos + 139 + row * 18;
				graphics.fill(sx, sy, sx + 18, sy + 18, 0xFF373737);
				graphics.fill(sx + 1, sy + 1, sx + 17, sy + 17, 0xFF8B8B8B);
			}
		}
		for (int col = 0; col < 9; col++) {
			int sx = this.leftPos + 7 + col * 18;
			int sy = this.topPos + 197;
			graphics.fill(sx, sy, sx + 18, sy + 18, 0xFF373737);
			graphics.fill(sx + 1, sy + 1, sx + 17, sy + 17, 0xFF8B8B8B);
		}

		// Render stored items in virtual grid
		List<CrystalInventory.StoredItem> filtered = menu.getFilteredItems();
		int startIdx = menu.getScrollOffset() * COLS;

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				int idx = startIdx + row * COLS + col;
				int x = this.leftPos + GRID_X + col * SLOT_SIZE + 1;
				int y = this.topPos + GRID_Y + row * SLOT_SIZE + 1;

				if (idx < filtered.size()) {
					CrystalInventory.StoredItem item = filtered.get(idx);
					ItemStack display = item.template.copy();
					display.setCount(1);
					graphics.renderItem(display, x, y);

					String countStr = formatCount(item.count);
					graphics.pose().pushPose();
					graphics.pose().translate(0, 0, 200);
					graphics.drawString(font, countStr, x + 17 - font.width(countStr), y + 9, 0xFFFFFF, true);
					graphics.pose().popPose();

					if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
						graphics.fill(x, y, x + 16, y + 16, 0x80FFFFFF);
					}
				}
			}
		}

		// Title
		graphics.drawString(font, "NuroTech Crafting Terminal", this.leftPos + 8, this.topPos + 6, 0x404040, false);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, partialTick);
		renderTooltip(graphics, mouseX, mouseY);

		// Tooltip for hovered virtual grid item
		List<CrystalInventory.StoredItem> filtered = menu.getFilteredItems();
		int startIdx = menu.getScrollOffset() * COLS;
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				int idx = startIdx + row * COLS + col;
				int x = this.leftPos + GRID_X + col * SLOT_SIZE + 1;
				int y = this.topPos + GRID_Y + row * SLOT_SIZE + 1;
				if (idx < filtered.size() && mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
					CrystalInventory.StoredItem item = filtered.get(idx);
					ItemStack tooltipStack = item.template.copy();
					tooltipStack.setCount(item.count);
					graphics.renderTooltip(font, tooltipStack, mouseX, mouseY);
				}
			}
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		int gridLeft = this.leftPos + GRID_X + 1;
		int gridTop = this.topPos + GRID_Y + 1;
		int gridRight = gridLeft + COLS * SLOT_SIZE - 2;
		int gridBottom = gridTop + ROWS * SLOT_SIZE - 2;

		if (mouseX >= gridLeft && mouseX < gridRight && mouseY >= gridTop && mouseY < gridBottom) {
			int col = (int) (mouseX - gridLeft) / SLOT_SIZE;
			int row = (int) (mouseY - gridTop) / SLOT_SIZE;
			int gridIndex = row * COLS + col;

			List<CrystalInventory.StoredItem> filtered = menu.getFilteredItems();
			int actualIndex = gridIndex + menu.getScrollOffset() * COLS;

			if (actualIndex < filtered.size()) {
				LostdepthsMod.PACKET_HANDLER.sendToServer(new NTGridClickPacket(gridIndex, button));
			} else {
				LostdepthsMod.PACKET_HANDLER.sendToServer(new NTGridInsertPacket(button));
			}
			return true;
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		menu.setScrollOffset(menu.getScrollOffset() - (int) Math.signum(delta));
		return true;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (searchBox.isFocused()) {
			if (keyCode == 256) {
				searchBox.setFocused(false);
				return true;
			}
			return searchBox.keyPressed(keyCode, scanCode, modifiers);
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char c, int modifiers) {
		if (searchBox.isFocused()) {
			return searchBox.charTyped(c, modifiers);
		}
		return super.charTyped(c, modifiers);
	}

	private static String formatCount(int count) {
		if (count >= 1_000_000) return (count / 1_000_000) + "M";
		if (count >= 10_000) return (count / 1_000) + "K";
		if (count >= 1_000) return String.format("%.1fK", count / 1000.0);
		return String.valueOf(count);
	}
}
