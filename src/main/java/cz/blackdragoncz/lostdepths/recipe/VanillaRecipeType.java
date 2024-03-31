package cz.blackdragoncz.lostdepths.recipe;

import cz.blackdragoncz.lostdepths.client.recipe_view.IRecipeViewerRecipeType;
import cz.blackdragoncz.lostdepths.util.IItemProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public record VanillaRecipeType<RECIPE extends Recipe<?>>(
        ResourceLocation id,
        RecipeType<RECIPE> vanillaType,
        Class<? extends RECIPE> recipeClass,
        ItemStack iconStack,
        List<IItemProvider> workstations)
    implements IRecipeViewerRecipeType<RECIPE> {

    public VanillaRecipeType(RecipeType<RECIPE> vanillaType, Class<? extends RECIPE> recipeClass, ItemLike item, IItemProvider... altWorkstations) {
        this(Objects.requireNonNull(BuiltInRegistries.RECIPE_TYPE.getKey(vanillaType)), vanillaType, recipeClass, new ItemStack(item), List.of(altWorkstations));
    }

    @Override
    public boolean requiresHolder() {
        return true;
    }

    @Nullable
    @Override
    public ResourceLocation icon() {
        return null;
    }

    @Override
    public int xOffset() {
        return 0;
    }

    @Override
    public int yOffset() {
        return 0;
    }

    @Override
    public int width() {
        return 100;
    }

    @Override
    public int height() {
        return 100;
    }

    @Override
    public Component getTextComponent() {
        return Component.literal(vanillaType.toString());
    }
}
