package cz.blackdragoncz.lostdepths.client.jei;

import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.recipe.CompressingRecipe;
import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import cz.blackdragoncz.lostdepths.recipe.LDShapedRecipe;
import cz.blackdragoncz.lostdepths.recipe.ModuleRecipe;
import cz.blackdragoncz.lostdepths.recipe.RecipeTypeWrapper;
import cz.blackdragoncz.lostdepths.util.IItemProvider;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class RecipeViewerRecipeType {

    public static final RecipeTypeWrapper<CompressingRecipe> V1_COMPRESSING = new RecipeTypeWrapper<>(LostDepthsModRecipeType.V1_COMPRESSING.get(), CompressingRecipe.class, 0, 0, 144, 20, new IItemProvider() {
        @Override
        public @NotNull Item asItem() {
            return LostdepthsModItems.GALACTIC_COMPRESSOR.get();
        }
    });

    public static final RecipeTypeWrapper<CompressingRecipe> V2_COMPRESSING = new RecipeTypeWrapper<>(LostDepthsModRecipeType.V2_COMPRESSING.get(), CompressingRecipe.class, 0, 0, 144, 20, new IItemProvider() {
        @Override
        public @NotNull Item asItem() {
            return LostdepthsModItems.EXTRA_TERESTRIAL_COMPRESSOR.get();
        }
    });

    public static final RecipeTypeWrapper<CompressingRecipe> V3_COMPRESSING = new RecipeTypeWrapper<>(LostDepthsModRecipeType.V3_COMPRESSING.get(), CompressingRecipe.class, 0, 0, 144, 20, new IItemProvider() {
        @Override
        public @NotNull Item asItem() {
            return LostdepthsModItems.BLACK_HOLE_COMPRESSOR.get();
        }
    });

    public static final RecipeTypeWrapper<LDShapedRecipe> GALACTIC_WORKSTATION = new RecipeTypeWrapper<>(LostDepthsModRecipeType.GALACTIC_WORKSTATION.get(), LDShapedRecipe.class, 0, 0, 160, 60, new IItemProvider() {
        @Override
        public @NotNull Item asItem() {
            return LostdepthsModItems.GALACTIC_WORKSTATION.get();
        }
    });

    public static final RecipeTypeWrapper<LDShapedRecipe> ALLOY_WORKSTATION = new RecipeTypeWrapper<>(LostDepthsModRecipeType.ALLOY_WORKSTATION.get(), LDShapedRecipe.class, 0, 0, 160, 60 + 18, new IItemProvider() {
        @Override
        public @NotNull Item asItem() {
            return LostdepthsModItems.ALLOY_WORKSTATION.get();
        }
    });

    public static final RecipeTypeWrapper<ModuleRecipe> MODULE_CREATOR = new RecipeTypeWrapper<>(LostDepthsModRecipeType.MODULE_CREATOR.get(), ModuleRecipe.class, 0, 0, 142, 70, new IItemProvider() {
        @Override
        public @NotNull Item asItem() {
            return LostdepthsModItems.MODULE_CREATOR.get();
        }
    });

}
