package cz.blackdragoncz.lostdepths.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.client.hologram.HologramTemplates;
import cz.blackdragoncz.lostdepths.network.HologramSelectPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.jetbrains.annotations.Nullable;

// Lists what THIS client can actually render, since templates are client assets. The server re-checks the pick against its own index.
public class HologramSelectScreen extends Screen {

	private static final int WIDTH = 176;
	// Full panel height: NTGuiTheme only has art for the top 120px, so a shorter window looks cut off at the bottom.
	private static final int HEIGHT = 226;

	private final BlockPos pos;
	private final List<ResourceLocation> ids = new ArrayList<>();

	// What the selector is showing versus what the projector is actually set to. Editing a coordinate must never change the hologram, so packets
	// always carry `applied`, and only the Select button promotes `browsing` into it.
	private int browsing = -1;
	@Nullable
	private ResourceLocation applied;
	private boolean visible;
	private boolean dirty;
	private final boolean initialCulled;
	private boolean sentCulled;

	private int leftPos;
	private int topPos;
	private EditBox offsetX;
	private EditBox offsetY;
	private EditBox offsetZ;
	private EditBox rotationX;
	private EditBox rotationY;
	private EditBox rotationZ;
	private Button selector;
	private Checkbox cull;

	private final float initialOffsetX;
	private final float initialOffsetY;
	private final float initialOffsetZ;
	private final float initialRotationX;
	private final float initialRotationY;
	private final float initialRotationZ;

	public HologramSelectScreen(BlockPos pos, @Nullable ResourceLocation current, float offsetX, float offsetY, float offsetZ, float rotationX, float rotationY, float rotationZ, boolean visible, boolean culled) {
		super(Component.translatable("gui.lostdepths.hologram_select"));
		this.pos = pos;
		this.applied = current;
		this.visible = visible;
		this.initialCulled = culled;
		this.sentCulled = culled;
		this.initialOffsetX = offsetX;
		this.initialOffsetY = offsetY;
		this.initialOffsetZ = offsetZ;
		this.initialRotationX = rotationX;
		this.initialRotationY = rotationY;
		this.initialRotationZ = rotationZ;
		this.ids.addAll(HologramTemplates.ids());
		this.ids.sort(Comparator.comparing(ResourceLocation::toString));
		this.browsing = current == null ? -1 : this.ids.indexOf(current);
	}

	@Override
	protected void init() {
		super.init();
		this.leftPos = (this.width - WIDTH) / 2;
		this.topPos = (this.height - HEIGHT) / 2;

		// Coordinates are literals with a // gui: marker so .tools/gui-designer can read and rewrite them. Keep them literal.
		this.addRenderableWidget(Button.builder(Component.literal("<"), b -> cycle(-1)).bounds(this.leftPos + 8, this.topPos + 24, 16, 18).build()); // gui:button:prev
		this.selector = this.addRenderableWidget(Button.builder(selectorLabel(), b -> cycle(1)).bounds(this.leftPos + 26, this.topPos + 24, 124, 18).build()); // gui:button:selector
		this.addRenderableWidget(Button.builder(Component.literal(">"), b -> cycle(1)).bounds(this.leftPos + 152, this.topPos + 24, 16, 18).build()); // gui:button:next
		this.addRenderableWidget(Button.builder(Component.translatable("gui.lostdepths.hologram_set"), b -> applySelection()).bounds(this.leftPos + 8, this.topPos + 46, 160, 18).build()); // gui:button:set

		this.offsetX = numberField(this.leftPos + 8, this.topPos + 88, 50, 16, this.initialOffsetX); // gui:edit_box:offset_x
		this.offsetY = numberField(this.leftPos + 63, this.topPos + 88, 50, 16, this.initialOffsetY); // gui:edit_box:offset_y
		this.offsetZ = numberField(this.leftPos + 118, this.topPos + 88, 50, 16, this.initialOffsetZ); // gui:edit_box:offset_z

		this.rotationX = numberField(this.leftPos + 8, this.topPos + 132, 50, 16, this.initialRotationX); // gui:edit_box:rotation_x
		this.rotationY = numberField(this.leftPos + 63, this.topPos + 132, 50, 16, this.initialRotationY); // gui:edit_box:rotation_y
		this.rotationZ = numberField(this.leftPos + 118, this.topPos + 132, 50, 16, this.initialRotationZ); // gui:edit_box:rotation_z

		this.addRenderableWidget(Button.builder(Component.translatable("gui.lostdepths.hologram_display"), b -> setVisible(true)).bounds(this.leftPos + 8, this.topPos + 160, 76, 20).build()); // gui:button:display
		this.addRenderableWidget(Button.builder(Component.translatable("gui.lostdepths.hologram_hide"), b -> setVisible(false)).bounds(this.leftPos + 92, this.topPos + 160, 76, 20).build()); // gui:button:hide
		this.cull = this.addRenderableWidget(new Checkbox(this.leftPos + 8, this.topPos + 198, 150, 20, Component.translatable("gui.lostdepths.hologram_cull"), this.initialCulled)); // gui:checkbox:cull
	}

