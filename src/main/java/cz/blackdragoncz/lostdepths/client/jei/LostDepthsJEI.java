package cz.blackdragoncz.lostdepths.client.jei;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.client.jei.machines.GalacticCompressorRecipeCategory;
import cz.blackdragoncz.lostdepths.client.recipe_view.IRecipeViewerRecipeType;
import cz.blackdragoncz.lostdepths.client.recipe_view.RecipeViewerRecipeType;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.recipe.CompressingRecipe;
import cz.blackdragoncz.lostdepths.recipe.LDRecipeType;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@JeiPlugin
public class LostDepthsJEI implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(LostdepthsMod.MODID, "jei_plugin");
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registry) {

    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(LostdepthsModItems.GALACTIC_COMPRESSOR.get(), 1), recipeType(RecipeViewerRecipeType.V1_COMPRESSING));
        registry.addRecipeCatalyst(new ItemStack(LostdepthsModItems.EXTRA_TERESTRIAL_COMPRESSOR.get(), 1), recipeType(RecipeViewerRecipeType.V2_COMPRESSING));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

        registry.addRecipeCategories(new GalacticCompressorRecipeCategory(guiHelper, RecipeViewerRecipeType.V1_COMPRESSING));
        registry.addRecipeCategories(new GalacticCompressorRecipeCategory(guiHelper, RecipeViewerRecipeType.V2_COMPRESSING));
    }


    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registry) {
        registry.addRecipes(recipeType(RecipeViewerRecipeType.V1_COMPRESSING), LDRecipeType.V1_COMPRESSING.get().getRecipeType().getRecipes(Minecraft.getInstance().level));
        registry.addRecipes(recipeType(RecipeViewerRecipeType.V2_COMPRESSING), LDRecipeType.V2_COMPRESSING.get().getRecipeType().getRecipes(Minecraft.getInstance().level));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        IModPlugin.super.registerGuiHandlers(registration);
    }

    private static final Map<IRecipeViewerRecipeType<?>, RecipeType<?>> recipeTypeInstanceCache = new HashMap<>();


    @SuppressWarnings("unchecked")
    public static <TYPE> RecipeType<TYPE> recipeType(IRecipeViewerRecipeType<TYPE> recipeType) {
        return (RecipeType<TYPE>) recipeTypeInstanceCache.computeIfAbsent(recipeType, r -> new RecipeType<>(r.id(), r.recipeClass()));
    }
}
