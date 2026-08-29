package cz.blackdragoncz.lostdepths.client.jei;

import cz.blackdragoncz.lostdepths.client.gui.AllGuiTextures;
import cz.blackdragoncz.lostdepths.recipe.ItemUseRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Collections;
import java.util.List;

/**
 * The mob-target counterpart of {@link ItemUseCategory}: use item X on mob Y to get Z.
 * The target mob is the anchor - the use slot (41,1) and the arrow (41,19) sit centred above it,
 * and the result goes on the right. Slot coords stay integer literals so the JEI designer can read them.
 */
public class EntityUseCategory extends BaseRecipeCategory<ItemUseRecipe> {

    private static final int TARGET_X = 50;
    private static final int RESULT_X = 140;
    private static final int MOB_FEET = 54;
    private static final int MOB_BOX = 20;
    private static final int SHADOW_Y = 47;
    private static final int SHADOW_HALF_WIDTH = 26;
    private static final int HOVER_HALF_WIDTH = 20;

    protected EntityUseCategory(IGuiHelper helper, IRecipeViewerRecipeType<ItemUseRecipe> recipeType) {
        super(helper, recipeType);
    }

    @Override
    public void draw(ItemUseRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, graphics, mouseX, mouseY);

        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, TARGET_X - 9, 19);

        drawMob(graphics, recipe.getUseOnEntity(), TARGET_X);
        drawMob(graphics, recipe.getResultEntity(), RESULT_X);
    }

    /** Name comes from the type, not the rendered mob, so it still shows if the render failed. */
    @Override
    public List<Component> getTooltipStrings(ItemUseRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (isOverMob(TARGET_X, mouseX, mouseY) && recipe.getUseOnEntity() != null)
            return List.of(recipe.getUseOnEntity().getDescription());
        if (isOverMob(RESULT_X, mouseX, mouseY) && recipe.getResultEntity() != null)
            return List.of(recipe.getResultEntity().getDescription());
        return Collections.emptyList();
    }

    private static boolean isOverMob(int centerX, double mouseX, double mouseY) {
        return mouseX >= centerX - HOVER_HALF_WIDTH && mouseX <= centerX + HOVER_HALF_WIDTH
                && mouseY >= MOB_FEET - MOB_BOX && mouseY <= MOB_FEET;
    }

    private static void drawMob(GuiGraphics graphics, EntityType<?> type, int centerX) {
        if (type == null)
            return;
        AllGuiTextures.JEI_SHADOW.render(graphics, centerX - SHADOW_HALF_WIDTH, SHADOW_Y);
        JeiEntityRenderer.render(graphics, type, centerX, MOB_FEET, MOB_BOX);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ItemUseRecipe recipe, IFocusGroup focuses) {

        builder.addSlot(RecipeIngredientRole.INPUT, 41, 1)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(Ingredient.of(recipe.getUseItem()))
                .addTooltipCallback(
                        (view, tooltip) -> tooltip.add(1, Component.translatable(recipe.getUseDescription()))
                );

        // A mob result has no slot - it is drawn instead, so the recipe is only reachable from its input.
        if (!recipe.getResult().isEmpty())
            builder.addSlot(RecipeIngredientRole.OUTPUT, 132, 38)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addItemStack(recipe.getResult());
    }
}
