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

public class AlloyWorkstationRecipeCategory extends BaseRecipeCategory<LDShapedRecipe> {

    private static final ResourceLocation texture = new ResourceLocation("lostdepths:textures/screens/wsgui_2.png");
    private final IDrawable background;

    public AlloyWorkstationRecipeCategory(IGuiHelper helper, IRecipeViewerRecipeType<LDShapedRecipe> recipeType) {
        super(helper, recipeType);
        this.background = helper.drawableBuilder(texture, 3, 33, 160, 60 + 18).setTextureSize(176, 206).build();
    }

    @Override
    public void draw(@NotNull LDShapedRecipe recipe, @NotNull IRecipeSlotsView slotsView, @NotNull GuiGraphics graphics, double mouseX, double mouseY) {
        this.background.draw(graphics);
        super.draw(recipe, slotsView, graphics, mouseX, mouseY);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, LDShapedRecipe shapedRecipe, @NotNull IFocusGroup focusGroup) {
        for (int y = 0; y < shapedRecipe.getHeight(); y++) {
            for (int x = 0; x < shapedRecipe.getWidth(); x++) {
                int index = x + y * shapedRecipe.getWidth();
                Ingredient ingredient = shapedRecipe.getIngredients().get(index);

                if (index == 3 || index == 5)
                    continue;

                int yOffset = 0;

                if ((x == 0 || x == 2) && y == 0) {
                    yOffset = 9;
                } else if ((x == 0 || x == 2) && y == 2) {
                    yOffset = -18;
                }

                builder.addSlot(RecipeIngredientRole.INPUT, 4 + x * 27, 4 + yOffset + y * 27).addIngredients(ingredient);
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 139, 22).addItemStack(shapedRecipe.getResult());

        builder.moveRecipeTransferButton(160 - 16, getHeight() - 16);
    }
}
