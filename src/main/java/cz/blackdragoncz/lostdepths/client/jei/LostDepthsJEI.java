package cz.blackdragoncz.lostdepths.client.jei;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.client.jei.machines.AlloyWorkstationRecipeCategory;
import cz.blackdragoncz.lostdepths.client.jei.machines.GenericCompressorRecipeCategory;
import cz.blackdragoncz.lostdepths.client.jei.machines.GalacticWorkstationRecipeCategory;
import cz.blackdragoncz.lostdepths.client.jei.machines.ModuleCreatorRecipeCategory;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import cz.blackdragoncz.lostdepths.init.LostdepthsModMenus;
import cz.blackdragoncz.lostdepths.world.inventory.AlloyWorkstationMenu;
import cz.blackdragoncz.lostdepths.world.inventory.GalacticWorkstationMenu;
import cz.blackdragoncz.lostdepths.world.inventory.ModuleCreatorGUIMenu;
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
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(GalacticWorkstationMenu.class, LostdepthsModMenus.GALACTIC_WORKSTATION_MENU.get(), recipeType(RecipeViewerRecipeType.GALACTIC_WORKSTATION), 1, 6, 7, 36);
        registration.addRecipeTransferHandler(AlloyWorkstationMenu.class, LostdepthsModMenus.ALLOY_WORKSTATION_MENU.get(), recipeType(RecipeViewerRecipeType.ALLOY_WORKSTATION), 1, 7, 8, 36);
        registration.addRecipeTransferHandler(ModuleCreatorGUIMenu.class, LostdepthsModMenus.MODULE_CREATOR_GUI.get(), recipeType(RecipeViewerRecipeType.MODULE_CREATOR), 1, 4, 5, 36);
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(LostdepthsModItems.GALACTIC_COMPRESSOR.get(), 1), recipeType(RecipeViewerRecipeType.V1_COMPRESSING));
        registry.addRecipeCatalyst(new ItemStack(LostdepthsModItems.EXTRA_TERESTRIAL_COMPRESSOR.get(), 1), recipeType(RecipeViewerRecipeType.V2_COMPRESSING));
        registry.addRecipeCatalyst(new ItemStack(LostdepthsModItems.BLACK_HOLE_COMPRESSOR.get(), 1), recipeType(RecipeViewerRecipeType.V3_COMPRESSING));
        registry.addRecipeCatalyst(new ItemStack(LostdepthsModItems.GALACTIC_WORKSTATION.get(), 1), recipeType(RecipeViewerRecipeType.GALACTIC_WORKSTATION));
        registry.addRecipeCatalyst(new ItemStack(LostdepthsModItems.ALLOY_WORKSTATION.get(), 1), recipeType(RecipeViewerRecipeType.ALLOY_WORKSTATION));
        registry.addRecipeCatalyst(new ItemStack(LostdepthsModItems.MODULE_CREATOR.get(), 1), recipeType(RecipeViewerRecipeType.MODULE_CREATOR));
        registry.addRecipeCatalyst(new ItemStack(LostdepthsModItems.META_MATERIALIZER.get(), 1), recipeType(RecipeViewerRecipeType.META_MATERIALIZER));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

        registry.addRecipeCategories(new GenericCompressorRecipeCategory(guiHelper, RecipeViewerRecipeType.V1_COMPRESSING, 15, false));
        registry.addRecipeCategories(new GenericCompressorRecipeCategory(guiHelper, RecipeViewerRecipeType.V2_COMPRESSING, 10, false));
        registry.addRecipeCategories(new GenericCompressorRecipeCategory(guiHelper, RecipeViewerRecipeType.V3_COMPRESSING, 5, true));
        registry.addRecipeCategories(new GalacticWorkstationRecipeCategory(guiHelper, RecipeViewerRecipeType.GALACTIC_WORKSTATION));
        registry.addRecipeCategories(new AlloyWorkstationRecipeCategory(guiHelper, RecipeViewerRecipeType.ALLOY_WORKSTATION));
        registry.addRecipeCategories(new ModuleCreatorRecipeCategory(guiHelper, RecipeViewerRecipeType.MODULE_CREATOR));
        registry.addRecipeCategories(new ItemUseCategory(guiHelper, RecipeViewerRecipeType.ITEM_USE));
        registry.addRecipeCategories(new MetaMaterializerCategory(guiHelper, RecipeViewerRecipeType.META_MATERIALIZER));
    }


    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registry) {
        registry.addRecipes(recipeType(RecipeViewerRecipeType.V1_COMPRESSING), LostDepthsModRecipeType.V1_COMPRESSING.get().getRecipeType().getRecipes(Minecraft.getInstance().level));

        registry.addRecipes(recipeType(RecipeViewerRecipeType.V2_COMPRESSING), LostDepthsModRecipeType.V1_COMPRESSING.get().getRecipeType().getRecipes(Minecraft.getInstance().level));
        registry.addRecipes(recipeType(RecipeViewerRecipeType.V2_COMPRESSING), LostDepthsModRecipeType.V2_COMPRESSING.get().getRecipeType().getRecipes(Minecraft.getInstance().level));
        
        registry.addRecipes(recipeType(RecipeViewerRecipeType.V3_COMPRESSING), LostDepthsModRecipeType.V1_COMPRESSING.get().getRecipeType().getRecipes(Minecraft.getInstance().level));
        registry.addRecipes(recipeType(RecipeViewerRecipeType.V3_COMPRESSING), LostDepthsModRecipeType.V2_COMPRESSING.get().getRecipeType().getRecipes(Minecraft.getInstance().level));
        registry.addRecipes(recipeType(RecipeViewerRecipeType.V3_COMPRESSING), LostDepthsModRecipeType.V3_COMPRESSING.get().getRecipeType().getRecipes(Minecraft.getInstance().level));

        registry.addRecipes(recipeType(RecipeViewerRecipeType.GALACTIC_WORKSTATION), LostDepthsModRecipeType.GALACTIC_WORKSTATION.get().getRecipeType().getRecipes(Minecraft.getInstance().level));
        registry.addRecipes(recipeType(RecipeViewerRecipeType.ALLOY_WORKSTATION), LostDepthsModRecipeType.ALLOY_WORKSTATION.get().getRecipeType().getRecipes(Minecraft.getInstance().level));
        registry.addRecipes(recipeType(RecipeViewerRecipeType.MODULE_CREATOR), LostDepthsModRecipeType.MODULE_CREATOR.get().getRecipeType().getRecipes(Minecraft.getInstance().level));

        registry.addRecipes(recipeType(RecipeViewerRecipeType.ITEM_USE), LostDepthsModRecipeType.ITEM_USE.get().getRecipeType().getRecipes(Minecraft.getInstance().level));
        registry.addRecipes(recipeType(RecipeViewerRecipeType.META_MATERIALIZER), LostDepthsModRecipeType.META_MATERIALIZER.get().getRecipeType().getRecipes(Minecraft.getInstance().level));
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
