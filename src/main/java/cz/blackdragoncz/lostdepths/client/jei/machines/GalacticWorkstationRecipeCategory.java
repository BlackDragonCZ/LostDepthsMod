package cz.blackdragoncz.lostdepths.client.jei.machines;

import cz.blackdragoncz.lostdepths.client.jei.BaseRecipeCategory;
import cz.blackdragoncz.lostdepths.client.jei.IRecipeViewerRecipeType;
import cz.blackdragoncz.lostdepths.recipe.LDShapedRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class GalacticWorkstationRecipeCategory extends BaseRecipeCategory<LDShapedRecipe> {

    private static final ResourceLocation texture = new ResourceLocation("lostdepths:textures/screens/galactic_workstation.png");
    private final IDrawable background;

    public GalacticWorkstationRecipeCategory(IGuiHelper helper, IRecipeViewerRecipeType<LDShapedRecipe> recipeType) {
        super(helper, recipeType);

        this.background = helper.drawableBuilder(texture, 7, 13, 160, 60).setTextureSize(176, 166).build();
    }

    @Override
    public void draw(@NotNull LDShapedRecipe recipe, @NotNull IRecipeSlotsView slotsView, @NotNull GuiGraphics graphics, double mouseX, double mouseY) {
        this.background.draw(graphics);
        super.draw(recipe, slotsView, graphics, mouseX, mouseY);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, LDShapedRecipe shapedRecipe, IFocusGroup focusGroup) {
        for (int y = 0; y < shapedRecipe.getHeight(); y++) {
            for (int x = 0; x < shapedRecipe.getWidth(); x++) {
                int index = x + y * shapedRecipe.getWidth();
                Ingredient ingredient = shapedRecipe.getIngredients().get(index);

                if (index == 4 || index == 6 || index == 8)
                    continue;

                builder.addSlot(RecipeIngredientRole.INPUT, 9 + x * 18, 4 + y * 18).addIngredients(ingredient);
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 135, 22).addItemStack(shapedRecipe.getResult());

        builder.moveRecipeTransferButton(160 - 16, 60 - 16);
    }
}
