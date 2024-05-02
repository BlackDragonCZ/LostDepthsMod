package cz.blackdragoncz.lostdepths.client.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import cz.blackdragoncz.lostdepths.client.gui.AllGuiTextures;
import cz.blackdragoncz.lostdepths.client.gui.element.GuiGameElement;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.recipe.ItemUseRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ItemUseCategory extends BaseRecipeCategory<ItemUseRecipe> {

    protected ItemUseCategory(IGuiHelper helper, IRecipeViewerRecipeType<ItemUseRecipe> recipeType) {
        super(helper, recipeType);
    }

    @Override
    public void draw(ItemUseRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, graphics, mouseX, mouseY);

        AllGuiTextures.JEI_SHADOW.render(graphics, 62, 47);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 74, 10);

        BlockState state = LostdepthsModBlocks.SERPENTINE_ORE_UNPOWERED.get()
                .defaultBlockState();

        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(74, 51, 100);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale = 20;

        GuiGameElement.of(state)
                .scale(scale)
                .render(graphics);

        matrixStack.popPose();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ItemUseRecipe recipe, IFocusGroup focuses) {

        builder.addSlot(RecipeIngredientRole.INPUT, 27, 38)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(Ingredient.of(new ItemStack(LostdepthsModItems.SERPENTINE_ORE_UNPOWERED.get())));

        builder.addSlot(RecipeIngredientRole.INPUT, 51, 5)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(Ingredient.of(new ItemStack(LostdepthsModItems.ENDER_VOLTAIC_SOLUTION.get())))
                .addTooltipCallback(
                        (view, tooltip) -> tooltip.add(1, Component.translatable("lostdepths.description.serpentine_ore_use"))
                );

        builder.addSlot(RecipeIngredientRole.OUTPUT, 132, 38)
                .setBackground(getRenderedSlot(), -1, -1)
                .addItemStack(new ItemStack(LostdepthsModItems.SERPENTINE_ORE.get()));
    }
}