	private EditBox numberField(int x, int y, int w, int h, float value) {
		EditBox box = new EditBox(this.font, x, y, w, h, Component.empty());
		box.setMaxLength(12);
		box.setValue(trim(value));
		box.setFilter(HologramSelectScreen::isNumeric);
		// Live: mark dirty and let tick() push it, so holding a key does not fire a packet per character.
		box.setResponder(text -> this.dirty = true);
		return this.addRenderableWidget(box);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.cull != null && this.cull.selected() != this.sentCulled) {
			this.sentCulled = this.cull.selected();
			this.dirty = true;
		}
		if (this.dirty) {
			this.dirty = false;
			send();
		}
	}

	private static boolean isNumeric(String text) {
		if (text.isEmpty() || text.equals("-") || text.equals(".") || text.equals("-."))
			return true;
		try {
			Float.parseFloat(text);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static String trim(float value) {
		return value == (long) value ? Long.toString((long) value) : Float.toString(value);
	}

	private static float parse(EditBox box) {
		try {
			return Float.parseFloat(box.getValue());
		} catch (NumberFormatException e) {
			return 0.0f;
		}
	}

	private Component selectorLabel() {
		if (this.ids.isEmpty())
			return Component.translatable("gui.lostdepths.hologram_none");
		if (this.browsing < 0)
			return Component.translatable("gui.lostdepths.hologram_unset");
		ResourceLocation id = this.ids.get(this.browsing);
		return Component.literal((id.equals(this.applied) ? "> " : "") + id.getPath());
	}

	private void cycle(int direction) {
		if (this.ids.isEmpty())
			return;
		// -1 is the "none" slot, so the list wraps through unset as well.
		int size = this.ids.size() + 1;
		this.browsing = Math.floorMod(this.browsing + 1 + direction, size) - 1;
		this.selector.setMessage(selectorLabel());
	}

	private void applySelection() {
		this.applied = this.browsing < 0 ? null : this.ids.get(this.browsing);
		this.selector.setMessage(selectorLabel());
		send();
	}

	private void setVisible(boolean visible) {
		this.visible = visible;
		send();
	}

	private void send() {
		LostdepthsMod.PACKET_HANDLER.sendToServer(new HologramSelectPacket(this.pos, this.applied, parse(this.offsetX), parse(this.offsetY), parse(this.offsetZ), parse(this.rotationX), parse(this.rotationY), parse(this.rotationZ), this.visible, this.cull != null && this.cull.selected()));
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		// A focused field must swallow the inventory key, or typing a coordinate closes the screen.
		if (this.getFocused() instanceof EditBox box && box.canConsumeInput())
			return super.keyPressed(keyCode, scanCode, modifiers);
		if (this.minecraft != null && this.minecraft.options.keyInventory.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode))) {
			this.onClose();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public void onClose() {
		// Catch anything typed in the same tick as the close.
		if (this.dirty) {
			this.dirty = false;
			send();
		}
		super.onClose();
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(graphics);
		NTGuiTheme.panel(graphics, this.leftPos, this.topPos, WIDTH, HEIGHT);
		Component state = Component.translatable(this.visible ? "gui.lostdepths.hologram_state_shown" : "gui.lostdepths.hologram_state_hidden");

		// Every label is positioned literally and marked so .tools/gui-designer can move it. Keep the coordinates literal.
		graphics.drawString(this.font, this.title, this.leftPos + 8, this.topPos + 8, NTGuiTheme.HEADING, false); // gui:label:title
		graphics.drawString(this.font, Component.translatable("gui.lostdepths.hologram_offset"), this.leftPos + 8, this.topPos + 65, NTGuiTheme.BODY, false); // gui:label:offset_heading
		graphics.drawString(this.font, "X", this.leftPos + 8, this.topPos + 77, NTGuiTheme.HINT, false); // gui:label:offset_x_axis
		graphics.drawString(this.font, "Y", this.leftPos + 63, this.topPos + 77, NTGuiTheme.HINT, false); // gui:label:offset_y_axis
		graphics.drawString(this.font, "Z", this.leftPos + 118, this.topPos + 77, NTGuiTheme.HINT, false); // gui:label:offset_z_axis
		graphics.drawString(this.font, Component.translatable("gui.lostdepths.hologram_rotation"), this.leftPos + 8, this.topPos + 106, NTGuiTheme.BODY, false); // gui:label:rotation_heading
		graphics.drawString(this.font, "X", this.leftPos + 8, this.topPos + 120, NTGuiTheme.HINT, false); // gui:label:rotation_x_axis
		graphics.drawString(this.font, "Y", this.leftPos + 63, this.topPos + 120, NTGuiTheme.HINT, false); // gui:label:rotation_y_axis
		graphics.drawString(this.font, "Z", this.leftPos + 118, this.topPos + 120, NTGuiTheme.HINT, false); // gui:label:rotation_z_axis
		graphics.drawString(this.font, state, this.leftPos + 8, this.topPos + 186, NTGuiTheme.HINT, false); // gui:label:state

		super.render(graphics, mouseX, mouseY, partialTick);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
