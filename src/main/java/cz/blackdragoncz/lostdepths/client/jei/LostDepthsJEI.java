package cz.blackdragoncz.lostdepths.client.jei;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.client.jei.machines.GenericCompressorRecipeCategory;
import cz.blackdragoncz.lostdepths.client.recipe_view.IRecipeViewerRecipeType;
import cz.blackdragoncz.lostdepths.client.recipe_view.RecipeViewerRecipeType;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
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
        registry.addRecipeCatalyst(new ItemStack(LostdepthsModItems.BLACK_HOLE_COMPRESSOR.get(), 1), recipeType(RecipeViewerRecipeType.V3_COMPRESSING));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

        registry.addRecipeCategories(new GenericCompressorRecipeCategory(guiHelper, RecipeViewerRecipeType.V1_COMPRESSING, 15));
        registry.addRecipeCategories(new GenericCompressorRecipeCategory(guiHelper, RecipeViewerRecipeType.V2_COMPRESSING, 10));
        registry.addRecipeCategories(new GenericCompressorRecipeCategory(guiHelper, RecipeViewerRecipeType.V3_COMPRESSING, 5));
    }


    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registry) {
        registry.addRecipes(recipeType(RecipeViewerRecipeType.V1_COMPRESSING), LostDepthsModRecipeType.V1_COMPRESSING.get().getRecipeType().getRecipes(Minecraft.getInstance().level));

        registry.addRecipes(recipeType(RecipeViewerRecipeType.V2_COMPRESSING), LostDepthsModRecipeType.V1_COMPRESSING.get().getRecipeType().getRecipes(Minecraft.getInstance().level));
        registry.addRecipes(recipeType(RecipeViewerRecipeType.V2_COMPRESSING), LostDepthsModRecipeType.V2_COMPRESSING.get().getRecipeType().getRecipes(Minecraft.getInstance().level));
        
        registry.addRecipes(recipeType(RecipeViewerRecipeType.V3_COMPRESSING), LostDepthsModRecipeType.V1_COMPRESSING.get().getRecipeType().getRecipes(Minecraft.getInstance().level));
        registry.addRecipes(recipeType(RecipeViewerRecipeType.V3_COMPRESSING), LostDepthsModRecipeType.V2_COMPRESSING.get().getRecipeType().getRecipes(Minecraft.getInstance().level));
        registry.addRecipes(recipeType(RecipeViewerRecipeType.V3_COMPRESSING), LostDepthsModRecipeType.V3_COMPRESSING.get().getRecipeType().getRecipes(Minecraft.getInstance().level));
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
