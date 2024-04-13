package cz.blackdragoncz.lostdepths.init;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.recipe.CompressingRecipe;
import cz.blackdragoncz.lostdepths.recipe.ILDRecipeTypeProvider;
import cz.blackdragoncz.lostdepths.recipe.LDRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;

public class LostDepthsModRecipeType<RECIPE extends LDRecipe> implements RecipeType<RECIPE>, ILDRecipeTypeProvider<RECIPE> {

    public static final DeferredRegister<RecipeType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, LostdepthsMod.MODID);
    public static final RegistryObject<LostDepthsModRecipeType<CompressingRecipe>> V1_COMPRESSING = REGISTRY.register("v1_compressing", () -> new LostDepthsModRecipeType<>(new ResourceLocation(LostdepthsMod.MODID, "v1_compressing")));
    public static final RegistryObject<LostDepthsModRecipeType<CompressingRecipe>> V2_COMPRESSING = REGISTRY.register("v2_compressing", () -> new LostDepthsModRecipeType<>(new ResourceLocation(LostdepthsMod.MODID, "v2_compressing")));
    public static final RegistryObject<LostDepthsModRecipeType<CompressingRecipe>> V3_COMPRESSING = REGISTRY.register("v3_compressing", () -> new LostDepthsModRecipeType<>(new ResourceLocation(LostdepthsMod.MODID, "v3_compressing")));

    private final ResourceLocation registryName;

    public LostDepthsModRecipeType(ResourceLocation registryName) {
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
    public LostDepthsModRecipeType<RECIPE> getRecipeType() {
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
