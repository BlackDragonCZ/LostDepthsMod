package cz.blackdragoncz.lostdepths.client.jei.machines;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.client.jei.BaseRecipeCategory;
import cz.blackdragoncz.lostdepths.client.jei.IRecipeViewerRecipeType;
import cz.blackdragoncz.lostdepths.recipe.CompressingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class GenericCompressorRecipeCategory extends BaseRecipeCategory<CompressingRecipe> {

    private final int neededStackSize;
    private final boolean requiresPower;

    private final int arrowWidth = 24;
    private final int arrowHeight = 17;
    private final IDrawable slot;
    protected final IDrawable arrowBg;
    protected final IDrawable powerIcon;
    protected final IDrawableAnimated arrow;

    public GenericCompressorRecipeCategory(IGuiHelper helper, IRecipeViewerRecipeType<CompressingRecipe> recipeType, int neededStackSize, boolean requiresPower) {
        super(helper, recipeType);
        this.neededStackSize = neededStackSize;
        this.requiresPower = requiresPower;

        slot = helper.getSlotDrawable();
        this.arrowBg = helper.drawableBuilder(new ResourceLocation("textures/gui/container/furnace.png"), 80, 35, arrowWidth, arrowHeight).build();
        this.arrow = helper.drawableBuilder(new ResourceLocation("textures/gui/container/furnace.png"), 176, 14, arrowWidth, arrowHeight).buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
        this.powerIcon = helper.drawableBuilder(LostdepthsMod.rl("textures/gui/jei_handler.png"), 224, 96, 15, 14).build();
    }

    @Override
    public void draw(CompressingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

        slot.draw(guiGraphics, 35, 1);
        slot.draw(guiGraphics, 95, 1);

        int w = this.getBackground().getWidth();
        int h = this.getBackground().getHeight();
        arrowBg.draw(guiGraphics, w / 2 - arrowWidth / 2 + 1, h / 2 - arrowHeight / 2 + 1);
        arrow.draw(guiGraphics, w / 2 - arrowWidth / 2, h / 2 - arrowHeight / 2);

        if (requiresPower) {
            powerIcon.draw(guiGraphics, 8, h / 2 - 14 / 2);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CompressingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 35 + 1, 2).addItemStack(new ItemStack(recipe.getInput().getItem(), neededStackSize));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 95 + 1, 2).addItemStack(recipe.getOutput());
    }
}
