package cz.blackdragoncz.lostdepths.client.jei.machines;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.client.jei.BaseRecipeCategory;
import cz.blackdragoncz.lostdepths.client.jei.IRecipeViewerRecipeType;
import cz.blackdragoncz.lostdepths.recipe.FusionTableRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class FusionTableRecipeCategory extends BaseRecipeCategory<FusionTableRecipe> {

    private final IDrawable slot;
    private final int arrowWidth = 24;
    private final int arrowHeight = 17;
    protected final IDrawable arrowBg;
    protected final IDrawableAnimated arrow;

    public FusionTableRecipeCategory(IGuiHelper guiHelper, IRecipeViewerRecipeType<FusionTableRecipe> recipeType) {
        super(guiHelper, recipeType);
        slot = guiHelper.getSlotDrawable();
        this.arrowBg = guiHelper.drawableBuilder(new ResourceLocation("textures/gui/container/furnace.png"), 80, 35, arrowWidth, arrowHeight).build();
        this.arrow = guiHelper.drawableBuilder(new ResourceLocation("textures/gui/container/furnace.png"), 176, 14, arrowWidth, arrowHeight).buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FusionTableRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1)
                .addItemStack(recipe.getInput1());

        builder.addSlot(RecipeIngredientRole.INPUT, 1, 21)
                .addItemStack(recipe.getInput2());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 109, 11)
                .addItemStack(recipe.getOutput());
    }

    @Override
    public void draw(FusionTableRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

        // Input slot backgrounds
        slot.draw(guiGraphics, 0, 0);
        slot.draw(guiGraphics, 0, 20);

        // Output slot background
        slot.draw(guiGraphics, 108, 10);

        // Arrow between inputs and output
        int centerY = 20 - arrowHeight / 2;
        arrowBg.draw(guiGraphics, 50, centerY);
        arrow.draw(guiGraphics, 50, centerY);
    }
}
