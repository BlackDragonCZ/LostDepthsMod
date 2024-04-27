package cz.blackdragoncz.lostdepths.client.jei.machines;

import com.mojang.serialization.Codec;
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
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class ModuleCreatorRecipeCategory extends BaseRecipeCategory<ModuleRecipe> {

    private static final ResourceLocation texture = new ResourceLocation("lostdepths:textures/screens/module_creator.png");

    private final IGuiHelper helper;
    private IDrawable background;

    public ModuleCreatorRecipeCategory(IGuiHelper helper, IRecipeViewerRecipeType<ModuleRecipe> recipeType) {
        super(helper, recipeType);
        this.helper = helper;
    }

    @Override
    public void draw(ModuleRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        this.background = helper.drawableBuilder(texture, 15, 15, 136, 56).setTextureSize(256, 256).build();
        this.background.draw(guiGraphics, 0, 0);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, ModuleRecipe recipe, @NotNull IFocusGroup focusGroup) {
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient ingredient = recipe.getIngredients().get(i);
            if (i < 3)
                builder.addSlot(RecipeIngredientRole.INPUT, 2 + i * 26, 2).addIngredients(ingredient);
            else
                builder.addSlot(RecipeIngredientRole.INPUT, 28, 39).addIngredients(ingredient);
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 119, 20).addItemStack(recipe.getResult());

        builder.moveRecipeTransferButton(getWidth() - 16, getHeight() - 16);
    }
}
