package cz.blackdragoncz.lostdepths.client.gui;

import com.google.common.collect.ImmutableList;
import cz.blackdragoncz.lostdepths.LostdepthsMod;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class InfusedBookViewScreen extends Screen {
	private static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(LostdepthsMod.MODID, "textures/gui/guidebook/books_drago.png");
	private static final int TEXTURE_WIDTH = 512;
	private static final int TEXTURE_HEIGHT = 256;

	// Page dimensions (each page is 138px of the 272px-wide book spread)
	private static final int BOOK_WIDTH = 138;
	private static final int BOOK_HEIGHT = 180;
	private static final int BOOK_TOP = 15;
	private static final int RIGHT_PAGE_U = 134;

	private static final int LEFT_TEXT_OFFSET_X = 22;
	private static final int RIGHT_TEXT_OFFSET_X = 14;
	private static final int TEXT_OFFSET_Y = 16;
	private static final int TEXT_WIDTH = 96;
	private static final int TEXT_HEIGHT = 130;
	private static final int MAX_LINES = TEXT_HEIGHT / 9;

	// Use vanilla book arrow sprites (from book.png, 256x256)
	private static final ResourceLocation VANILLA_BOOK_TEXTURE = new ResourceLocation("textures/gui/book.png");
	private static final int ARROW_W = 23;
	private static final int ARROW_H = 13;

	private final List<String> pages;
	private int currentPage;
	private List<FormattedCharSequence> cachedPageLines = Collections.emptyList();
	private int cachedPageIndex = -1;
	private Component pageMsg = CommonComponents.EMPTY;
	private Button forwardButton;
	private Button backButton;

	public InfusedBookViewScreen(ItemStack stack) {
		super(GameNarrator.NO_TITLE);
		this.pages = loadPages(stack);
	}

	private static List<String> loadPages(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		if (tag == null) return ImmutableList.of();
		ListTag pagesList = tag.getList("pages", 8);
		ImmutableList.Builder<String> builder = ImmutableList.builder();
		for (int i = 0; i < pagesList.size(); i++) {
			builder.add(pagesList.getString(i));
		}
		return builder.build();
	}

	private boolean isRightPage() {
		return this.currentPage % 2 == 1;
	}

	private int getTextOffsetX() {
		return isRightPage() ? RIGHT_TEXT_OFFSET_X : LEFT_TEXT_OFFSET_X;
	}

	private int getBookU() {
		return isRightPage() ? RIGHT_PAGE_U : 0;
	}

	private int getBookLeft() {
		return (this.width - BOOK_WIDTH) / 2;
	}

	@Override
	protected void init() {
		int bookLeft = getBookLeft();
		int buttonY = BOOK_TOP + BOOK_HEIGHT + 6;

		this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, btn -> this.onClose())
				.bounds(bookLeft, buttonY, BOOK_WIDTH, 20).build());

		int arrowY = BOOK_TOP + BOOK_HEIGHT - 18;
		this.backButton = this.addRenderableWidget(new ArrowButton(bookLeft + 8, arrowY, false, btn -> this.pageBack()));
		this.forwardButton = this.addRenderableWidget(new ArrowButton(bookLeft + BOOK_WIDTH - ARROW_W - 12, arrowY, true, btn -> this.pageForward()));

		this.updateButtonVisibility();
	}

	private void pageForward() {
		if (this.currentPage < this.pages.size() - 1) {
			++this.currentPage;
			this.cachedPageIndex = -1;
		}
		this.updateButtonVisibility();
	}

	private void pageBack() {
		if (this.currentPage > 0) {
			--this.currentPage;
			this.cachedPageIndex = -1;
		}
		this.updateButtonVisibility();
	}

	private void updateButtonVisibility() {
		this.forwardButton.visible = this.currentPage < this.pages.size() - 1;
		this.backButton.visible = this.currentPage > 0;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (super.keyPressed(keyCode, scanCode, modifiers)) return true;
		switch (keyCode) {
			case 266: this.backButton.onPress(); return true;
			case 267: this.forwardButton.onPress(); return true;
			default: return false;
		}
	}

	@Override
	public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(g);
		int bookLeft = getBookLeft();

		// Render left or right page depending on page index
		g.blit(BOOK_TEXTURE, bookLeft, BOOK_TOP, getBookU(), 0, BOOK_WIDTH, BOOK_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);

		if (this.cachedPageIndex != this.currentPage) {
			this.cachedPageLines = this.getPageLines(this.currentPage);
			this.pageMsg = Component.translatable("book.pageIndicator", this.currentPage + 1, Math.max(this.pages.size(), 1));
			this.cachedPageIndex = this.currentPage;
		}

		int textX = bookLeft + getTextOffsetX();
		int textY = BOOK_TOP + TEXT_OFFSET_Y;

		// Page indicator
		int msgWidth = this.font.width(this.pageMsg);
		g.drawString(this.font, this.pageMsg, textX + (TEXT_WIDTH - msgWidth) / 2, textY, 0x404040, false);

		// Page content
		int lineCount = Math.min(MAX_LINES, this.cachedPageLines.size());
		for (int i = 0; i < lineCount; i++) {
			g.drawString(this.font, this.cachedPageLines.get(i), textX, textY + 12 + i * 9, 0, false);
		}

		super.render(g, mouseX, mouseY, partialTick);
	}

	private List<FormattedCharSequence> getPageLines(int pageIndex) {
		if (pageIndex < 0 || pageIndex >= this.pages.size()) return Collections.emptyList();
		String raw = this.pages.get(pageIndex);
		FormattedText text;
		try {
			text = Component.Serializer.fromJson(raw);
			if (text == null) text = FormattedText.of(raw);
		} catch (Exception e) {
			text = FormattedText.of(raw);
		}
		return this.font.split(text, TEXT_WIDTH);
	}

	// Custom arrow button that renders from the book texture
	@OnlyIn(Dist.CLIENT)
	static class ArrowButton extends Button {
		private final boolean isForward;

		ArrowButton(int x, int y, boolean forward, OnPress onPress) {
			super(x, y, ARROW_W, ARROW_H, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
			this.isForward = forward;
		}

		@Override
		public void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
			int u = this.isHoveredOrFocused() ? 23 : 0;
			int v = this.isForward ? 192 : 205;
			g.blit(VANILLA_BOOK_TEXTURE, this.getX(), this.getY(), u, v, ARROW_W, ARROW_H);
		}

		@Override
		public void playDownSound(net.minecraft.client.sounds.SoundManager manager) {
			manager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
		}
	}
}
