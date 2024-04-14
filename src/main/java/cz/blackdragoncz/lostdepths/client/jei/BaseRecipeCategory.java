package cz.blackdragoncz.lostdepths.client.jei;

import com.mojang.blaze3d.platform.InputConstants;
import cz.blackdragoncz.lostdepths.client.gui.element.GuiElement;
import cz.blackdragoncz.lostdepths.client.gui.IGuiWrapper;
import cz.blackdragoncz.lostdepths.client.gui.element.IProgressInfoHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecipeCategory<RECIPE> extends AbstractContainerEventHandler implements IRecipeCategory<RECIPE>, IGuiWrapper {

    protected static IDrawable createIcon(IGuiHelper helper, IRecipeViewerRecipeType<?> recipeType) {
        ItemStack stack = recipeType.iconStack();
        if (stack.isEmpty()) {
            ResourceLocation icon = recipeType.icon();
            if (icon == null) {
                throw new IllegalStateException("Expected recipe type to have either an icon stack or an icon location");
            }
            return helper.drawableBuilder(icon, 0, 0, 18, 18).setTextureSize(18, 18).build();
        }
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, stack);
    }

    private final List<GuiElement> guiElements = new ArrayList<>();
    private final Component component;
    private final IGuiHelper guiHelper;
    private final IDrawable background;
    private final RecipeType<RECIPE> recipeType;
    private final IDrawable icon;
    private final int xOffset;
    private final int yOffset;

    @Nullable
    private ITickTimer timer;

    protected BaseRecipeCategory(IGuiHelper helper, IRecipeViewerRecipeType<RECIPE> recipeType) {
        this(helper, LostDepthsJEI.recipeType(recipeType), recipeType.getTextComponent(), createIcon(helper, recipeType), recipeType.xOffset(), recipeType.yOffset(), recipeType.width(), recipeType.height());
    }

    protected BaseRecipeCategory(IGuiHelper helper, RecipeType<RECIPE> recipeType, Component component, IDrawable icon, int xOffset, int yOffset, int width, int height) {
        this.recipeType = recipeType;
        this.component = component;
        this.guiHelper = helper;
        this.icon = icon;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.background = new NOOPDrawable(width, height);
    }

    @Override
    public boolean handleInput(RECIPE recipe, double mouseX, double mouseY, InputConstants.Key input) {
        return switch (input.getType()) {
            case KEYSYM -> keyPressed(input.getValue(), -1, 0);
            case SCANCODE -> keyPressed(-1, input.getValue(), 0);
            //Shift by the offset as JEI doesn't realize we are actually starting at negatives
            case MOUSE -> mouseClicked(mouseX, mouseY, input.getValue());
        };
    }

    @Override
    public int getGuiLeft() {
        return xOffset;
    }

    @Override
    public int getGuiTop() {
        return yOffset;
    }

    @Override
    public int getXSize() {
        return background.getWidth();
    }

    @Override
    public int getYSize() {
        return background.getHeight();
    }

    @Override
    public Font getFont() {
        return Minecraft.getInstance().font;
    }

    @Override
    public RecipeType<RECIPE> getRecipeType() {
        return recipeType;
    }

    @Override
    public Component getTitle() {
        return component;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }
    @Override
    public List<? extends GuiEventListener> children() {
        return guiElements;
    }

    protected IProgressInfoHandler getSimpleProgressTimer() {
        if (timer == null) {
            timer = guiHelper.createTickTimer(SharedConstants.TICKS_PER_SECOND, 20, false);
        }
        return () -> timer.getValue() / 20D;
    }
}
