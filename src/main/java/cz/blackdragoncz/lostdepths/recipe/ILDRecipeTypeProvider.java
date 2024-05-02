package cz.blackdragoncz.lostdepths.recipe;

import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public interface ILDRecipeTypeProvider<C extends Container, RECIPE extends LDRecipe<C>> {

    default ResourceLocation getRegistryName() {
        return getRecipeType().getRegistryName();
    }

    LostDepthsModRecipeType<C, RECIPE> getRecipeType();

    default List<RECIPE> getRecipes(@Nullable Level level) {
        return getRecipeType().getRecipes(level);
    }
}
