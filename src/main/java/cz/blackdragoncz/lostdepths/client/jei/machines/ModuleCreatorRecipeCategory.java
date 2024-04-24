package cz.blackdragoncz.lostdepths.client.jei.machines;

import cz.blackdragoncz.lostdepths.client.jei.BaseRecipeCategory;
import cz.blackdragoncz.lostdepths.client.jei.IRecipeViewerRecipeType;
import cz.blackdragoncz.lostdepths.recipe.ModuleRecipe;
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

public class ModuleCreatorRecipeCategory extends BaseRecipeCategory<ModuleRecipe> {

    private static final ResourceLocation texture = new ResourceLocation("lostdepths:textures/screens/module_creator.png");

    private final IDrawable background;

    public ModuleCreatorRecipeCategory(IGuiHelper helper, IRecipeViewerRecipeType<ModuleRecipe> recipeType) {
        super(helper, recipeType);
        this.background = helper.drawableBuilder(texture, 12, 22, 142, 70).setTextureSize(176, 186).build();
    }

    @Override
    public void draw(ModuleRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        this.background.draw(guiGraphics, 0, 0);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, ModuleRecipe recipe, @NotNull IFocusGroup focusGroup) {
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient ingredient = recipe.getIngredients().get(i);
            builder.addSlot(RecipeIngredientRole.INPUT, 10, 17 + i * 18).addIngredients(ingredient);
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 10, 30).addItemStack(recipe.getResult());

        builder.moveRecipeTransferButton(160 - 16, getHeight() - 16);
    }
}
