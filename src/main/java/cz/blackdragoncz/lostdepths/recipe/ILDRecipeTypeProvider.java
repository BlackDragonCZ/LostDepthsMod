package cz.blackdragoncz.lostdepths.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public interface ILDRecipeTypeProvider<RECIPE extends LDRecipe> {

    default ResourceLocation getRegistryName() {
        return getRecipeType().getRegistryName();
    }

    LDRecipeType<RECIPE> getRecipeType();

    default List<RECIPE> getRecipes(@Nullable Level level) {
        return getRecipeType().getRecipes(level);
    }
}
