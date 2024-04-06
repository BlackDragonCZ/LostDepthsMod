package cz.blackdragoncz.lostdepths.client.recipe_view;

import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.recipe.CompressingRecipe;
import cz.blackdragoncz.lostdepths.recipe.LDRecipeType;
import cz.blackdragoncz.lostdepths.recipe.RecipeTypeWrapper;
import cz.blackdragoncz.lostdepths.util.IItemProvider;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class RecipeViewerRecipeType {

    public static final RecipeTypeWrapper<CompressingRecipe> V1_COMPRESSING = new RecipeTypeWrapper<>(LDRecipeType.V1_COMPRESSING.get(), CompressingRecipe.class, 0, 0, 144, 20, new IItemProvider() {
        @Override
        public @NotNull Item asItem() {
            return LostdepthsModItems.GALACTIC_COMPRESSOR.get();
        }
    });

    public static final RecipeTypeWrapper<CompressingRecipe> V2_COMPRESSING = new RecipeTypeWrapper<>(LDRecipeType.V2_COMPRESSING.get(), CompressingRecipe.class, 0, 0, 144, 20, new IItemProvider() {
        @Override
        public @NotNull Item asItem() {
            return LostdepthsModItems.EXTRA_TERESTRIAL_COMPRESSOR.get();
        }
    });

}
