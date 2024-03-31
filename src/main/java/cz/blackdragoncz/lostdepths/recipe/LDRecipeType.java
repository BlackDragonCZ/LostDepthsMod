package cz.blackdragoncz.lostdepths.recipe;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.checkerframework.checker.units.qual.A;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LDRecipeType<RECIPE extends LDRecipe> implements RecipeType<RECIPE>, ILDRecipeTypeProvider<RECIPE> {

    public static final DeferredRegister<RecipeType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, LostdepthsMod.MODID);

    public static final RegistryObject<LDRecipeType<CompressingRecipe>> COMPRESSING = REGISTRY.register("compressing", () -> new LDRecipeType<>(new ResourceLocation(LostdepthsMod.MODID, "compressing")));

    private final ResourceLocation registryName;

    public LDRecipeType(ResourceLocation registryName) {
        this.registryName = registryName;
    }

    @Override
    public String toString() {
        return registryName.toString();
    }

    @Override
    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public LDRecipeType<RECIPE> getRecipeType() {
        return this;
    }

    @Override
    public List<RECIPE> getRecipes(@Nullable Level level) {
        return getRecipes(level.getRecipeManager(), level);
    }
    public List<RECIPE> getRecipes(RecipeManager recipeManager, @Nullable Level world) {
        List<RECIPE> recipes = recipeManager.getAllRecipesFor(this);

        return recipes;
    }
}
