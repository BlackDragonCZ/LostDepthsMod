package cz.blackdragoncz.lostdepths.client.jei;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.client.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ChestLootCategory implements mezz.jei.api.recipe.category.IRecipeCategory<ChestLootRecipe> {

    public static final RecipeType<ChestLootRecipe> RECIPE_TYPE =
            new RecipeType<>(LostdepthsMod.rl("ld_chest_loot"), ChestLootRecipe.class);

    private static final int WIDTH = 162;
    private static final int HEIGHT = 120;

    private static final int SLOT_SIZE = 18;
    private static final int COLUMNS = 7;
    private static final int SLOT_START_X = 0;
    private static final int SLOT_START_Y = 12;
    private static final int SLOT_SPACING = 23;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slotDrawable;

    public ChestLootCategory(IGuiHelper helper) {
        this.background = new NOOPDrawable(WIDTH, HEIGHT);
        this.icon = helper.drawableBuilder(
                LostdepthsMod.rl("textures/item/questionmark.png"),
                0, 0, 16, 16
        ).setTextureSize(16, 16).build();
        this.slotDrawable = BaseRecipeCategory.getRenderedSlot();
    }

    @Override
    public RecipeType<ChestLootRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Chest Loot");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ChestLootRecipe recipe, IFocusGroup focuses) {
        List<ChestLootRecipe.LootEntry> entries = recipe.entries();

        for (int i = 0; i < entries.size(); i++) {
            ChestLootRecipe.LootEntry entry = entries.get(i);
            int col = i % COLUMNS;
            int row = i / COLUMNS;
            int x = SLOT_START_X + col * SLOT_SPACING;
            int y = SLOT_START_Y + row * (SLOT_SPACING + 12);

            builder.addSlot(RecipeIngredientRole.OUTPUT, x + 1, y + 1)
                    .addItemStack(entry.item());
        }
    }

    @Override
    public void draw(ChestLootRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics g, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        List<ChestLootRecipe.LootEntry> entries = recipe.entries();
        int totalWeight = recipe.totalWeight();

        // Draw title: chest name + rolls
        String title = recipe.chestName() + " (Rolls: " + recipe.rollsMin() + "-" + recipe.rollsMax() + ")";
        g.drawString(font, title, 0, 1, AllGuiTextures.FONT_COLOR, false);

        // Draw slots and weight labels
        for (int i = 0; i < entries.size(); i++) {
            ChestLootRecipe.LootEntry entry = entries.get(i);
            int col = i % COLUMNS;
            int row = i / COLUMNS;
            int x = SLOT_START_X + col * SLOT_SPACING;
            int y = SLOT_START_Y + row * (SLOT_SPACING + 12);

            // Draw slot background
            slotDrawable.draw(g, x, y);

            // Draw weight percentage below slot
            float percent = (float) entry.weight() / totalWeight * 100f;
            String label;
            if (percent >= 10) {
                label = String.format("%.0f%%", percent);
            } else {
                label = String.format("%.1f%%", percent);
            }
            int labelWidth = font.width(label);
            g.drawString(font, label, x + SLOT_SIZE / 2 - labelWidth / 2, y + SLOT_SIZE + 1, 0x888888, false);

            // Draw count range
            if (entry.minCount() != entry.maxCount()) {
                String count = entry.minCount() + "-" + entry.maxCount();
                int countWidth = font.width(count);
                g.drawString(font, count, x + SLOT_SIZE / 2 - countWidth / 2, y + SLOT_SIZE + 10, 0xAAAAAA, false);
            }
        }
    }
}
