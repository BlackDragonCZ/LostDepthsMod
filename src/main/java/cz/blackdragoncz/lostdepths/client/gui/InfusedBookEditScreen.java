package cz.blackdragoncz.lostdepths.client.gui;

import com.google.common.collect.Lists;
import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.network.SaveInfusedBookMessage;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class InfusedBookEditScreen extends Screen {
	private static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(LostdepthsMod.MODID, "textures/gui/guidebook/books_drago.png");
	private static final int TEXTURE_WIDTH = 512;
	private static final int TEXTURE_HEIGHT = 256;

	// Page dimensions (each page is 138px of the 272px-wide book spread)
	private static final int BOOK_WIDTH = 138;
	private static final int BOOK_HEIGHT = 180;
	// Right page starts at this U offset in the texture
	private static final int RIGHT_PAGE_U = 134;

	// Text area relative to book top-left corner
	private static final int LEFT_TEXT_OFFSET_X = 22;
	private static final int RIGHT_TEXT_OFFSET_X = 14;
	private static final int TEXT_OFFSET_Y = 16;
	private static final int PAGE_TEXT_WIDTH = 96;
	private static final int PAGE_TEXT_HEIGHT = 130;

	// Use vanilla book arrow sprites (from book.png, 256x256)
	private static final ResourceLocation VANILLA_BOOK_TEXTURE = new ResourceLocation("textures/gui/book.png");
	private static final int ARROW_W = 23;
	private static final int ARROW_H = 13;

	private static final Component EDIT_TITLE_LABEL = Component.translatable("book.editTitle");
	private static final Component FINALIZE_WARNING_LABEL = Component.translatable("book.finalizeWarning");
	private static final FormattedCharSequence BLACK_CURSOR = FormattedCharSequence.forward("_", Style.EMPTY.withColor(ChatFormatting.BLACK));
	private static final FormattedCharSequence GRAY_CURSOR = FormattedCharSequence.forward("_", Style.EMPTY.withColor(ChatFormatting.GRAY));

	private final Player owner;
	private final ItemStack book;
	private final InteractionHand hand;
	private boolean isModified;
	private boolean isSigning;
	private int frameTick;
	private int currentPage;
	private final List<String> pages = Lists.newArrayList();
	private String title = "";

	private final TextFieldHelper pageEdit = new TextFieldHelper(
			this::getCurrentPageText, this::setCurrentPageText,
			this::getClipboard, this::setClipboard,
			s -> s.length() < 1024 && this.font.wordWrapHeight(s, PAGE_TEXT_WIDTH) <= PAGE_TEXT_HEIGHT
	);
	private final TextFieldHelper titleEdit = new TextFieldHelper(
			() -> this.title, s -> this.title = s,
			this::getClipboard, this::setClipboard,
			s -> s.length() < 16
	);

	private long lastClickTime;
	private int lastIndex = -1;
	private Button forwardButton;
	private Button backButton;
	private Button doneButton;
	private Button signButton;
	private Button finalizeButton;
	private Button cancelButton;
	private Checkbox contractCheckbox;
	private DisplayCache displayCache = DisplayCache.EMPTY;
	private Component pageMsg = CommonComponents.EMPTY;
	private final Component ownerText;

	public InfusedBookEditScreen(Player player, ItemStack book, InteractionHand hand) {
		super(GameNarrator.NO_TITLE);
		this.owner = player;
		this.book = book;
		this.hand = hand;
		CompoundTag tag = book.getTag();
		if (tag != null) {
			ListTag pagesList = tag.getList("pages", 8);
			for (int i = 0; i < pagesList.size(); i++) {
				this.pages.add(pagesList.getString(i));
			}
		}
		if (this.pages.isEmpty()) {
			this.pages.add("");
		}
		this.ownerText = Component.translatable("book.byAuthor", player.getName()).withStyle(ChatFormatting.DARK_GRAY);
	}

	private void setClipboard(String text) {
		if (this.minecraft != null) TextFieldHelper.setClipboardContents(this.minecraft, text);
	}

	private String getClipboard() {
		return this.minecraft != null ? TextFieldHelper.getClipboardContents(this.minecraft) : "";
	}

	private int getNumPages() {
		return this.pages.size();
	}

	@Override
	public void tick() {
		super.tick();
		++this.frameTick;
	}

	private int getBookLeft() {
		return (this.width - BOOK_WIDTH) / 2;
	}

	private static final int BOOK_TOP = 15;

	private boolean isRightPage() {
		return this.currentPage % 2 == 1;
	}

	private int getTextOffsetX() {
		return isRightPage() ? RIGHT_TEXT_OFFSET_X : LEFT_TEXT_OFFSET_X;
	}

	private int getBookU() {
		return isRightPage() ? RIGHT_PAGE_U : 0;
	}

	@Override
	protected void init() {
		this.clearDisplayCache();
		int bookLeft = getBookLeft();
		int buttonY = BOOK_TOP + BOOK_HEIGHT + 6;

		this.signButton = this.addRenderableWidget(Button.builder(Component.translatable("book.signButton"), btn -> {
			this.isSigning = true;
			this.updateButtonVisibility();
		}).bounds(bookLeft, buttonY, BOOK_WIDTH / 2 - 1, 20).build());

		this.doneButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, btn -> {
			this.minecraft.setScreen(null);
			this.saveChanges(false);
		}).bounds(bookLeft + BOOK_WIDTH / 2 + 1, buttonY, BOOK_WIDTH / 2 - 1, 20).build());

		this.finalizeButton = this.addRenderableWidget(Button.builder(Component.translatable("book.finalizeButton"), btn -> {
			if (this.isSigning) {
				this.saveChanges(true);
				this.minecraft.setScreen(null);
			}
		}).bounds(bookLeft, buttonY, BOOK_WIDTH / 2 - 1, 20).build());

		this.cancelButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, btn -> {
			if (this.isSigning) this.isSigning = false;
			this.updateButtonVisibility();
		}).bounds(bookLeft + BOOK_WIDTH / 2 + 1, buttonY, BOOK_WIDTH / 2 - 1, 20).build());

		// Contract checkbox - shown during signing
		this.contractCheckbox = this.addRenderableWidget(
				new Checkbox(bookLeft, buttonY + 24, BOOK_WIDTH, 20, Component.literal("Allow contract signature"), false)
		);

		// Page navigation - custom arrow buttons rendered from the book texture
		int arrowY = BOOK_TOP + BOOK_HEIGHT - 18;
		this.backButton = this.addRenderableWidget(new ArrowButton(bookLeft + 8, arrowY, false, btn -> this.pageBack()));
		this.forwardButton = this.addRenderableWidget(new ArrowButton(bookLeft + BOOK_WIDTH - ARROW_W - 12, arrowY, true, btn -> this.pageForward()));

		this.updateButtonVisibility();
	}

	private void pageBack() {
		if (this.currentPage > 0) --this.currentPage;
		this.updateButtonVisibility();
		this.clearDisplayCacheAfterPageChange();
	}

	private void pageForward() {
		if (this.currentPage < this.getNumPages() - 1) {
			++this.currentPage;
		} else {
			this.appendPageToBook();
			if (this.currentPage < this.getNumPages() - 1) ++this.currentPage;
		}
		this.updateButtonVisibility();
		this.clearDisplayCacheAfterPageChange();
	}

	private void updateButtonVisibility() {
		this.backButton.visible = !this.isSigning && this.currentPage > 0;
		this.forwardButton.visible = !this.isSigning;
		this.doneButton.visible = !this.isSigning;
		this.signButton.visible = !this.isSigning;
		this.cancelButton.visible = this.isSigning;
		this.finalizeButton.visible = this.isSigning;
		this.finalizeButton.active = !this.title.trim().isEmpty();
		this.contractCheckbox.visible = this.isSigning;
	}

	private void eraseEmptyTrailingPages() {
		ListIterator<String> it = this.pages.listIterator(this.pages.size());
		while (it.hasPrevious() && it.previous().isEmpty()) {
			it.remove();
		}
	}

	private void saveChanges(boolean signing) {
		if (this.isModified) {
			this.eraseEmptyTrailingPages();
			Optional<String> titleOpt = signing ? Optional.of(this.title.trim()) : Optional.empty();
			boolean contractAllowed = signing && this.contractCheckbox.selected();
			LostdepthsMod.PACKET_HANDLER.sendToServer(
					new SaveInfusedBookMessage(this.hand, List.copyOf(this.pages), titleOpt, contractAllowed)
			);
		}
	}

	private void appendPageToBook() {
		if (this.getNumPages() < 100) {
			this.pages.add("");
			this.isModified = true;
		}
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (super.keyPressed(keyCode, scanCode, modifiers)) return true;
		if (this.isSigning) return this.titleKeyPressed(keyCode, scanCode, modifiers);
		boolean flag = this.bookKeyPressed(keyCode, scanCode, modifiers);
		if (flag) {
			this.clearDisplayCache();
			return true;
		}
		return false;
	}

	@Override
	public boolean charTyped(char c, int modifiers) {
		if (super.charTyped(c, modifiers)) return true;
		if (this.isSigning) {
			boolean flag = this.titleEdit.charTyped(c);
			if (flag) {
				this.updateButtonVisibility();
				this.isModified = true;
				return true;
			}
			return false;
		}
		if (SharedConstants.isAllowedChatCharacter(c)) {
			this.pageEdit.insertText(Character.toString(c));
			this.clearDisplayCache();
			return true;
		}
		return false;
	}

	private boolean bookKeyPressed(int keyCode, int scanCode, int modifiers) {
		if (Screen.isSelectAll(keyCode)) { this.pageEdit.selectAll(); return true; }
		if (Screen.isCopy(keyCode)) { this.pageEdit.copy(); return true; }
		if (Screen.isPaste(keyCode)) { this.pageEdit.paste(); return true; }
		if (Screen.isCut(keyCode)) { this.pageEdit.cut(); return true; }
		TextFieldHelper.CursorStep step = Screen.hasControlDown() ? TextFieldHelper.CursorStep.WORD : TextFieldHelper.CursorStep.CHARACTER;
		switch (keyCode) {
			case 257: case 335: this.pageEdit.insertText("\n"); return true;
			case 259: this.pageEdit.removeFromCursor(-1, step); return true;
			case 261: this.pageEdit.removeFromCursor(1, step); return true;
			case 262: this.pageEdit.moveBy(1, Screen.hasShiftDown(), step); return true;
			case 263: this.pageEdit.moveBy(-1, Screen.hasShiftDown(), step); return true;
			case 264: this.changeLine(1); return true;
			case 265: this.changeLine(-1); return true;
			case 266: this.backButton.onPress(); return true;
			case 267: this.forwardButton.onPress(); return true;
			case 268: this.keyHome(); return true;
			case 269: this.keyEnd(); return true;
			default: return false;
		}
	}

	private void changeLine(int dir) {
		int i = this.pageEdit.getCursorPos();
		int j = this.getDisplayCache().changeLine(i, dir);
		this.pageEdit.setCursorPos(j, Screen.hasShiftDown());
	}

	private void keyHome() {
		if (Screen.hasControlDown()) {
			this.pageEdit.setCursorToStart(Screen.hasShiftDown());
		} else {
			int i = this.pageEdit.getCursorPos();
			int j = this.getDisplayCache().findLineStart(i);
			this.pageEdit.setCursorPos(j, Screen.hasShiftDown());
		}
	}

	private void keyEnd() {
		if (Screen.hasControlDown()) {
			this.pageEdit.setCursorToEnd(Screen.hasShiftDown());
		} else {
			DisplayCache cache = this.getDisplayCache();
			int i = this.pageEdit.getCursorPos();
			int j = cache.findLineEnd(i);
			this.pageEdit.setCursorPos(j, Screen.hasShiftDown());
		}
	}

	private boolean titleKeyPressed(int keyCode, int scanCode, int modifiers) {
		switch (keyCode) {
			case 257: case 335:
				if (!this.title.isEmpty()) {
					this.saveChanges(true);
					this.minecraft.setScreen(null);
				}
				return true;
			case 259:
				this.titleEdit.removeCharsFromCursor(-1);
				this.updateButtonVisibility();
				this.isModified = true;
				return true;
			default: return false;
		}
	}

	private String getCurrentPageText() {
		return this.currentPage >= 0 && this.currentPage < this.pages.size() ? this.pages.get(this.currentPage) : "";
	}

	private void setCurrentPageText(String text) {
		if (this.currentPage >= 0 && this.currentPage < this.pages.size()) {
			this.pages.set(this.currentPage, text);
			this.isModified = true;
			this.clearDisplayCache();
		}
	}

	@Override
	public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(g);
		this.setFocused(null);
		int bookLeft = getBookLeft();

		// Render left or right page depending on page index
		g.blit(BOOK_TEXTURE, bookLeft, BOOK_TOP, getBookU(), 0, BOOK_WIDTH, BOOK_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);

		int textX = bookLeft + getTextOffsetX();
		int textY = BOOK_TOP + TEXT_OFFSET_Y;

		if (this.isSigning) {
			boolean cursorBlink = this.frameTick / 6 % 2 == 0;
			FormattedCharSequence titleWithCursor = FormattedCharSequence.composite(
					FormattedCharSequence.forward(this.title, Style.EMPTY),
					cursorBlink ? BLACK_CURSOR : GRAY_CURSOR
			);

			int labelWidth = this.font.width(EDIT_TITLE_LABEL);
			g.drawString(this.font, EDIT_TITLE_LABEL, textX + (PAGE_TEXT_WIDTH - labelWidth) / 2, textY + 10, 0, false);

			int titleWidth = this.font.width(titleWithCursor);
			g.drawString(this.font, titleWithCursor, textX + (PAGE_TEXT_WIDTH - titleWidth) / 2, textY + 26, 0, false);

			int ownerWidth = this.font.width(this.ownerText);
			g.drawString(this.font, this.ownerText, textX + (PAGE_TEXT_WIDTH - ownerWidth) / 2, textY + 38, 0, false);

			g.drawWordWrap(this.font, FINALIZE_WARNING_LABEL, textX, textY + 56, PAGE_TEXT_WIDTH, 0);
		} else {
			// Page indicator at top
			int pageNumWidth = this.font.width(this.pageMsg);
			g.drawString(this.font, this.pageMsg, textX + (PAGE_TEXT_WIDTH - pageNumWidth) / 2, textY, 0x404040, false);

			DisplayCache cache = this.getDisplayCache();
			for (LineInfo line : cache.lines) {
				g.drawString(this.font, line.asComponent, line.x, line.y, -16777216, false);
			}
			this.renderHighlight(g, cache.selection);
			this.renderCursor(g, cache.cursor, cache.cursorAtEnd);
		}

		super.render(g, mouseX, mouseY, partialTick);
	}

	private void renderCursor(GuiGraphics g, Pos2i pos, boolean atEnd) {
		if (this.frameTick / 6 % 2 == 0) {
			pos = this.convertLocalToScreen(pos);
			if (!atEnd) {
				g.fill(pos.x, pos.y - 1, pos.x + 1, pos.y + 9, -16777216);
			} else {
				g.drawString(this.font, "_", pos.x, pos.y, 0, false);
			}
		}
	}

	private void renderHighlight(GuiGraphics g, Rect2i[] rects) {
		for (Rect2i rect : rects) {
			int x0 = rect.getX();
			int y0 = rect.getY();
			g.fill(RenderType.guiTextHighlight(), x0, y0, x0 + rect.getWidth(), y0 + rect.getHeight(), -16776961);
		}
	}

	// Convert screen coordinates to local text coordinates
	private Pos2i convertScreenToLocal(Pos2i pos) {
		return new Pos2i(pos.x - getBookLeft() - getTextOffsetX(), pos.y - BOOK_TOP - TEXT_OFFSET_Y - 12);
	}

	// Convert local text coordinates to screen coordinates
	private Pos2i convertLocalToScreen(Pos2i pos) {
		return new Pos2i(pos.x + getBookLeft() + getTextOffsetX(), pos.y + BOOK_TOP + TEXT_OFFSET_Y + 12);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (super.mouseClicked(mouseX, mouseY, button)) return true;
		if (button == 0) {
			long now = net.minecraft.Util.getMillis();
			DisplayCache cache = this.getDisplayCache();
			int idx = cache.getIndexAtPosition(this.font, this.convertScreenToLocal(new Pos2i((int) mouseX, (int) mouseY)));
			if (idx >= 0) {
				if (idx == this.lastIndex && now - this.lastClickTime < 250L) {
					if (!this.pageEdit.isSelecting()) {
						this.selectWord(idx);
					} else {
						this.pageEdit.selectAll();
					}
				} else {
					this.pageEdit.setCursorPos(idx, Screen.hasShiftDown());
				}
				this.clearDisplayCache();
			}
			this.lastIndex = idx;
			this.lastClickTime = now;
		}
		return true;
	}

	private void selectWord(int index) {
		String s = this.getCurrentPageText();
		this.pageEdit.setSelectionRange(
				StringSplitter.getWordPosition(s, -1, index, false),
				StringSplitter.getWordPosition(s, 1, index, false)
		);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (super.mouseDragged(mouseX, mouseY, button, dragX, dragY)) return true;
		if (button == 0) {
			DisplayCache cache = this.getDisplayCache();
			int idx = cache.getIndexAtPosition(this.font, this.convertScreenToLocal(new Pos2i((int) mouseX, (int) mouseY)));
			this.pageEdit.setCursorPos(idx, true);
			this.clearDisplayCache();
		}
		return true;
	}

	private DisplayCache getDisplayCache() {
		if (this.displayCache == null) {
			this.displayCache = this.rebuildDisplayCache();
			this.pageMsg = Component.translatable("book.pageIndicator", this.currentPage + 1, this.getNumPages());
		}
		return this.displayCache;
	}

	private void clearDisplayCache() {
		this.displayCache = null;
	}

	private void clearDisplayCacheAfterPageChange() {
		this.pageEdit.setCursorToEnd();
		this.clearDisplayCache();
	}

	private DisplayCache rebuildDisplayCache() {
		String text = this.getCurrentPageText();
		if (text.isEmpty()) return DisplayCache.EMPTY;

		int cursorPos = this.pageEdit.getCursorPos();
		int selPos = this.pageEdit.getSelectionPos();
		IntList lineStarts = new IntArrayList();
		List<LineInfo> lines = Lists.newArrayList();
		MutableInt lineIndex = new MutableInt();
		MutableBoolean endsWithNewline = new MutableBoolean();
		StringSplitter splitter = this.font.getSplitter();

		splitter.splitLines(text, PAGE_TEXT_WIDTH, Style.EMPTY, true, (style, start, end) -> {
			int k = lineIndex.getAndIncrement();
			String sub = text.substring(start, end);
			endsWithNewline.setValue(sub.endsWith("\n"));
			String stripped = StringUtils.stripEnd(sub, " \n");
			int y = k * 9;
			Pos2i screenPos = this.convertLocalToScreen(new Pos2i(0, y));
			lineStarts.add(start);
			lines.add(new LineInfo(style, stripped, screenPos.x, screenPos.y));
		});

		int[] starts = lineStarts.toIntArray();
		boolean atEnd = cursorPos == text.length();
		Pos2i cursorLocal;
		if (atEnd && endsWithNewline.isTrue()) {
			cursorLocal = new Pos2i(0, lines.size() * 9);
		} else {
			int line = findLineFromPos(starts, cursorPos);
			int xOff = this.font.width(text.substring(starts[line], cursorPos));
			cursorLocal = new Pos2i(xOff, line * 9);
		}

		List<Rect2i> selection = Lists.newArrayList();
		if (cursorPos != selPos) {
			int lo = Math.min(cursorPos, selPos);
			int hi = Math.max(cursorPos, selPos);
			int loLine = findLineFromPos(starts, lo);
			int hiLine = findLineFromPos(starts, hi);
			if (loLine == hiLine) {
				selection.add(this.createPartialLineSelection(text, splitter, lo, hi, loLine * 9, starts[loLine]));
			} else {
				int loEnd = loLine + 1 > starts.length ? text.length() : starts[loLine + 1];
				selection.add(this.createPartialLineSelection(text, splitter, lo, loEnd, loLine * 9, starts[loLine]));
				for (int m = loLine + 1; m < hiLine; m++) {
					int y = m * 9;
					String lineText = text.substring(starts[m], starts[m + 1]);
					int w = (int) splitter.stringWidth(lineText);
					selection.add(this.createSelection(new Pos2i(0, y), new Pos2i(w, y + 9)));
				}
				selection.add(this.createPartialLineSelection(text, splitter, starts[hiLine], hi, hiLine * 9, starts[hiLine]));
			}
		}

		return new DisplayCache(text, cursorLocal, atEnd, starts, lines.toArray(new LineInfo[0]), selection.toArray(new Rect2i[0]));
	}

	static int findLineFromPos(int[] lineStarts, int pos) {
		int i = Arrays.binarySearch(lineStarts, pos);
		return i < 0 ? -(i + 2) : i;
	}

	private Rect2i createPartialLineSelection(String text, StringSplitter splitter, int from, int to, int y, int lineStart) {
		String s1 = text.substring(lineStart, from);
		String s2 = text.substring(lineStart, to);
		Pos2i p1 = new Pos2i((int) splitter.stringWidth(s1), y);
		Pos2i p2 = new Pos2i((int) splitter.stringWidth(s2), y + 9);
		return this.createSelection(p1, p2);
	}

	private Rect2i createSelection(Pos2i from, Pos2i to) {
		Pos2i s1 = this.convertLocalToScreen(from);
		Pos2i s2 = this.convertLocalToScreen(to);
		int x0 = Math.min(s1.x, s2.x);
		int x1 = Math.max(s1.x, s2.x);
		int y0 = Math.min(s1.y, s2.y);
		int y1 = Math.max(s1.y, s2.y);
		return new Rect2i(x0, y0, x1 - x0, y1 - y0);
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

	@OnlyIn(Dist.CLIENT)
	static class DisplayCache {
		static final DisplayCache EMPTY = new DisplayCache("", new Pos2i(0, 0), true, new int[]{0},
				new LineInfo[]{new LineInfo(Style.EMPTY, "", 0, 0)}, new Rect2i[0]);
		private final String fullText;
		final Pos2i cursor;
		final boolean cursorAtEnd;
		private final int[] lineStarts;
		final LineInfo[] lines;
		final Rect2i[] selection;

		DisplayCache(String text, Pos2i cursor, boolean cursorAtEnd, int[] lineStarts, LineInfo[] lines, Rect2i[] selection) {
			this.fullText = text;
			this.cursor = cursor;
			this.cursorAtEnd = cursorAtEnd;
			this.lineStarts = lineStarts;
			this.lines = lines;
			this.selection = selection;
		}

		int getIndexAtPosition(Font font, Pos2i pos) {
			int line = pos.y / 9;
			if (line < 0) return 0;
			if (line >= this.lines.length) return this.fullText.length();
			LineInfo info = this.lines[line];
			return this.lineStarts[line] + font.getSplitter().plainIndexAtWidth(info.contents, pos.x, info.style);
		}

		int changeLine(int pos, int dir) {
			int line = InfusedBookEditScreen.findLineFromPos(this.lineStarts, pos);
			int target = line + dir;
			if (target >= 0 && target < this.lineStarts.length) {
				int xOff = pos - this.lineStarts[line];
				int maxX = this.lines[target].contents.length();
				return this.lineStarts[target] + Math.min(xOff, maxX);
			}
			return pos;
		}

		int findLineStart(int pos) {
			int line = InfusedBookEditScreen.findLineFromPos(this.lineStarts, pos);
			return this.lineStarts[line];
		}

		int findLineEnd(int pos) {
			int line = InfusedBookEditScreen.findLineFromPos(this.lineStarts, pos);
			return this.lineStarts[line] + this.lines[line].contents.length();
		}
	}

	@OnlyIn(Dist.CLIENT)
	static class LineInfo {
		final Style style;
		final String contents;
		final Component asComponent;
		final int x;
		final int y;

		LineInfo(Style style, String contents, int x, int y) {
			this.style = style;
			this.contents = contents;
			this.x = x;
			this.y = y;
			this.asComponent = Component.literal(contents).setStyle(style);
		}
	}

	@OnlyIn(Dist.CLIENT)
	static class Pos2i {
		final int x;
		final int y;

		Pos2i(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
