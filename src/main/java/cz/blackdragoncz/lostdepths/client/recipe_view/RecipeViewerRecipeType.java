package cz.blackdragoncz.lostdepths.client.recipe_view;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.recipe.CompressingRecipe;
import cz.blackdragoncz.lostdepths.recipe.LDRecipeType;
import cz.blackdragoncz.lostdepths.recipe.RecipeTypeWrapper;
import cz.blackdragoncz.lostdepths.util.IItemProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class RecipeViewerRecipeType {

    public static final RecipeTypeWrapper<CompressingRecipe> COMPRESSING = new RecipeTypeWrapper<>(LDRecipeType.COMPRESSING.get(), CompressingRecipe.class, 0, 0, 144, 20, new IItemProvider() {
        @Override
        public @NotNull Item asItem() {
            return LostdepthsModItems.GALACTIC_COMPRESSOR.get();
        }
    });

}
