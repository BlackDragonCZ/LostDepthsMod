package cz.blackdragoncz.lostdepths.client.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import cz.blackdragoncz.lostdepths.client.gui.AllGuiTextures;
import cz.blackdragoncz.lostdepths.client.gui.element.GuiGameElement;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.recipe.MetaMaterializerRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class MetaMaterializerCategory extends BaseRecipeCategory<MetaMaterializerRecipe> {

    protected MetaMaterializerCategory(IGuiHelper helper, IRecipeViewerRecipeType<MetaMaterializerRecipe> recipeType) {
        super(helper, recipeType);
    }

    @Override
    public void draw(MetaMaterializerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, graphics, mouseX, mouseY);


        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(0, 66, 0);
        matrixStack.scale(2.0f, 1.0f, 0.75f);
        AllGuiTextures.JEI_SHADOW.render(graphics, 0, 0);
        matrixStack.popPose();

        AllGuiTextures.JEI_SHADOW.render(graphics, 118, 47);

        for (int i = 0; i < 3; i++) {

            AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 10 + i * 30, 30);

            matrixStack.pushPose();
            matrixStack.translate(10 + i * 30, 70, 100);
            matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
            matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));

            GuiGameElement.of(LostdepthsModBlocks.META_COLLECTOR.get().defaultBlockState())
                    .scale(20.0)
                    .render(graphics);
            matrixStack.popPose();
        }

        matrixStack.pushPose();
        matrixStack.translate(130, 50, 100);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));

        GuiGameElement.of(LostdepthsModBlocks.META_MATERIALIZER.get().defaultBlockState())
                .scale(20.0)
                .render(graphics);
        matrixStack.popPose();

        RenderSystem.disableCull();
        matrixStack.pushPose();
        matrixStack.translate(115, 30, 0);
        matrixStack.last().pose().rotate((float) Math.toRadians(-90), 0, 0, 1);
        matrixStack.scale(-1, 1, 1);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 0, 0);
        matrixStack.popPose();

        matrixStack.pushPose();
        matrixStack.translate(140, 55, 0);
        matrixStack.last().pose().rotate((float) Math.toRadians(-90), 0, 0, 1);
        matrixStack.scale(-1, 1, 1);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 0, 0);
        matrixStack.popPose();
        RenderSystem.enableCull();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MetaMaterializerRecipe recipe, IFocusGroup iFocusGroup) {
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            builder.addSlot(RecipeIngredientRole.INPUT, 15 + i * 30, 10)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addTooltipCallback(
                            (view, tooltip) -> tooltip.add(1, Component.literal("Interact with ").append(Component.translatable(recipe.getItems().get(finalI).getDescriptionId())))
                    )
                    .addItemStack(recipe.getItems().get(i));
        }

        builder.addSlot(RecipeIngredientRole.INPUT, 110, 10)
                .setBackground(getRenderedSlot(), -1, -1)
                .addTooltipCallback(
                        (view, tooltip) -> tooltip.add(1, Component.literal("Interact with ").append(Component.translatable(recipe.getHeldItem().getDescriptionId())))
                )
                .addItemStack(recipe.getHeldItem());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 155, 60)
                .setBackground(getRenderedSlot(), -1, -1)
                .addTooltipCallback(
                        (view, tooltip) -> tooltip.add(1, Component.literal("Result of correct placed items in collectors and interacted with item on meta materializer."))
                )
                .addItemStack(recipe.getResult());
    }
}
