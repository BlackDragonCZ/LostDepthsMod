package cz.blackdragoncz.lostdepths.client.jei.machines;

import cz.blackdragoncz.lostdepths.client.jei.BaseRecipeCategory;
import cz.blackdragoncz.lostdepths.client.recipe_view.IRecipeViewerRecipeType;
import cz.blackdragoncz.lostdepths.recipe.CompressingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GalacticCompressorRecipeCategory extends BaseRecipeCategory<CompressingRecipe> {

    private final IDrawable slot;

    public GalacticCompressorRecipeCategory(IGuiHelper helper, IRecipeViewerRecipeType<CompressingRecipe> recipeType) {
        super(helper, recipeType);
        slot = helper.getSlotDrawable();
    }

    @Override
    public void draw(CompressingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

        slot.draw(guiGraphics, 35, 1);
        slot.draw(guiGraphics, 95, 1);

        guiGraphics.blit(new ResourceLocation("lostdepths:textures/screens/recipebook_arrow.png"), 28 + 35, 2, 0, 0, 24, 17, 24, 17);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CompressingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 35 + 1, 2).addItemStack(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 95 + 1, 2).addItemStack(recipe.getOutput());
    }
}
