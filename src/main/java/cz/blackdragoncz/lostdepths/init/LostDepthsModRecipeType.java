package cz.blackdragoncz.lostdepths.init;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.recipe.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;

public class LostDepthsModRecipeType<C extends Container, RECIPE extends LDRecipe<C>> implements RecipeType<RECIPE>, ILDRecipeTypeProvider<C, RECIPE> {

    public static final DeferredRegister<RecipeType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, LostdepthsMod.MODID);
    public static final RegistryObject<LostDepthsModRecipeType<CraftingContainer, CompressingRecipe>> V1_COMPRESSING = REGISTRY.register("v1_compressing", () -> new LostDepthsModRecipeType<>("v1_compressing"));
    public static final RegistryObject<LostDepthsModRecipeType<CraftingContainer, CompressingRecipe>> V2_COMPRESSING = REGISTRY.register("v2_compressing", () -> new LostDepthsModRecipeType<>("v2_compressing"));
    public static final RegistryObject<LostDepthsModRecipeType<CraftingContainer, CompressingRecipe>> V3_COMPRESSING = REGISTRY.register("v3_compressing", () -> new LostDepthsModRecipeType<>("v3_compressing"));
    public static final RegistryObject<LostDepthsModRecipeType<CraftingContainer, LDShapedRecipe>> GALACTIC_WORKSTATION = REGISTRY.register("galactic_workstation", () -> new LostDepthsModRecipeType<>("galactic_workstation"));
    public static final RegistryObject<LostDepthsModRecipeType<CraftingContainer, LDShapedRecipe>> ALLOY_WORKSTATION = REGISTRY.register("alloy_workstation", () -> new LostDepthsModRecipeType<>("alloy_workstation"));
    public static final RegistryObject<LostDepthsModRecipeType<CraftingContainer, ModuleRecipe>> MODULE_CREATOR = REGISTRY.register("module_creator", () -> new LostDepthsModRecipeType<>("module_creator"));
    public static final RegistryObject<LostDepthsModRecipeType<RecipeWrapper, ItemUseRecipe>> ITEM_USE = REGISTRY.register("item_use", () -> new LostDepthsModRecipeType<>("item_use"));
    public static final RegistryObject<LostDepthsModRecipeType<RecipeWrapper, MetaMaterializerRecipe>> META_MATERIALIZER = REGISTRY.register("meta_materializer", () -> new LostDepthsModRecipeType<>("meta_materializer"));

    private final ResourceLocation registryName;

    public LostDepthsModRecipeType(ResourceLocation registryName) {
        this.registryName = registryName;
    }
    public LostDepthsModRecipeType(String registryName) {
        this.registryName = LostdepthsMod.rl(registryName);
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
    public LostDepthsModRecipeType<C, RECIPE> getRecipeType() {
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
